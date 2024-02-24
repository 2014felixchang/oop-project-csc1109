/*
 * Account class:
 * - account info handling (display & manage acc info like acc numbers and types)
 * - balance management (check balance, show current, available)
 * - account operations (deposit, withdraw)
 */
import java.util.ArrayList;

public class Account {
    private String accountNum;
    private double balance = 0;
    private double debt = 0;
    private double transLimit = 1000.00;
    private double interestRate = 0.1; //per year
    private ArrayList<String> history = new ArrayList<String>();

    /*
     * Input: account number
     * 
     * Process: if account number exists in records, initialize account object with attributes pulled from file (pre-existing account)
     *          else initialize account object with default values for attributes (new account)
     */
    public Account(String accNum) {
        this.accountNum = accNum;
        String record = CSVHandler.getRecord(accNum, "Accounts.csv");
        if (record != null) {
            String[] accountData = record.split(",");
            this.balance = Double.parseDouble(accountData[1]);
            this.debt = Double.parseDouble(accountData[2]);
            this.transLimit = Double.parseDouble(accountData[3]);
            if (accountData.length > 4) {
                for (int i = 4; i < accountData.length; i++) {
                    this.history.add(accountData[i]);
                }
            }
        }
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
     * Output: account transfer limit type double
     */
    public double getTransLimit() {
        return this.transLimit;
    }

    /*
     * Input: new limit amount type double
     * 
     * Process: changes account limit to new given limit
     */
    public void setTransferLimit(double newLimit) {
        this.transLimit = newLimit;
    }
    
    /*
     * Input: withdraw amount type double
     * 
     * Process: if amount exceeds balance, loan remaining fund. Else minus amount from balance.
     */
    public void withdraw(double amount) {
        if ((this.balance - amount) < 0) {
            System.out.println("Withdrawal limit reached. Remaining amount will be loaned, and added to debt");
            this.debt += amount - this.balance; //May not be the right idea as there is no auto loan irl
            this.balance = 0.0;
            this.addHistory("Withdrawn: $" + convert2DP(amount));
        }
        else {
            this.balance -= amount;
            this.addHistory("Withdrawn: $" + convert2DP(amount));
        }
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    /*
     * Input: deposit amount type double
     * 
     * Process: adds amount to balance
     */
    public void deposit(double amount) {
        this.balance += amount;
        this.addHistory("Deposited: $" + convert2DP(amount));
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    /*
     * Output: returns balance as String in 2 d.p, rounded up
     */
    public double getBalance() {
        return this.balance;
    }

    /*
     * Input: new account balance amount, type double
     * 
     * Process: sets account balance to given amount
     */
    public void setBalance(double amount) {
        this.balance = amount;   
    }

    /*
    * Output: returns debt as String in 2 d.p, rounded up
    */
    public double getDebt() {
        return this.debt;
    }

    /*
    * Input: amount type double
    * 
    * Process: minus amount from debt, if amount more than debt, add excess to balance
    */
    public void minusDebt(double amount) {
        if (amount > this.debt) {
            this.balance += amount - debt;
            this.debt = 0.0;
        }
        else {
            this.debt -= amount;
        }
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    /*
     * Output: returns interest rate of account
     */
    public double getInterest() {
        return this.interestRate;
    }

    public void transactionHistory() {
        System.out.println(this.accountNum + " Transaction History:");

        for (int i = 0; i < this.history.size() ; i++) {
            System.out.println(this.history.get(i));
        }
    }

    public void clearHistory() {
        this.history.clear();
    }

    public void addHistory(String transaction) {
        this.history.add(transaction);
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    public ArrayList<String> getHistory() {
        return this.history;
    }

    public String convert2DP(double amount) {
        String amt = String.format("%.2f", amount);
        return amt;
    }

    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNum);
        System.out.println("Current Balance: $" + convert2DP(balance));
        System.out.println("Debt: $" + convert2DP(debt));
        System.out.println("Transfer Limit: " + convert2DP(transLimit));
    }

    /*
     * Process: convert account attributes to comma-separated string of format accountNum,balance,debt,transLimit,history...
     * 
     * output: formatted string of account data
     */
    public String convertToCSV() {
        String accountData = this.getAccountNum() + "," + convert2DP(this.getBalance()) + "," + convert2DP(this.getDebt()) + "," + convert2DP(this.getTransLimit());
        if (this.getHistory() != null) {
            for (String i : this.getHistory()) {
                accountData += "," + i;
            }
        }
        return accountData;
    }
    
}   