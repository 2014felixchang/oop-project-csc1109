// need to add in validation check like does the account exist before transferring money or removing account etc

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Bank {
    private String bankName;
    private List<Customer> customers;
    
    public Bank(String bankName){
        this.bankName = bankName;
        this.customers = new ArrayList<>();
    }

    public String getBankName(){
        return bankName;
    }

    public static void main(String[] args) {
        Bank bank = new Bank("My Bank");
        
        while (true) {
            BankUI.displayMainMenu();
            int choice = BankUI.getUserChoice();

            switch (choice) {
                case 1:
                    BankUI.register(bank);
                    break;
                case 2:
                    BankUI.login(bank);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    
    public void transferMoney(String sourceAccountNum, String destinationAccountNum, double amount){
        if ((CSVHandler.getAccountFromCSV(sourceAccountNum)) == null || CSVHandler.getAccountFromCSV(destinationAccountNum) == null ) {
            System.out.println("Invalid account numbers given. Transfer process terminated.");
            return;
        }
        
        Account sourceAccount = new Account(sourceAccountNum);
        Account destinationAccount = new Account(destinationAccountNum);

        if (sourceAccount.getBalance() < amount) {
            System.out.println("Insufficient balance to transfer funds.");
            return;
        }

        if (sourceAccount.getTransLimit() < amount) {
            System.out.println("Transfer amount exceeds account transfer limit. Please increase transfer limit before trying again.");
            return;
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        sourceAccount.addHistory("Transfered $" + amount + " to Account: " + destinationAccount.getAccountNum());
        destinationAccount.addHistory ("Received $" + amount + " from Account: " + sourceAccount.getAccountNum());

        CSVHandler.updateCSV(sourceAccount.getAccountNum(), "Accounts.csv", sourceAccount.convertToCSV());
        CSVHandler.updateCSV(destinationAccount.getAccountNum(), "Accounts.csv", destinationAccount.convertToCSV());

        System.out.println("\nTransfer Successful!");
    }
    
    public static String generateAccNum() {
        Random rand = new Random();
        String randomAccNum = String.valueOf(rand.nextInt(9999999));
        while (Bank.checkAccNumExists(randomAccNum) == true) {
            randomAccNum = String.valueOf(rand.nextInt(9999999));
        }
        return randomAccNum;
    }

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
}
