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
        
        String record;

        if ((record = Account.retrieveRecord(accNum)) != null) {
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
        this.updateRecord();
    }

    /*
     * Input: deposit amount type double
     * 
     * Process: adds amount to balance
     */
    public void deposit(double amount) {
        this.balance += amount;
        this.addHistory("Deposited: $" + convert2DP(amount));
        this.updateRecord();
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
    public void setDebt(double amount) {
        if (amount > this.debt) {
            this.debt = 0.0;
            this.balance += amount - debt;
        }
        else {
            this.debt -= amount;
        }
        this.updateRecord();
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
        updateRecord();
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

    /*
     * Process: update existing account record in csv
     */
    public void updateRecord() {
        String currentLine;
        String accountInfo = this.convertToCSV();
        
        try (
            BufferedReader bR = new BufferedReader(new FileReader("Accounts.csv")); 
            BufferedWriter bW = new BufferedWriter(new FileWriter("temp.csv", false))
            ) 
        {
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(accountNum) == false) {
                    // if current line is NOT the account to update, write line to temp file
                    bW.write(currentLine, 0, currentLine.length());
                    bW.newLine();
                }
                else {
                    // else write new account info to temp file
                    bW.write(accountInfo, 0, accountInfo.length());
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
     * Process: append new account record to csv for new accounts
     */
    public void addRecord() {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter("Accounts.csv", true))){
            bW.write(this.convertToCSV());
            bW.newLine();
        }   
        catch (IOException e) {
            System.out.println(e);
        }
    }

    /*
     * Process: retrieve account record, if it exists in file, else return null
     */
    public static String retrieveRecord(String accountNum) {
        try (BufferedReader bR = new BufferedReader(new FileReader("Accounts.csv"))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(accountNum) == true) {
                    return currentLine;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public static void main(String[] args) {
        Account acc1 = new Account("1234567");
        // acc1.transferFunds(200, acc2);
        // acc1.deposit(100);
        acc1.withdraw(10);
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