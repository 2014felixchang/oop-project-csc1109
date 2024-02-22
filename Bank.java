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
    private List<Customer> customers;
    private static Scanner scanner = new Scanner(System.in);
    
    public Bank(String bankName){
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
        this.accountNums = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    public String getBankName(){
        return bankName;
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
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role: ");
        String role = scanner.nextLine();
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();

        password = PasswordHasher.hashPassword(password);
        Customer.registerCustomer(username, password, role, id);

        System.out.println("Registration successful!");
    }

    public static void login(Bank bank) {
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        if (Customer.loginCustomer(loginUsername, loginPassword)) {
            // Retrieve the full customer details after successful authentication
            Customer customer = CSVHandler.retrieveCustomer(loginUsername);
            if (customer != null) {
                if ("Admin".equals(customer.getRole())) {
                    adminMenu(bank, customer);
                } else {
                    accountsMenu(bank, customer);
                }
            } else {
                System.out.println("Failed to retrieve customer details.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static void adminMenu(Bank bank, Customer admin) {
        while (true) {
            System.out.println("Welcome to the admin menu, " + admin.getUsername() + "!");
            System.out.println("1. View all customers");
            System.out.println("2. Add a new customer");
            System.out.println("3. Remove a customer");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
    
            switch (choice) {
                case "1":
                    // Implement the logic to view all customers
                    viewAllCustomers();
                    break;
                case "2":
                    // Implement the logic to add a new customer
                    register(bank);
                    break;
                case "3":
                    // Implement the logic to remove a customer
                    System.out.print("Enter the username of the customer to remove: ");
                    String usernameToRemove = scanner.nextLine();
                    bank.removeCustomer(usernameToRemove);
                    break;
                case "4":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                    break;
            }
        }
    }

    public void removeCustomer(String username) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equals(username)) {
                customers.remove(i);
                System.out.println("Customer " + username + " removed successfully.");
                return;
            }
        }
        System.out.println("Customer " + username + " not found.");
    }

    public static void viewAllCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("CustomerInfo.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String username = parts[0];
                    System.out.println(username);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CustomerInfo.csv: " + e.getMessage());
        }
    }
    
    public static void accountsMenu(Bank bank, Customer customer) {
        while (true) {
            String accounts[] = customer.getCustomerAccounts();
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
                CSVHandler.addAccountToCSV(newAccount);
                // update CustomerAccounts.csv with new account added to customer account info
                String newCustomerRecord = CSVHandler.getCustAccsFromCSV(customer.getUsername()) + randomAccNum;
                CSVHandler.updateCSV(customer.getUsername(), "CustomerAccounts.csv", newCustomerRecord);
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
