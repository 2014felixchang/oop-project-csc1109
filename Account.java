/*
 * Account class:
 * - account info handling (display & manage acc info like acc numbers and types)
 * - balance management (check balance, show current, available)
 * - account operations (deposit, withdraw)
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.ArrayList;

public class Account {
    private String accountNum;
    private double balance;
    private double debt;
    private ArrayList<String> history;

    /*
     * Input: account number
     * 
     * Process: initialize account object with balance, debt & history (pulled from file)
     */
    public Account(String accNum) {
        this.accountNum = accNum;

        String record;
        if ((record = this.retrieveRecord(accNum)) == null) {
            this.balance = 0;
            this.debt = 0;
            this.history = new ArrayList<String>();
        }
        else {
            String[] accountData = record.split(",");
            this.balance = Double.parseDouble(accountData[1]);
            this.debt = Double.parseDouble(accountData[2]);
            try {
                for (int i = 3; i < accountData.length; i++) {
                    this.history.add(accountData[i]);
                }
            }
            catch (NullPointerException e) {
                this.history = new ArrayList<String>();
            }
        }
    }

    /*
     * old constructor
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
            this.addHistory("Withdrawn: $" + convert2DP(amount));
        }
    }

    /*
     * Input: deposit amount type double
     * 
     * Process: adds amount to balance
     */
    public void deposit(double amount) {
        this.balance += amount;
        this.addHistory("Deposited: $" + convert2DP(amount));
    }

    /*
     * Output: returns balance as String in 2 d.p, rounded up
     */
    public double getBalance() {
        return balance;
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
    public String getDebt() {
        return convert2DP(this.debt);
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
    }

    /*
     * Process: convert account attributes to a formatted string of comma-separated-values (CSV)
     * 
     * output: formatted string of account data
     */
    public String convertToCSV() {
        String accountData = this.getAccountNum() + "," + convert2DP(this.getBalance()) + "," + this.getDebt();
        try {
            for (String i : this.getHistory()) {
                accountData += "," + i;
            }
        }
        catch (NullPointerException e) {
            System.out.println("No account history");
        }
        return accountData;
    }

    /*
     * Process: update existing account record in csv
     */
    public void updateRecord() {
        String currentLine;
        String accountData = this.convertToCSV();
        
        try (BufferedReader bR = new BufferedReader(new FileReader("Accounts.csv")); 
        BufferedWriter bW = new BufferedWriter(new FileWriter("temp.csv", false))) 
        {
            while ((currentLine = bR.readLine()) != null) {
                if (currentLine.contains(this.accountNum) == false) {
                    // if current line not contain this account num, write line to temp file
                    bW.write(currentLine, 0, currentLine.length());
                    bW.newLine();
                }
                else {
                    // else write new account info to temp file
                    bW.write(accountData, 0, accountData.length());
                    bW.newLine();
                }
            }
        } 
        catch (IOException e) {
            System.err.println(e);
        }

        try {
            Path accPath = Paths.get("Accounts.csv");
            Path tempPath = Paths.get("temp.csv");
            // delete old file and rename temp file to accounts.csvs
            Files.delete(accPath);
            Files.move(tempPath, accPath);
            // File dump = new File("Accounts.csv");
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    /*
     * Process: add new account record to csv for new accounts
     */
    public void addRecord() {
        File currentAccounts = new File("Accounts.csv");
        try {
            BufferedWriter bW = new BufferedWriter(new FileWriter(currentAccounts, true));
            bW.newLine();
            bW.write(this.convertToCSV());
            bW.flush();
            bW.close();
        }   
        catch (IOException e) {
            System.out.println(e);
        }
    }

    /*
     * retrieve account record, if it exists in file, else return null
     */
    public String retrieveRecord(String accountNum) {
        String currentLine;
        try {
            BufferedReader bR = new BufferedReader(new FileReader("Accounts.csv"));

            while ((currentLine = bR.readLine()) != null) {
                if (currentLine.contains(accountNum) == true) {
                    return currentLine;
                }
            }

            bR.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public static void main(String[] args) {
        Account acc1 = new Account("accountno123");
        // acc1.transferFunds(200, acc2);
        acc1.deposit(100);
        System.out.println(acc1.getBalance());  // Should print 800.00
        acc1.displayAccountInfo();
        acc1.updateRecord();
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
    
}   