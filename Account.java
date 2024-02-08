/*
 * Account class:
 * - account info handling (display & manage acc info like acc numbers and types)
 * - balance management (check balance, show current, available)
 * - account operations (deposit, withdraw)
 */

public class Account {
    private String accountNum;
    private double balance;
    private double debt;

    /*
     * Input: account number, current balance, account debt (pulled from file)
     * 
     * Process: initialize account object with pulled info
     */
    public Account(String accNum, double balance, double debt) {
        this.accountNum = accNum;
        this.balance = balance;
        this.debt = debt;
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
            this.debt += this.balance - amount;
            this.balance = 0.0;
        }
        else {
            this.balance -= amount;
        }
    }

    /*
     * Input: deposit amount type double
     * 
     * Process: adds amount to balance
     */
    public void deposit(double amount) {
        this.balance += amount;
    }

    /*
     * Output: returns balance as String in 2 d.p, rounded up
     */
    public String getBalance() {
        String balance = String.format("$%.2f", this.balance);
        //double balance = this.balance;
        return balance;
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
        String debt = String.format(".2f", this.debt);
        return debt;
    }

    public void transferFunds(double amount, Account toAccount) {
        if (this.balance < amount) {
            System.out.println("Insufficient balance to transfer funds.");
            return;
        }

        this.balance -= amount;
        toAccount.deposit(amount);
    }

    public static void main(String[] args) {
        Account acc1 = new Account("accountno123", 1000, 0);
        Account acc2 = new Account("accountno456", 500, 0);
        acc1.transferFunds(200, acc2);
        System.out.println(acc1.getBalance());  // Should print 800.00
        System.out.println(acc2.getBalance());  // Should print 700.00
    }

}   