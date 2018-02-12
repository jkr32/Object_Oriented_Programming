package accountManager.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.*;

import accountManager.controller.AccController;
import accountManager.model.AccModel;
import accountManager.model.ModelEvent;
import accountManager.model.NegBalance;

public class EditViewer extends JFrameView {

	private final JPanel contentPane;
	private final JTextField txtText;
	private final JTextField textField;
	public static final String DISMISS = "Dismiss";
	public static final String DEPOSIT = "Deposit";
	public static final String WITHDRAW = "Withdraw";
	public String state;
	public int index;
	public double balance;
	public int viewIndex;


	public EditViewer(AccModel model, AccController controller, String state, int index, int viewIndex) {
		super(model, controller);
		this.state = state;
		this.index = index;
		this.viewIndex = viewIndex;
		if(state == "Dollars") {this.balance = ((AccController)getController()).getBalance(this.index);}
		else if(state == "Euros") {this.balance = ((AccController)getController()).getBalance(this.index) * 0.79;}
		else if(state == "Yen") {this.balance = ((AccController)getController()).getBalance(this.index) * 94.1;}
		
		setTitle(((AccController)getController()).getName(this.index) + " - " + ((AccController)getController()).getID(this.index) + "; Operations in "
				+ this.state);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Handler handler = new Handler();
		
		JLabel lblBalance = new JLabel("Account funds: ");
		lblBalance.setBounds(87, 42, 139, 14);
		contentPane.add(lblBalance);
		
		txtText = new JTextField();
		txtText.setEditable(false);
		txtText.setText(String.valueOf(Math.round(this.balance*100.0)/100.0) + " " + this.state);
		txtText.setBounds(209, 39, 150, 20);
		contentPane.add(txtText);
                txtText.setHorizontalAlignment(SwingConstants.RIGHT);
		txtText.setColumns(10);
		
		JLabel lblEnterAmount = new JLabel("Enter Amount in "+ ": ");
		lblEnterAmount.setBounds(73, 88, 139, 14);
		contentPane.add(lblEnterAmount);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setText("0.0");
		textField.setBounds(222, 85, 99, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnDeposit = new JButton(DEPOSIT);
		btnDeposit.addActionListener(handler);
		btnDeposit.setBounds(106, 143, 89, 23);
		contentPane.add(btnDeposit);
		
		JButton btnWithdraw = new JButton(WITHDRAW);
		btnWithdraw.addActionListener(handler);
		btnWithdraw.setBounds(219, 143, 89, 23);
		contentPane.add(btnWithdraw);
		
		JButton btnDismiss = new JButton(DISMISS);
		btnDismiss.addActionListener(handler);
		btnDismiss.setBounds(166, 211, 89, 23);
		contentPane.add(btnDismiss);
	}

	public void modelChanged(ModelEvent event) {
		double bal = ((AccController)getController()).getBalance(index);
		if(this.state == "Euros") {bal *= 0.79;}
		else if(this.state == "Yen") {bal *= 94.1;}
		txtText.setText(String.valueOf(Math.round(bal*100.0)/100.0) + " " + this.state);
		textField.setText("0.0");
	}
	
	public void exit() {
		setVisible(false);
	}
	
	class Handler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			try {
				((AccController)getController()).btnEditPressed(e.getActionCommand(), state, textField.getText(), index, viewIndex);
			} catch (NegBalance e1) {
				JOptionPane.showMessageDialog(contentPane, "Insufficient funds: amount to withdraw is " + textField.getText() + ", it is greater than available funds: "
						+ balance);
			} 
	    } }
}
