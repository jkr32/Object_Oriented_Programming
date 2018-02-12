package accountManager.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import accountManager.model.AccModel;
import accountManager.model.ModelEvent;
import accountManager.model.NegBalance;
import accountManager.model.OpenView;
import accountManager.view.AccViewer;
import accountManager.view.AgentViewer;
import accountManager.view.EditViewer;
import accountManager.view.JFrameView;
import accountManager.view.RunnerViewer;
import java.io.IOException;

public class AccController extends AbstController{
	private final ArrayList<EditViewer> editors = new ArrayList<>();
	private int numEditors = 0;
	
	private final ArrayList<AgentViewer> agents = new ArrayList<>();
	private final ArrayList<RunnerViewer> runners = new ArrayList<>();
	private final HashMap<Integer,Operator> operators = new HashMap<>();
	private final HashMap<Integer,Timer> timers = new HashMap<>();
	private final ArrayList<Boolean> isDeposit = new ArrayList<>();
	private int numAgents = 0;
	
	public AccController() throws IOException {
		setModel(new AccModel());
		setView(new AccViewer((AccModel)getModel(), this));
		((JFrameView)getView()).setVisible(true);
	}
	
	public void btnAccountPressed(String option, int index) throws OpenView {
		if(option.equals(AccViewer.editDollars)) {
			editors.add(new EditViewer((AccModel)getModel(), this, "Dollars", index, numEditors));
			(editors.get(numEditors)).setVisible(true);
			numEditors++;
		} 
		else if(option.equals(AccViewer.editEuros)) {
			editors.add(new EditViewer((AccModel)getModel(), this, "Euros", index, numEditors));
			(editors.get(numEditors)).setVisible(true);
			numEditors++;
		} 
		else if(option.equals(AccViewer.editYen)) {
			editors.add(new EditViewer((AccModel)getModel(), this, "Yen", index, numEditors));
			(editors.get(numEditors)).setVisible(true);
			numEditors++;
		} 
		else if(option.equals(AccViewer.depositAgent)) {
			agents.add(new AgentViewer((AccModel)getModel(),this, index, numAgents, "Deposit"));
			runners.add(new RunnerViewer((AccModel)getModel(),this, index, numAgents, "Deposit"));
			isDeposit.add(true);
			
			(agents.get(numAgents)).setVisible(true);
			numAgents++;
		}
		else if(option.equals(AccViewer.withdrawAgent)) {
			agents.add(new AgentViewer((AccModel)getModel(),this, index, numAgents, "Withdraw"));
			runners.add(new RunnerViewer((AccModel)getModel(),this, index, numAgents, "Withdraw"));
			isDeposit.add(false);
			
			(agents.get(numAgents)).setVisible(true);
			numAgents++;
		}
		else if(option.equals(AccViewer.SAVE)) {
			((AccModel)getModel()).save();
			((AccModel)getModel()).saved = true;
		} 
		else if(option.equals(AccViewer.EXIT)) {
			if(!((AccModel)getModel()).saved) {
				((AccModel)getModel()).save();
			}
			((AccViewer)getView()).exit();
		} 
	}
	
	public void btnEditPressed(String option, String state, String amount, int accountIndex, int viewIndex) throws NegBalance {
		if(option.equals(EditViewer.DEPOSIT)) {
			((AccModel)getModel()).deposit(Double.parseDouble(amount), state, accountIndex);
		} else if(option.equals(EditViewer.WITHDRAW)) {
			double tester = Double.parseDouble(amount);
			if(state == "Euros") {tester /= 0.79;}
			else if(state == "Yen") {tester /= 94.1;}
			if (((AccModel)getModel()).getBalance(accountIndex) > tester) {
				((AccModel) getModel()).withdraw(Double.parseDouble(amount), state, accountIndex);
			} else{throw new NegBalance();}
		} else if(option.equals(EditViewer.DISMISS)) {
			((EditViewer) editors.get(viewIndex)).exit();
		}
	}
	
	public void btnAgentPressed(String option, int viewIndex, int accountIndex, String agentID, String amount, String operations) throws NumberFormatException{
		if(option.equals(AgentViewer.DISMISS)) {
			((AgentViewer) agents.get(viewIndex)).exit();
		} else if(option.equals(AgentViewer.startAgent)) {
			int agID = Integer.parseInt(agentID);
			double am = Double.parseDouble(amount);
			double op = Double.parseDouble(operations);
			
			((RunnerViewer)runners.get(viewIndex)).setVariables(agID, am, op);
			((JFrameView)runners.get(viewIndex)).setVisible(true);
			((AgentViewer) agents.get(viewIndex)).exit();
			
			op = 1000 / op;
			
			operators.put(agID, new Operator(am,isDeposit.get(viewIndex),accountIndex, viewIndex));
			timers.put(agID, new Timer());
			timers.get(agID).schedule(operators.get(agID), 0, (long) op);
		}
	}
	
	public void btnRunnerPressed(String option, int viewIndex, int agentID) {
		if(option.equals(RunnerViewer.DISMISS)) {
			((RunnerViewer) runners.get(viewIndex)).exit();
		}
		else if(option.equals(RunnerViewer.STOPAG)) {
			((RunnerViewer) runners.get(viewIndex)).setNewState("Stopped");
			
			timers.get(agentID).cancel();
			
			final ModelEvent me = new ModelEvent(this, 1, "");
			SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							((AccModel)getModel()).notifyChanged(me);
						}
					});
			notifyAll();
		}
	}
	
	public void openFile(String file) throws NumberFormatException, FileNotFoundException {
		((AccModel)getModel()).openFile(file);
	}
	
	public int getAccCount() {return ((AccModel)getModel()).getAccCount();}
	public String getName(int index) {return ((AccModel)getModel()).getName(index);}
	public int getID(int index) {return ((AccModel)getModel()).getID(index);}
	public double getBalance(int index) {return ((AccModel)getModel()).getBalance(index);}
	public int getTotalOp(int agentID) {return (operators.get(agentID)).getTotalOp();}
	public double getTotalTrans(int agentID) {return (operators.get(agentID)).getTotalTrans();}
	
	private class Operator extends TimerTask {
		private final double amount;
		private final boolean isDeposit;
		private final int accountIndex;
		private int totalOp = 0;
		private final int viewerIndex;
		private double totalTrans = 0.0;
		
		public Operator(double amount, boolean isDeposit, int accountIndex, int viewerIndex) {
			this.amount = amount;
			this.isDeposit = isDeposit;
			this.accountIndex = accountIndex;
			this.viewerIndex = viewerIndex;
		}

		public synchronized void run() {
			if(isDeposit) {
				((AccModel)getModel()).deposit(amount,"Dollars",accountIndex);
				((RunnerViewer)runners.get(viewerIndex)).setNewState("Running");
                                totalOp++;
                                totalTrans += amount;
			} else {
				if(amount>getBalance(accountIndex)) {
					((RunnerViewer)runners.get(viewerIndex)).setNewState("Blocked");
				} else {
					((AccModel)getModel()).withdraw(amount,"Dollars",accountIndex);
					((RunnerViewer)runners.get(viewerIndex)).setNewState("Running");
                                        totalOp++;
                                        totalTrans += amount;
				}
			}
			
			totalOp++;
			totalTrans += amount;
		}
		
		public int getTotalOp() {return totalOp;}
		public double getTotalTrans() {return totalTrans;}
	}
}