import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Account class:
 * Account info handling (display and manage acc info like acc numbers and types).
 * Balance management (check balance, show current, available).
 * Account operations (deposit, withdraw).
 */
 public class Account {
    private String accountNum;
    private double balance;
    private double transLimit;
    private ArrayList<String> history = new ArrayList<String>();
    private G16_LON loan;


    /**
     * Constructs an Account object with existing information given the account number exists in file.
     * Else, an account object with default values for attributes are created.
     *
     * @param accNum the account number of an account
     */
    public Account(String accNum) {
        this.accountNum = accNum;
        String record = CSVHandler.getRecord(accNum, "Accounts.csv");
        if (record != null) {
            String[] accountData = record.split(",");
            this.balance = Double.parseDouble(accountData[1]);
            this.transLimit = Double.parseDouble(accountData[2]);
            if (!accountData[3].equals(" ")) {
                double principal = Double.parseDouble(accountData[3]);
                double interestRate = Double.parseDouble(accountData[4]);
                LocalDate loanStartDate = LocalDate.parse(accountData[5]);
                int loanTermMonths = Integer.parseInt(accountData[6]);
                this.loan = new G16_LON(principal, interestRate, loanStartDate, loanTermMonths);
                double totalRepayment = loan.getTotalPayment();
                double loanRepaymentLeft = Double.parseDouble(accountData[7]);
                double amountPaid = totalRepayment - loanRepaymentLeft;
                // this will effectively make the loan object's repayment amount be the correct amount
                // need to do this because the class has no setter method to set the loan repayment amount
                this.loan.payLoan(amountPaid);
            }
            else {
                this.loan = null;
            }
            if (accountData.length > 7) {
                for (int i = 8; i < accountData.length; i++) {
                    this.history.add(accountData[i]);
                }
            }
        }
        else {
            this.balance = 0.0;
            this.transLimit = 1000.0;
            this.loan = null;
        }
    }

    /**
     * Gets the account's number
     * 
     * @return the account number
     */
    public String getAccountNum() {
        return this.accountNum;
    }

    /**
     * Changes account number of an account object to new given number (not likely to be used because account number should not change)
     * 
     * @param newNum the new account number
     */
    public void setAccountNum(String newNum) {
        this.accountNum = newNum;
    }

    /**
     * Gets the account's transfer limit
     * 
     * @return the account's transfer limit
     */
    public double getTransLimit() {
        return this.transLimit;
    }

    /**
     * Changes account transfer limit to new given limit
     * 
     * @param newLimit the new transfer limit for an account
     */
    public void setTransferLimit(double newLimit) {
        this.transLimit = newLimit;
    }
    
    /**
     * Minuses withdrawal amount from account balance. If amount exceeds balance, bank loans remaining funds required.
     * Loan amount is added to account's debt.
     * 
     * @param amount the amount to be withdrawn from the account
     */
    public void withdraw(double amount) {
        if ((this.balance - amount) < 0) {
            this.balance = 0.0;
            this.addHistory("Withdrawn: $" + convert2DP(amount));
        }
        else {
            this.balance -= amount;
            this.addHistory("Withdrawn: $" + convert2DP(amount));
        }
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    /**
     * Adds deposited amount to account balance. 
     * 
     * @param amount the amount to be deposited into the account
     */
    public void deposit(double amount) {
        this.balance += amount;
        this.addHistory("Deposited: $" + convert2DP(amount));
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    /**
     * Gets the account's balance
     * 
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets account balance, used in funds transfer process
     * 
     * @param amount the amount to set an account's balance to
     */
    public void setBalance(double amount) {
        this.balance = amount;   
    }
    
    /**
     * Prints an account's transaction history
     */
    public void transactionHistory() {
        System.out.println(this.accountNum + " Transaction History:");

        for (int i = 0; i < this.history.size() ; i++) {
            System.out.println(this.history.get(i));
        }
    }

    /**
     * Clears an account's transaction history
     */
    public void clearHistory() {
        this.history.clear();
    }

    /**
     * Adds a single transaction to an account's history
     * 
     * @param transaction the transaction performed on the account
     */
    public void addHistory(String transaction) {
        this.history.add(transaction);
        if (history.size() > 5) {
            history.remove(0);
        }
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    /**
     * Gets the transaction history of the account
     * 
     * @return an account's transaction history in an ArrayList of type String
     */
    public ArrayList<String> getHistory() {
        return this.history;
    }

    /**
     * Converts a double value into a String with 2 decimal point precision
     * 
     * @param amount the amount to be converted
     * @return the converted value 
     */
    public static String convert2DP(double amount) {
        String amt = String.format("%.2f", amount);
        return amt;
    }

    /**
     * Prints an account's number, balance, debt and transfer limit. 
     */
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNum);
        System.out.println("Current Balance: $" + convert2DP(balance));
        // System.out.println("Debt: $" + convert2DP(debt));
        System.out.println("Transfer Limit: " + convert2DP(transLimit));
    }

    /**
     * Converts an account's attributes into a string of comma-separated-values (CSV)
     * 
     * @return the account's attributes as a CSV string
     */
    public String convertToCSV() {
        String accountData = this.getAccountNum() + "," + convert2DP(this.getBalance()) + "," + convert2DP(this.getTransLimit());
        if (loan != null) {
            accountData += "," + loan.getPrincipal() + "," + loan.getInterestRate() + "," + loan.getLoanStartDate() + "," + loan.getLoanTermMonths() + "," + loan.getLoanRepayment();
        }
        else {
            accountData += ", , , , ,";
        }

        if (this.getHistory() != null) {
            for (String i : this.getHistory()) {
                accountData += "," + i;
            }
        }

        return accountData;
    }

    /**
     * Method for applying a loan using G16_LON
     * @param principal The amount to be loaned
     * @param interestRate The interest rate of the loan
     * @param loanStartDate The start date of the loan
     * @param loanTermMonths The duration of the loan
     */
    public void applyForLoan(double principal, double interestRate, LocalDate loanStartDate, int loanTermMonths) {
        G16_LON loan = new G16_LON(principal, interestRate, loanStartDate, loanTermMonths);
        this.loan = loan;
        this.addHistory("Applied for Loan: $" + convert2DP(principal));
        CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
    }

    /**
     * Method for the user to pay off the loan
     * @param amount The amount that the user is paying
     */
    public void makeLoanPayment(double amount) {
        if (this.loan != null) {
            this.loan.payLoan(amount);
            this.addHistory("Loan Payment: $" + convert2DP(amount));
            CSVHandler.updateCSV(accountNum, "Accounts.csv", this.convertToCSV());
        } else {
            System.out.println("No active loan for this account.");
        }
    }

    /**
     * Method to display any active loan that the user has
     */
    public void displayLoanDetails() {
        if (this.loan != null) {
            this.loan.displayLoanDetails();
        } else {
            System.out.println("No active loan for this account.");
        }
    }

    /**
     * Getter for the loan id
     * @return Returns the loan id if available
     */
    public String getLoanId() {
        if (this.loan == null) {
            return null;
        }
        return this.loan.getLoanId();
    }

    /**
     * Deletes the active loan
     */
    public void deleteLoan() {
        this.loan = null;
    }
    
    /**
     * Gets the amount that still needs to be repaid
     * @return The amount that needs to be repaid
     */
    public double getLoanRepayment() {
        if (this.loan != null) {
            return this.loan.getLoanRepayment();
        }
        return 0;
    }

}

