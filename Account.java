/*
 * Account class:
 * - account info handling (display & manage acc info like acc numbers and types)
 * - balance management (check balance, show current, available)
 * - account operations (deposit, withdraw)
 */
import java.util.ArrayList;

public class Account {
    private String accountNum;
    private double balance;
    private double debt;
    private ArrayList<String> history;

    /*
     * Input: account number, current balance, account debt (pulled from file)
     * 
     * Process: initialize account object with pulled info
     */
    public Account(String accNum, double balance, double debt) {
        this.accountNum = accNum;
        this.balance = balance;
        this.debt = debt;
        this.history = new ArrayList<String>();
    }

    /*
     * Output: account number type String
     */
    public String getAccountNum() {
        return this.accountNum;
    }

    /*
     * Input: new account number type String
     * 
     * Process: changes account number to new given number
     */
    public void setAccountNum(String newNum) {
        this.accountNum = newNum;
    }
    
    /*
     * Input: withdrawal amount type double
     * 
     * Process: if amount exceeds balance, loan remaining fund. Else minus amount from balance.
     */
    public void withdraw(double amount) {
        if ((this.balance - amount) < 0) {
            System.out.println("Withdrawal limit reached. Remaining amount will be loaned, and added to debt");
            this.debt += this.balance - amount; //May not be the right idea as there is no auto loan irl
            this.balance = 0.0;
        }
        else {
            this.balance -= amount;
            this.history.add ("Withdrawn: $" + convert(amount));
        }
    }

    /*
     * Input: deposit amount type double
     * 
     * Process: adds amount to balance
     */
    public void deposit(double amount) {
        this.balance += amount;
        this.history.add ("Deposited: $" + convert(amount));
    }

    /*
     * Output: returns balance as String in 2 d.p, rounded up
     */
    public String getBalance() {
        //double balance = this.balance;
        return convert(balance);
    }

    /*
    * Input: amount type double
    * 
    * Process: minus amount from debt, if amount more than debt, add excess to balance
    */
    public void payDebt(double amount) {
        if (amount > this.debt) {
            this.debt = 0.0;
            this.balance += amount - debt;
            System.out.println("Debt payment exceeds debt amount, remaining funds added to balance.");
        }
        else {
            this.debt -= amount;
        }
    }

    /*
    * Output: returns debt as String in 2 d.p, rounded up
    */
    public String getDebt() {
        String debt = String.format("%.2f", this.debt);
        return debt;
    }

    // See bank.java
    // public void transferFunds(double amount, Account toAccount) {
    //     if (this.balance < amount) {
    //         System.out.println("Insufficient balance to transfer funds.");
    //         return;
    //     }

    //     this.balance -= amount;
    //     toAccount.deposit(amount);
    //     this.history.add ("Transfered $" + convert(amount) + " to Account Number: " + toAccount.getAccountNum());
    //     toAccount.history.add ("Received $" + convert(amount) + " from Account Number: " + this.accountNum);
    // }
    
    public void transactionHistory() {
        System.out.println(this.accountNum + " Transaction History:");

        for (int i = 0; i < this.history.size() ; i++) {
            System.out.println(this.history.get(i));
        }
    }

    public String convert(double amount) {
        String amt = String.format("%.2f", amount);
        return amt;
    }

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNum);
        System.out.println("Current Balance: $" + convert(balance));
        System.out.println("Debt: $" + convert(debt));
    }
    public static void main(String[] args) {
        Account acc1 = new Account("accountno123", 1000, 0);
        Account acc2 = new Account("accountno456", 500, 0);
        // acc1.transferFunds(200, acc2);
        acc1.deposit(100);
        System.out.println(acc1.getBalance());  // Should print 800.00
        System.out.println(acc2.getBalance());  // Should print 700.00
        acc1.transactionHistory();
        acc2.transactionHistory();
        acc1.displayAccountInfo();
    }

}   