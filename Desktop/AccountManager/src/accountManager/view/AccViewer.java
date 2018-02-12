package accountManager.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import accountManager.controller.AccController;
import accountManager.controller.Controller;
import accountManager.model.AccModel;
import accountManager.model.Model;
import accountManager.model.ModelEvent;
import accountManager.model.OpenView;
import java.awt.Color;
import java.io.IOException;
import javax.swing.JTextField;

public class AccViewer extends JFrameView {
	
	private JPanel appPanel;
	private JComboBox cb;
	public static final String editDollars = "Edit Account in $";
	public static final String editEuros = "Edit Account in Euros";
	public static final String editYen = "Edit Account in Yen";
	public static final String depositAgent = "Start Deposit Agent";
	public static final String withdrawAgent = "Start Withdraw Agent";
	public static final String SAVE = "Save";
	public static final String EXIT = "Exit";
	private JTextField textField;
	
        //start app
        
        
	public static void main(String[] args) throws IOException {
		new AccController();
	}

    public AccViewer(Model model, Controller controller) {
        super(model, controller);
    }

	//show JFrame
    
	public AccViewer(AccModel model, AccController controller) throws IOException {
		super(model, controller);
		ArrayList<String> accList = new ArrayList<>();
		
		Scanner scanner = new Scanner(System.in);
		
		while (true) {
			System.out.println("Please type file name you would like to input: ");
			String file = scanner.nextLine();
			try {
				((AccController) getController()).openFile(file);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(appPanel, "Invalid Account Field for " + file + " on line "+(((AccController)getController()).getAccCount()+1)+".\n"
						+ "Name must be string.\n" + "ID must be integer.\n" + "Balance must be double.");
				e.printStackTrace();
				System.exit(0);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(appPanel, "Invalid File.");
				continue;
			} 
			break;
		}
		for(int i=0;i<((AccController)getController()).getAccCount();i++) {
			int userID = ((AccController)getController()).getID(i);
			String userName = ((AccController)getController()).getName(i);
			accList.add(userID + " : " + userName);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 320);
		appPanel = new JPanel();
		appPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(appPanel);
		appPanel.setLayout(null);
                 
		Handler handler = new Handler();
		            
		cb = new JComboBox(accList.toArray());
		cb.setBounds(120, 40, 250, 22);
		appPanel.add(cb);
		
		JButton saveButton = new JButton(SAVE);
		saveButton.addActionListener(handler);
		saveButton.setBounds(99, 239, 89, 23);
                saveButton.setBackground(Color.RED);
                saveButton.setOpaque(true);
                saveButton.setBorderPainted(false);
		appPanel.add(saveButton);
		
		JButton exitButton = new JButton(EXIT);
		exitButton.addActionListener(handler);
		exitButton.setBounds(289, 239, 89, 23);
                exitButton.setBackground(Color.RED);
                exitButton.setOpaque(true);
                exitButton.setBorderPainted(false);
		appPanel.add(exitButton);
		
		JButton editDollarButton = new JButton(editDollars);
		editDollarButton.addActionListener(handler);
		editDollarButton.setBounds(20, 100, 200, 30);
                editDollarButton.setBackground(Color.GREEN);
                editDollarButton.setOpaque(true);
                editDollarButton.setBorderPainted(false);
		appPanel.add(editDollarButton);
		
		JButton editEurosButton = new JButton(editEuros);
		editEurosButton.addActionListener(handler);
		editEurosButton.setBounds(20, 137, 200, 30);
                editEurosButton.setBackground(Color.GREEN);
                editEurosButton.setOpaque(true);
                editEurosButton.setBorderPainted(false);
		appPanel.add(editEurosButton);
		
		JButton editYenButton = new JButton(editYen);
		editYenButton.addActionListener(handler);
		editYenButton.setBounds(20, 173, 200, 30);
                editYenButton.setBackground(Color.GREEN);
                editYenButton.setOpaque(true);
                editYenButton.setBorderPainted(false);
		appPanel.add(editYenButton);
		
		JButton depositAgentButton = new JButton(depositAgent);
		depositAgentButton.addActionListener(handler);
		depositAgentButton.setBounds(230, 100, 200, 40);
                depositAgentButton.setBackground(Color.BLUE);
                depositAgentButton.setOpaque(true);
                depositAgentButton.setBorderPainted(false);
		appPanel.add(depositAgentButton);
		
		JButton withdrawAgentButton = new JButton(withdrawAgent);
		withdrawAgentButton.addActionListener(handler);
		withdrawAgentButton.setBounds(230, 163, 200, 40);
                withdrawAgentButton.setBackground(Color.BLUE);
                withdrawAgentButton.setOpaque(true);
                withdrawAgentButton.setBorderPainted(false);
		appPanel.add(withdrawAgentButton);
	}
	
	public void modelChanged(ModelEvent event) {
			
		 }
	
	public void exit() {
		System.exit(0);
	}
	
	class Handler implements ActionListener { 
		public void actionPerformed(ActionEvent e) {
			try {
				((AccController)getController()).btnAccountPressed(e.getActionCommand(), cb.getSelectedIndex());
			} catch (OpenView e1) {
				if (e.getActionCommand() == editDollars) {
					JOptionPane.showMessageDialog(appPanel, "Edit Dollar already open.");
				} else if (e.getActionCommand() == editEuros) {
					JOptionPane.showMessageDialog(appPanel, "Edit Euro already open.");
				} else if (e.getActionCommand() == editYen) {
					JOptionPane.showMessageDialog(appPanel, "Edit Yen already open.");
				}
			}
	    } }
}
