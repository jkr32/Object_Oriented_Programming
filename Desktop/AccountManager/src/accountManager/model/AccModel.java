package accountManager.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.SwingUtilities;

public class AccModel extends AbstModel{
	
	private String file;
	
	public ArrayList<Account> accounts = new ArrayList<>();
	public int numAccounts = 0;
	public boolean saved = false;
	
	public void openFile(String file) throws FileNotFoundException, NumberFormatException {
		this.file = file;
		
		Scanner scanner = new Scanner(new File(file));
		String line;
		while(scanner.hasNextLine()) {
			line = scanner.nextLine();
			String[] parts = line.split(",");
			String name = parts[0];
			int ID = Integer.parseInt(parts[1]);
			double balance = Double.parseDouble(parts[2]);
			accounts.add(new Account(name, ID, balance));
			numAccounts++;
		}
		
		scanner.close();
		
		Collections.sort(accounts, Account.IDCompare);
	}
	
	public synchronized void deposit(double amount, String state, int index) {
		double balance = accounts.get(index).getBalance();
		if(state == "Euros") {amount /= 0.79;}
		else if(state == "Yen") {amount /= 94.1;}

		balance += amount;
		this.saved = false;
		accounts.get(index).setBalance(balance);
		
		final ModelEvent me = new ModelEvent(this, 1, "");
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
		notifyAll();
	}
	
	public synchronized void withdraw(double amount, String state, int index) {
		double balance = accounts.get(index).getBalance();
		if(state == "Euros") {amount /= 0.79;}
		else if(state == "Yen") {amount /= 94.1;}

		balance -= amount;
		
		this.saved = false;
		accounts.get(index).setBalance(balance);
		
		final ModelEvent me = new ModelEvent(this, 1, "");
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						notifyChanged(me);
					}
				});
		notifyAll();
	}
	
	public String getName(int index) {return accounts.get(index).getName();}
	public int getID(int index) {return accounts.get(index).getID();}
	public double getBalance(int index) {return accounts.get(index).getBalance();}
	public int getAccCount() {return numAccounts;}
	
	public void save() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(this.file));
			out.flush();
			
			for(int i=0;i<numAccounts;i++) {
				String line = accounts.get(i).getName() + "," + accounts.get(i).getID() + "," + accounts.get(i).getBalance();
				out.write(line);
				out.newLine();
			}
			
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}