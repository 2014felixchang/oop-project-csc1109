
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;



public class BankUI {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Insurance> insurancePolicies = new HashMap<>();

    public static void displayMainMenu(Bank bank) {
        while (true) {
            System.out.println("------------------------------------");
            System.out.println("Welcome to " + bank.getBankName() + "! Please give us your money!");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("------------------------------------");
            System.out.print("Enter your choice: ");

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
                    BankUI.printInvalid();
                    break;
            }
        }
    }

    public static void register(Bank bank) {
        while (true) {
            System.out.println("------------------------------------");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            // validate username entered, check if username already exists
            String customerString = CSVHandler.getRecord(username, "CustomerInfo.csv");
            if (customerString != null) {
                System.out.println("Username already exists. Please try again.");
                continue;
            }

            System.out.print("Enter 6 digit PIN: ");
            String password = scanner.nextLine();

            // validate PIN entered, first check if is numerical, then check if length is 6 digits
            int pin;
            try {
                pin = Integer.parseInt(password);
            } catch (NumberFormatException e) {
                System.out.println("Invalid password entered. Please try again.");
                continue;
            }
            if (password.length() != 6) {
                System.out.println("Invalid password entered. Please try again.");
                continue;
            }
            else {
                password = PasswordHasher.hashPassword(String.valueOf(pin));
            }

            System.out.print("Enter role: ");
            String role = scanner.nextLine();
            System.out.print("Enter ID: ");
            String id = scanner.nextLine();
        
            Customer customer = Customer.registerCustomer(username, password, role, id);
            System.out.println("Registration successful!");
            accountsMenu(bank, customer);
        }
    }

    public static void addNewCustomer() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role: ");
        String role = scanner.nextLine();
        System.out.print("Enter id: ");
        String id = scanner.nextLine();
        password = PasswordHasher.hashPassword(password);
        Customer.registerCustomer(username, password, role, id);
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
        CSVHandler.addCustomerDetailsToCSV(username, name, address, phoneNumber, email, dateOfBirth);
        System.out.println("Customer added successfully.");
    }

    public static void login(Bank bank) {
        System.out.println("------------------------------------");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        Customer customer = CSVHandler.retrieveCustomer(loginUsername);
        if (customer != null && customer.isLocked()) {
            System.out.println("The account is locked and cannot be accessed.");
            return;
        }

        if (Customer.loginCustomer(loginUsername, loginPassword)) {
            // Retrieve the full customer details after successful authentication
            if ("Admin".equals(customer.getRole())) {
                adminMenu(bank, customer);
            } else {
                accountsMenu(bank, customer);
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static void adminMenu(Bank bank, Customer admin) {
        while (true) {
            System.out.println("------------------------------------");
            System.out.println("Welcome to the admin menu, " + admin.getUsername() + "!");
            System.out.println("1. View all customers");
            System.out.println("2. Add a new customer");
            System.out.println("3. Remove a customer");
            System.out.println("4. Unlock a customer account");
            System.out.println("5. Logout");
            System.out.println("------------------------------------");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Implement the logic to view all customers
                    Customer.viewAllCustomers();
                    break;
                case "2":
                    addNewCustomer();
                    break;
                case "3":
                    // Implement the logic to remove a customer
                    System.out.print("Enter the username of the customer to remove: ");
                    String usernameToRemove = scanner.nextLine();

                    String accountsToRemove = CSVHandler.getRecord(usernameToRemove, "CustomerAccounts.csv");
                    if (accountsToRemove == null) {
                        System.out.println("Username does not exist.");
                    }
                    else {
                        String[] accountsToRemoveArray = accountsToRemove.split(",");
                        // start from the second column, index 1, since first col is username
                        for (int i = 1; i < accountsToRemoveArray.length; i++) {
                            CSVHandler.removeRecord(accountsToRemoveArray[i], "Accounts.csv");
                        }
                        CSVHandler.removeRecord(usernameToRemove, "CustomerInfo.csv");
                        CSVHandler.removeRecord(usernameToRemove, "CustomerAccounts.csv");
                        System.out.println("Customer removed successfully."); 
                    }
                    break;
                case "4":
                    // unlock customer account given the customer's username
                    System.out.print("Enter the username of the customer to unlock: ");
                    String usernameToUnlock = scanner.nextLine();
                    Customer customerToUnlock = CSVHandler.retrieveCustomer(usernameToUnlock);
                    if (customerToUnlock != null) {
                        customerToUnlock.setLocked(false);
                        // CSVHandler.updateCustomerLockStatus(usernameToUnlock, "0");
                        CSVHandler.updateCSV(usernameToUnlock, "CustomerInfo.csv", customerToUnlock.customerInfoToCSV());
                        System.out.println("Account unlocked successfully.");
                    } 
                    else {
                        System.out.println("Customer not found.");
                    }
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                    break;
            }
        }
    }

    public static void accountsMenu(Bank bank, Customer customer) {
        if (customer.isLocked()) {
            System.out.println("The account is locked and cannot be accessed.");
            return;
        }
        
        while (true) {
            String custAccountsRecord = CSVHandler.getRecord(customer.getUsername(), "CustomerAccounts.csv");
            String[] accounts = custAccountsRecord.split(",");
            String choice = "";

            System.out.println("------------------------------------");
            System.out.println("Welcome " + customer.getUsername() + "!");
            System.out.println("Which account would you like to view?");
            // accounts[0] contains username, so to iterate through account numbers, start index at 1
            int i = 1;
            for (; i < accounts.length; i++) {
                System.out.println(i + ". " + accounts[i]);
            }
            System.out.println(i + ". Make new bank account");
            System.err.println((i+1) + ". Logout");
            System.out.println("------------------------------------");
            System.out.print("Enter your choice: ");
            
            try {
                choice = scanner.nextLine();
                if (Integer.parseInt(choice) == (i+1)) { // i + 1 corresponds to logout option
                    return;
                }
                else if (Integer.parseInt(choice) == i) { // i corresponds to make new bank account option
                    // call generate random acount num
                    String randomAccNum = Bank.generateAccNum();
                    // create new account obj, call account addRecord()
                    Account newAccount = new Account(randomAccNum);
                    CSVHandler.addRecord("Accounts.csv", newAccount.convertToCSV());
                    // update CustomerAccounts.csv with new account added to customer account info
                    String newCustomerRecord = custAccountsRecord + "," + randomAccNum;
                    CSVHandler.updateCSV(customer.getUsername(), "CustomerAccounts.csv", newCustomerRecord);
                }
                else {
                    // go through all accounts and see if user choice entered corresponds with any existing accounts
                    for (int j = 1; j < accounts.length; j++) {
                        if (Integer.parseInt(choice) == j) {
                            transactMenu(bank, customer, accounts[j]);
                            continue;
                        }
                    }
                    // if the option number the user entered does not correspond to an existing account number, then print below
                    BankUI.printInvalid();
                }
            } catch (InputMismatchException e) {
                BankUI.printInvalid();
                continue;
            } catch (NumberFormatException e) {
                BankUI.printInvalid();
                continue;
            }
        }
    }

    public static void transactMenu(Bank bank, Customer customer, String accNum) {
        while (true) {
            Account loggedInAccount = new Account(accNum);
            int choice = 0;

            System.out.println("------------------------------------");
            System.out.println("Account number: " + loggedInAccount.getAccountNum());
            System.out.println("Balance: " + Account.convert2DP(loggedInAccount.getBalance()) + ",  Debt: " + Account.convert2DP(loggedInAccount.getDebt()));
            System.out.println("1. Transfer Funds");
            System.out.println("2. Change transfer limit");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Currency Exchange");
            System.out.println("6. Pay debt");
            System.out.println("7. Create Insurance Policy");
            System.out.println("8. Go back to accounts menu");
            System.out.println("9. Logout");
            System.out.println("------------------------------------");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();     // Consumes the \n after the integer
            } catch (InputMismatchException e) {
                BankUI.printInvalid();
                continue;
            }

            switch (choice) {
                case 1:
                    // Transfer money
                    System.out.print("Enter the account number to transfer money to: ");
                    String transferAccountNum = scanner.next();
                    System.out.print("Enter the amount to transfer: $");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine();     // Consumes the \n after the double
                    bank.transferMoney(loggedInAccount.getAccountNum(), transferAccountNum, transferAmount);
                    break;
                case 2:
                    while (true) {
                        System.out.println("------------------------------------");
                        System.out.println("Current transfer limit: $" + Account.convert2DP(loggedInAccount.getTransLimit()));
                        System.out.print("Enter new transfer limit: $");
                        try {
                            String newTransLimitStr = scanner.nextLine();
                            double newTransLimit = Double.parseDouble(newTransLimitStr);
                            loggedInAccount.setTransferLimit(newTransLimit);
                            CSVHandler.updateCSV(loggedInAccount.getAccountNum(), "Accounts.csv", loggedInAccount.convertToCSV());
                        } catch (InputMismatchException e) {
                            BankUI.printInvalid();
                            continue;
                        } catch (NumberFormatException e) {
                            BankUI.printInvalid();
                            continue;
                        }
                        break;
                    }
                    System.out.println("Transfer limit changed successfully!");
                    break;
                case 3:
                    // Deposit
                    System.out.print("Enter the amount to deposit: $");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();     // Consumes the \n after the double
                    loggedInAccount.deposit(depositAmount);
                    break;
                case 4:
                    // Withdraw
                    System.out.print("Enter the amount to withdraw: $");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();     // Consumes the \n after the double
                    loggedInAccount.withdraw(withdrawAmount);
                    break;
                case 5:
                    // Foreign Exchange
                    ForeignExchange foreignExchange = new ForeignExchange();
                    foreignExchange.displayRates();
                    System.out.println("Enter the currency to convert from (SGD/USD/JPY):");
                    String fromCurrency = scanner.next();
                    double exchangeAmount;
                    if (fromCurrency == "SGD"){
                        System.out.println("Enter the amount to exchange: ");
                        exchangeAmount = scanner.nextDouble();
                        scanner.nextLine();     // Consumes the \n after the double
                        if(loggedInAccount.getBalance() < exchangeAmount){
                            System.out.println("Not enough money in balance!");
                            break;
                        }
                        loggedInAccount.setBalance(loggedInAccount.getBalance() - exchangeAmount);
                    }else{
                        System.out.println("Please insert foreign cash into the machine");
                        System.out.println("Amount: ");
                        exchangeAmount = scanner.nextDouble();
                        scanner.nextLine();     // Consumes the \n after the double
                    }
                    System.out.println("Enter the currency to convert to (SGD/USD/JPY):");
                    String toCurrency = scanner.next();

                    double convertedAmount = foreignExchange.convert(fromCurrency, toCurrency, exchangeAmount);
                    if(toCurrency == "SGD"){
                        System.out.println("Converted amount: " + convertedAmount + " SGD");
                        System.out.println("Adding to balance...");
                        loggedInAccount.setBalance(loggedInAccount.getBalance() + convertedAmount);
                    }else{
                        System.out.println("Converted amount: " + convertedAmount + " " + toCurrency);
                        System.out.println("Dispensing amount...");
                    }
                    break;
                case 6:
                    // paying off debt
                    if (loggedInAccount.getDebt() == 0) {
                        System.out.println("This account has no debt!");
                        break;
                    }
                    else {
                        System.out.println("Enter amount of debt you are paying off: $");
                        double debtAmount = scanner.nextDouble();
                        loggedInAccount.minusDebt(debtAmount);
                        break;
                    }
                case 7:
                    BankUI.createNewInsurancePolicy();
                    break;
                case 8:
                    // go back to accounts menu
                    return;
                case 9:
                    // Logout
                    System.out.println("You have been logged out.");
                    Bank.main(null);
                default:
                    BankUI.printInvalid();
                    break;
            }
        }
    }
    //create policy
    public static void createNewInsurancePolicy() {

        System.out.println("Enter policy type (1 for LIFE, 2 for HEALTH, 3 for ACCIDENT): ");
        int policyTypeIndex = scanner.nextInt();
        System.out.println("Enter coverage option (1 for BASIC($1000), 2 for STANDARD($2000), 3 for PREMIUM($3000)): ");
        int coverageOptionIndex = scanner.nextInt();
        System.out.println("Enter policy tenure (1 for FIVE_YEARS, 2 for TEN_YEARS, 3 for FIFTEEN_YEARS, 4 for TWENTY_YEARS): ");
        int policyTenureIndex = scanner.nextInt();
        System.out.println("Enter premium frequency (1 for MONTHLY, 2 for QUARTERLY, 3 for SEMI_ANNUALLY, 4 for ANNUALLY): ");
        int premiumFrequencyIndex = scanner.nextInt();
        System.out.println("Enter policy start date (yyyy-MM-dd): ");
        String startDateString = scanner.next();
        System.out.println("Create policy successfully!");
        System.out.println("------------------------------------");

        Insurance insurance = new Insurance(policyTypeIndex, startDateString, coverageOptionIndex, policyTenureIndex, premiumFrequencyIndex);
        // Display the policy details
        insurance.displayPolicyDetails();
        
    }

    public static void printInvalid() {
        System.out.println("------------------------------------");
        System.out.println("Invalid input. Please try again.");
    }
   
    public static int getUserChoice() {
        try {
            String choice = scanner.nextLine();
            // scanner.nextLine();     // Consumes the \n after the integer
            return Integer.parseInt(choice);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    // ... more methods for other menus and user input
}