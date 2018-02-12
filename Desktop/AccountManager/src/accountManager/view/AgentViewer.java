package accountManager.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;

import accountManager.controller.AccController;
import accountManager.model.AccModel;
import accountManager.model.ModelEvent;

public class AgentViewer extends JFrameView {

	private final JPanel contentPane;
	private final JTextField txtAgentID;
	private final JTextField txtAmount;
	private final JTextField txtOperations;
	public static final String DISMISS = "Dismiss";
	public static final String startAgent = "Start Agent";

	public int accIndex;
	public int viewIndex;
	
	public AgentViewer(AccModel model, AccController controller, int accountIndex, int viewIndex, String state) {
		super(model, controller);
		this.accIndex = accountIndex;
		this.viewIndex = viewIndex;
		
		setTitle("Start " + state + " agent for account: " + ((AccController)getController()).getID(accountIndex));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Handler handler = new Handler();
		
		JLabel lblAgentId = new JLabel("Agent ID:");
		lblAgentId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAgentId.setBounds(142, 54, 59, 14);
		contentPane.add(lblAgentId);
		
		txtAgentID = new JTextField();
		txtAgentID.setBounds(211, 51, 114, 20);
		contentPane.add(txtAgentID);
		txtAgentID.setColumns(10);
		
		JLabel label = new JLabel("Amount in $:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(112, 79, 89, 14);
		contentPane.add(label);
		
		txtAmount = new JTextField();
		txtAmount.setColumns(10);
		txtAmount.setBounds(211, 76, 114, 20);
		contentPane.add(txtAmount);
		
		txtOperations = new JTextField();
		txtOperations.setText("0.0");
		txtOperations.setColumns(10);
		txtOperations.setBounds(211, 102, 114, 20);
		contentPane.add(txtOperations);
		
		JLabel label_1 = new JLabel("Operations per sec:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(58, 105, 143, 14);
		contentPane.add(label_1);
		
		JButton btnStartAgent = new JButton(startAgent);
		btnStartAgent.addActionListener(handler);
		btnStartAgent.setBounds(159, 154, 108, 23);
		contentPane.add(btnStartAgent);
		
		JButton btnDismiss = new JButton(DISMISS);
		btnDismiss.addActionListener(handler);
		btnDismiss.setBounds(159, 204, 108, 23);
		contentPane.add(btnDismiss);
	}

	public void modelChanged(ModelEvent event) {
		
	}
	
	public void exit() {
		setVisible(false);
	}
	
	class Handler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			try {
				((AccController)getController()).btnAgentPressed(e.getActionCommand(), viewIndex, accIndex, txtAgentID.getText(),
						txtAmount.getText(),txtOperations.getText());
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(contentPane, "Agent Input is Insufficient or Improper.");
			}
	    } }
}
