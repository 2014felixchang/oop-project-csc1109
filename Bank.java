// need to add in validation check like does the account exist before transferring money or removing account etc

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Bank {
    private String bankName;
    private List<Account> accounts;
    private List<String> accountNums;
    private static Scanner scanner = new Scanner(System.in);

    public Bank(String bankName){
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
        this.accountNums = new ArrayList<>();
    }

    public static void main(String[] args) {
        Bank bank = new Bank("My Bank");
        
        while (true) {
            System.out.println("------------------------------------");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();     // Consumes the \n after the integer

            switch (choice) {
                case 1:
                    register(bank);
                    break;
                case 2:
                    login(bank);
                    break;
                case 3:
                    System.exit(0);
                default:
                    scanner.close();
                    System.out.println("Invalid choice.");
            }
        }
    }

    
    public static void register(Bank bank) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter date of birth: ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter 6-digit password: ");
        String password = scanner.nextLine();

        Customer.registerCustomer(name, address, phoneNumber, email, dateOfBirth, username, password);

        System.out.println("Registration successful!");
    }

    public static void login(Bank bank) {
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        Customer customer = new Customer("", "", "", "", "", loginUsername, loginPassword, bank); // Create an instance of the Customer class
        if (Customer.loginCustomer(loginUsername, loginPassword)) {
            // Account loggedInAccount = customer.getAccount(); // Call getAccount() on the instance
            accountsMenu(bank, customer);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static void accountsMenu(Bank bank, Customer customer) {
        while (true) {
            String accounts[] = customer.getAccounts();
            System.out.println("------------------------------------");
            System.out.println("Welcome to " + bank.getBankName() + "!");
            System.out.println("Which account would you like to view?");

            int i = 1;
            for (String acc : accounts) {
                System.out.println(i++ + ". " + acc);
            }

            System.out.println("Enter the account number, or \"yes\" if you want to make a new bank account");
            String choice = scanner.nextLine();

            if (choice.equals("yes")) {
                // call generate random acount num
                String randomAccNum = Bank.generateAccNum();
                // create new account obj, call account addRecord()
                Account newAccount = new Account(randomAccNum);
                newAccount.addRecord();
                // update CustomerAccounts.csv with new account added to customer account info
                customer.updateRecord(randomAccNum);
            }
            else{
                for (String acc : accounts) {
                    if (acc.equals(choice)) {
                        transactMenu(bank, choice);
                    }
                }
            }
        }
    }

    public static void transactMenu(Bank bank, String accNum) {
        while (true) {
            Account loggedInAccount = new Account(accNum);

            System.out.println("------------------------------------");
            System.out.println("Account number " + loggedInAccount.getAccountNum());
            System.out.println("1. Check balance");
            System.out.println("2. Transfer Money");
            // need to add changing of transfer limit functionality
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Currency Exchange");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();     // Consumes the \n after the integer
            System.out.println("------------------------------------");

            switch (choice) {
                case 1:
                    // Check balance
                    System.out.println("Your balance is: $" + loggedInAccount.getBalance());
                    break;
                case 2:
                    // Transfer money
                    System.out.print("Enter the account number to transfer money to: ");
                    String transferAccountNum = scanner.next();
                    System.out.print("Enter the amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine();     // Consumes the \n after the double
                    bank.transferMoney(loggedInAccount.getAccountNum(), transferAccountNum, transferAmount);
                    break;
                case 3:
                    // Deposit
                    System.out.print("Enter the amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();     // Consumes the \n after the double
                    loggedInAccount.deposit(depositAmount);
                    break;
                case 4:
                    // Withdraw
                    System.out.print("Enter the amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();     // Consumes the \n after the double
                    loggedInAccount.withdraw(withdrawAmount);
                    break;
                case 5:
                    // Foreign Exchange (To do after account people add multiple currency)
                    System.out.println("Enter the amount to exchange:");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();     // Consumes the \n after the double
                    System.out.println("Enter the currency to convert from (SGD/USD):");
                    String fromCurrency = scanner.next();
                    System.out.println("Enter the currency to convert to (SGD/USD):");
                    String toCurrency = scanner.next();

                    ForeignExchange foreignExchange = new ForeignExchange();
                    double convertedAmount = foreignExchange.convert(fromCurrency, toCurrency, amount);
                    break;
                case 6:
                    // Logout
                    System.out.println("You have been logged out.");
                    main(null);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
    public void transferMoney(String sourceAccountNum, String destinationAccountNum, double amount){
        if ((Account.retrieveRecord(sourceAccountNum)) == null || Account.retrieveRecord(destinationAccountNum) == null ) {
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

        sourceAccount.updateRecord();
        destinationAccount.updateRecord();

        System.out.println("\nTransfer Successful!");
    }

    // public Account getAccountObj(String accountNum){
    //     // need to check if account exists
    //     int accountIndex = accountNums.indexOf(accountNum);
    //     Account tempAccount = accounts.get(accountIndex);
    //     return tempAccount;
    // }

    public List<String> getAccountNumList(){
        System.out.println(accountNums);
        return accountNums;
    }

    public void addAccount(Account account){
        accounts.add(account);
        accountNums.add(account.getAccountNum());
    }

    public void removeAccount(String accountNum){
        // need to check if account exists for it to be removed
        int accountIndex = accountNums.indexOf(accountNum);
        accounts.remove(accountIndex);
    }

    public void displayAccountInfo(String accountNum){
        if ((Account.retrieveRecord(accountNum)) == null) {
            Account tempAccount = new Account(accountNum);
            tempAccount.displayAccountInfo();
        }
    }

    public void setAccountTransferLimit(String accountNum, double newLimit){
        // Take in from CLI when customer enter the input and pass it onto their account's setting
        Account tempAccount = new Account(accountNum);
        tempAccount.setTransferLimit(newLimit);
    }

    public String getBankName(){
        return bankName;
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
