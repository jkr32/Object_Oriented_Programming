package accountManager.model;

import java.util.Comparator;

public class Account {
	private String name;
	private int ID;
	private double balance;
	
	public Account(String name, int ID, double balance) {
		this.name = name;
		this.ID = ID;
		this.balance = balance;
	}

	public String getName() {
		return name;
	}
        
        public void setName(String name) {
		this.name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public static Comparator<Account> IDCompare = new Comparator<Account>() {

		public int compare(Account a1, Account a2) {
		   int Acc1 = a1.getID();
		   int Acc2 = a2.getID();

		   return Acc1 - Acc2;
	    }};


	@Override
    public String toString() {
        return "[ name=" + name + ", ID=" + ID + ", balance=" + balance + "]";
    }
}
