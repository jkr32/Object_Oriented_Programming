package accountManager.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;

import accountManager.controller.AccController;
import accountManager.model.AccModel;
import accountManager.model.ModelEvent;

public class RunnerViewer extends JFrameView{

	private final JPanel contentPane;
	private final JButton btnDismiss;
	private final JButton btnStopAgent;
	private final JTextField txtAmount;
	private final JTextField txtOperations;
	private final JTextField txtTransferred;
	private final JTextField txtCompleted;
	private final JTextField txtState;
	public static final String DISMISS = "Dismiss";
	public static final String STOPAG = "Stop Agent";
	
	private final int accountIndex;
	private int agentID;
	private double amount;
	private double operations;
	private final String depOrWith;
	private final int viewIndex;
	private String state;

	public RunnerViewer(AccModel model, AccController controller, int accountIndex, int viewIndex, String depOrWith) {
		super(model, controller);
		this.accountIndex = accountIndex;
		this.depOrWith = depOrWith;
		this.viewIndex = viewIndex;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Handler handler = new Handler();
		
		JLabel lblAmountIn = new JLabel("Amount in $:");
		lblAmountIn.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmountIn.setBounds(100, 13, 108, 16);
		contentPane.add(lblAmountIn);
		
		JLabel label = new JLabel("Operations per second:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(65, 36, 143, 16);
		contentPane.add(label);
		
		txtAmount = new JTextField();
		txtAmount.setEditable(false);
		txtAmount.setBounds(220, 10, 116, 22);
		contentPane.add(txtAmount);
		txtAmount.setColumns(10);
		
		txtOperations = new JTextField();
		txtOperations.setEditable(false);
		txtOperations.setBounds(220, 33, 116, 22);
		contentPane.add(txtOperations);
		txtOperations.setColumns(10);
		
		JLabel lblAmountIn_1 = new JLabel("Amount in $");
		lblAmountIn_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblAmountIn_1.setBounds(65, 80, 86, 16);
		contentPane.add(lblAmountIn_1);
		
		JLabel lblTransferred = new JLabel("transferred:");
		lblTransferred.setHorizontalAlignment(SwingConstants.CENTER);
		lblTransferred.setBounds(65, 94, 86, 16);
		contentPane.add(lblTransferred);
		
		txtTransferred = new JTextField();
		txtTransferred.setEditable(false);
		txtTransferred.setHorizontalAlignment(SwingConstants.CENTER);
		txtTransferred.setText("0");
		txtTransferred.setBounds(52, 109, 116, 22);
		contentPane.add(txtTransferred);
		txtTransferred.setColumns(10);
		
		JLabel lblOperations = new JLabel("Operations");
		lblOperations.setHorizontalAlignment(SwingConstants.CENTER);
		lblOperations.setBounds(260, 80, 86, 16);
		contentPane.add(lblOperations);
		
		JLabel lblCompleted = new JLabel("completed:");
		lblCompleted.setHorizontalAlignment(SwingConstants.CENTER);
		lblCompleted.setBounds(260, 94, 86, 16);
		contentPane.add(lblCompleted);
		
		txtCompleted = new JTextField();
		txtCompleted.setEditable(false);
		txtCompleted.setHorizontalAlignment(SwingConstants.CENTER);
		txtCompleted.setText("0");
		txtCompleted.setBounds(246, 109, 116, 22);
		contentPane.add(txtCompleted);
		txtCompleted.setColumns(10);
		
		JLabel lblState = new JLabel("State:");
		lblState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblState.setBounds(138, 157, 56, 16);
		contentPane.add(lblState);
		
		txtState = new JTextField();
		txtState.setEditable(false);
		txtState.setText("Running");
		txtState.setBounds(206, 154, 80, 22);
		contentPane.add(txtState);
		txtState.setColumns(10);
		
		btnStopAgent = new JButton(STOPAG);
		btnStopAgent.addActionListener(handler);
		btnStopAgent.setBounds(71, 202, 97, 25);
		contentPane.add(btnStopAgent);
		
		btnDismiss = new JButton(DISMISS);
		btnDismiss.addActionListener(handler);
		btnDismiss.setEnabled(false);
		btnDismiss.setBounds(249, 202, 97, 25);
		contentPane.add(btnDismiss);
	}
	
	public void setVariables(int agentID, double amount, double operations) {
		this.agentID = agentID;
		this.amount = amount;
		this.operations = operations;
		this.state = "Running";
		txtAmount.setText(amount + "");
		txtOperations.setText(operations + "");
		setTitle(this.depOrWith + " agent " + this.agentID + " for account " + ((AccController)getController()).getID(this.accountIndex));
	}
	
	public void setNewState(String newState) {
		this.state = newState;
		txtState.setText(state);
	}
	
	public void exit() {
		setVisible(false);
	}

	public void modelChanged(ModelEvent event) {
		if(state == "Stopped") {
			btnDismiss.setEnabled(true);
			btnStopAgent.setEnabled(false);
		}
		int totalOp;
		totalOp = ((AccController)getController()).getTotalOp(agentID);
		double totalTrans = ((AccController)getController()).getTotalTrans(agentID);
		txtCompleted.setText(totalOp + "");
		txtTransferred.setText("$"+totalTrans);
	}
	
	class Handler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			((AccController)getController()).btnRunnerPressed(e.getActionCommand(), viewIndex, agentID);
	    } }
}
