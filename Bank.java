// need to add in validation check like does the account exist before transferring money or removing account etc

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Description: The Bank class represents a bank that manages the Customers and Accounts
 * and handles financial operations. Houses the main method to run for demo.
 */
public class Bank {
    private String bankName;
    private List<Customer> customers;
    
    /**
     * Constructor for the Bank class
     * @param bankName The name of the bank
     */
    public Bank(String bankName){
        this.bankName = bankName;
        this.customers = new ArrayList<>();
    }

    /**
     * Get the bank name
     * 
     * @return Bank name
     */
    public String getBankName(){
        return bankName;
    }

    /**
     * The main method of the whole application. 
     * Initiates the Bank and starts the application UI
     * 
     * @param args
     */
    public static void main(String[] args) {
        Bank bank = new Bank("My Bank");
        
        BankUI.displayMainMenu(bank);

    }

    /**
     * Transfers money from one account to another
     * 
     * @param sourceAccountNum The source account number
     * @param destinationAccountNum The destination account number
     * @param amount The amount to be transferred
     */
    public Account transferMoney(Account sourceAccountObj, String destinationAccountNum, double amount){
        if ((CSVHandler.getRecord(sourceAccountObj.getAccountNum(), "Accounts.csv")) == null || (CSVHandler.getRecord(sourceAccountObj.getAccountNum(), "Accounts.csv")) == null ) {
            System.out.println("Invalid account numbers given. Transfer process terminated.");
            return sourceAccountObj;
        }
        
        Account sourceAccount = sourceAccountObj;
        Account destinationAccount = new Account(destinationAccountNum);

        if (sourceAccount.getBalance() < amount) {
            System.out.println("Insufficient balance to transfer funds.");
            return sourceAccountObj;
        }

        if (sourceAccount.getTransLimit() < amount) {
            System.out.println("Transfer amount exceeds account transfer limit. Please increase transfer limit before trying again.");
            return sourceAccountObj;
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        sourceAccount.addHistory("Transfered $" + amount + " to Account: " + destinationAccount.getAccountNum());
        destinationAccount.addHistory ("Received $" + amount + " from Account: " + sourceAccount.getAccountNum());

        CSVHandler.updateCSV(sourceAccount.getAccountNum(), "Accounts.csv", sourceAccount.convertToCSV());
        CSVHandler.updateCSV(destinationAccount.getAccountNum(), "Accounts.csv", destinationAccount.convertToCSV());

        System.out.println("\nTransfer Successful!");
        return sourceAccountObj;
    }
    
    /**
     * Generates an account number for new accounts
     * 
     * @return The generated account number
     */
    public static String generateAccNum() {
        Random rand = new Random();
        String randomAccNum = String.valueOf(rand.nextInt(9999999));
        while (Bank.checkAccNumExists(randomAccNum) == true) {
            randomAccNum = String.valueOf(rand.nextInt(9999999));
        }
        return randomAccNum;
    }

    /**
     * Checks if the account number already exists
     * 
     * @param accNum The account number that is to be checked
     * @return True or False whether the account number already exists
     */
    public static boolean checkAccNumExists(String accNum) {
        try (BufferedReader bR = new BufferedReader(new FileReader("CustomerAccounts.csv"))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                if (currentLine.contains(accNum) == true) {
                    return true;
                }
            }
            return false;
        }
        catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Issues a loan to the account user using the external Loan class
     * 
     * @param account The account which the loan will be attached to
     * @param principal The principal amount of the loan
     * @param interestRate The interest rate of the loan
     * @param startDate The start date of the loan
     * @param termMonths The amount of months where the loan will be active
     */
    public void issueLoan(Account account, double principal, double interestRate, LocalDate startDate, int termMonths) {
        if (account.getLoanId() != null) {
            System.out.println("This account already has an active loan.");
            return;
        }

        try {
            account.applyForLoan(principal, interestRate, startDate, termMonths);
            account.displayLoanDetails();
        } catch (Exception e) {
            System.out.println("Failed to issue loan: " + e.getMessage());
    }
}
}
