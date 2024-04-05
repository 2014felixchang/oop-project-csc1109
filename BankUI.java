import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * The BankUI class serves as a helper class for the Bank class. 
 * It handles all the UI elements of the Bank application
 */
public class BankUI {
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Prints the Main Menu in the terminal
     * @param bank The active bank instance
     */
    public static void printMainMenu(Bank bank) {
        System.out.println("------------------------------------");
        System.out.println("Welcome to " + bank.getBankName() + "! Please give us your money!");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }

    /**
     * Handles the user's choice in the Main Menu
     * @param bank The active bank instance
     */
    public static void displayMainMenu(Bank bank) {
        while (true) {
            printMainMenu(bank);
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
                    break;
                default:
                    BankUI.printInvalid();
                    break;
            }
        }
    }
    /**
     * Prints out to the user to enter the information needed for creating a credit card
     * It will then take the information and create the Credit Card using the external component.
     */
    public static void createCreditCard() {
        try {
            System.out.println("Enter card number:");
            Long cardNumber = scanner.nextLong();
            scanner.nextLine(); // Consume newline
    
            System.out.println("Enter last 4 digits of card number:");
            String cardNumLast4Digit = scanner.nextLine();
    
            System.out.println("Enter card expiry date (format: yyyy-mm-dd):");
            String cardExpiryDateString = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate cardExpiryDate = LocalDate.parse(cardExpiryDateString, formatter);
    
            System.out.println("Enter annual fee:");
            double annualFee = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
    
            System.out.println("Enter interest rate:");
            double interestRate = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
    
            System.out.println("Enter benefits:");
            int benefits = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            System.out.println("Enter own limit:");
            double ownLimit = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
    
            System.out.println("Enter credit limit:");
            double creditLimit = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
    
            System.out.println("Enter credit bill:");
            double creditBill = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
    
            System.out.println("Enter account number:");
            int accountNum = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            // Create a CreditCard instance
            G21_CRD cc;
            if (benefits >= 3000) {
                cc = new G21_CRDTravel(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, ownLimit, creditLimit, creditBill, accountNum);
                cc.setCardType("Travel Credit Card");
            } else if (benefits >= 500) {
                cc = new G21_CRDRewards(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, ownLimit, creditLimit, creditBill, accountNum);
                cc.setCardType("Rewards Credit Card");
            } else if (benefits >= 200) {
                cc = new G21_CRDStudent(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, ownLimit, creditLimit, creditBill, accountNum);
                cc.setCardType("Student Credit Card");
            } else {
                cc = new G21_CRD(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "", ownLimit, creditLimit, creditBill, accountNum);
                cc.setCardType("Standard");
            }
            // Call the displayCreditDetails method based on user input and display 
            cc.displayCreditCardDetails();
    
            // Call the extended method based on the card type
            if (cc instanceof G21_CRDRewards) {
                G21_CRDRewards ccRewards = (G21_CRDRewards) cc;
                System.out.println("Do you want to claim rewards? (yes/no):");
                String claimRewardsOption = scanner.next();
                scanner.nextLine(); // Consume newline
                if (claimRewardsOption.equalsIgnoreCase("yes")) {
                    System.out.println("Enter the item number you want to claim:");
                    System.out.println("1. $30 Grab Voucher, 300points");
                    System.out.println("2. $50 Shopee Voucher, 500points");
                    System.out.println("3. Apple Airpods Pro (2nd Generation) (USB-C), 3630points");
                    System.out.println("4. Apple 10.2-inch iPad Wi-Fi 64GB Space Grey, 5040points");
                    int item = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    ccRewards.claimRewards(String.valueOf(item));
                }
            }
    
            //Call the extended method based on the card type
            if (cc instanceof G21_CRDStudent) {
                G21_CRDStudent ccStudent = (G21_CRDStudent) cc;
                System.out.println("Do you want to pay bill with cashback? (yes/no):");
                String payBillWithCashbackOption = scanner.next();
                scanner.nextLine(); // Consume newline
                if (payBillWithCashbackOption.equalsIgnoreCase("yes")) {
                    System.out.println("Enter the bill amount:");
                    double billAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    System.out.println("Enter the cashback amount:");
                    int cashbackAmount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    ccStudent.payBill(billAmount, cashbackAmount);
                }
            }
    
            // Example of using the extended class G21_CRDTravel
            if (cc instanceof G21_CRDTravel) {
                G21_CRDTravel ccTravel = (G21_CRDTravel) cc;
                System.out.println("Do you want to claim travel benefits? (yes/no):");
                String claimTravelBenefitsOption = scanner.next();
                scanner.nextLine(); // Consume newline
                if (claimTravelBenefitsOption.equalsIgnoreCase("yes")) {
                    System.out.println("Enter the benefit number you want to claim:");
                    System.out.println("1. Free airplane merchandise, 50points");
                    System.out.println("2. $200 Dining Voucher, 600points");
                    System.out.println("3. One Way trip within Asia, 10000points");
                    String item = scanner.nextLine();
                    ccTravel.claimBenefits(item);
                }
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }

    /**
     * Handles the registration process of a new user.
     * Prints the prompts for the user and creates a new user using the information provided.
     * @param bank The active Bank instance
     */
    public static void register(Bank bank) {
        try {
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

                System.out.print("Enter ID: ");
                String id = scanner.nextLine();

                Customer.registerCustomer(username, password, "User", id);
                System.out.println("Registration successful!");
                break;
            }
        } catch (Exception e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
        }
    }

    /**
     * Handles the login process of the Bank application.
     * Prints the prompts for the user and passes the information onto the Customer class.
     * Advances the menu upon a successful login.
     * 
     * @param bank The active Bank instance
     */
    public static void login(Bank bank) {
        try {
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
        } catch (Exception e) {
            System.out.println("An error occurred during login: " + e.getMessage());
        }
    }

    /**
     * Prints the admin menu
     * @param admin The active admin account
     */
    public static void printAdminMenu(Customer admin) {
        System.out.println("------------------------------------");
        System.out.println("Welcome to the admin menu, " + admin.getUsername() + "!");
        System.out.println("1. View all customers");
        System.out.println("2. Add a new customer");
        System.out.println("3. Remove a customer");
        System.out.println("4. Unlock a customer account");
        System.out.println("5. Logout");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }

    /**
     * Handles the admin's choice in the admin menu
     * @param bank The active Bank instance
     * @param admin The active admin account
     */
    public static void adminMenu(Bank bank, Customer admin) {
        while (true) {
            printAdminMenu(admin);
            int choice = BankUI.getUserChoice();

            switch (choice) {
                case 1:
                    // Implement the logic to view all customers
                    Customer.viewAllCustomers();
                    break;
                case 2:
                    Customer.addNewCustomer();
                    break;
                case 3:
                    // Implement the logic to remove a customer
                    Customer.removeCustomer();
                    break;
                case 4:
                    // unlock customer account given the customer's username
                    Customer.unlockCustomerAccount();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                    break;
            }
        }
    }

    /**
     * Handles the accounts menu where users choose their account to do their business and their choices
     * @param bank The active Bank instance
     * @param customer The active Customer
     */
    public static void accountsMenu(Bank bank, Customer customer) {
        if (customer.isLocked()) {
            System.out.println("The account is locked and cannot be accessed.");
            return;
        }
        
        while (true) {
            String custAccountsRecord = CSVHandler.getRecord(customer.getUsername(), "CustomerAccounts.csv");
            String[] accounts = custAccountsRecord.split(",");

            System.out.println("------------------------------------");
            System.out.println("Welcome " + customer.getUsername() + "!");
            System.out.println("Which account would you like to view?");
            int key = 1;
            for (; key < accounts.length; key++) {
                System.out.println(key + ". " + accounts[key]);
            }
            
            int i = key;
            System.out.println(i + ". Make new bank account");
            System.err.println((i+1) + ". Delete bank account");
            System.err.println((i+2) + ". Logout");
            System.out.println("------------------------------------");
            System.out.print("Enter your choice: ");

            try {
                String choice = scanner.nextLine();
                
                if (Integer.parseInt(choice) == (i+2)) { // i + 2 corresponds to log out option
                    return;
                }
                else if (Integer.parseInt(choice) == (i+1)) { // i + 1 corresponds to delete account option
                    System.out.println("------------------------------------");
                    System.out.print("Enter account number to delete: ");
                    try {
                        int deleteAccNum = Integer.parseInt(scanner.nextLine());
                        String delete = String.valueOf(deleteAccNum);

                        boolean validInput = false;
                        // validate acc num input
                        for (int k = 1; k < accounts.length; k++) {
                            if (accounts[k].equals(delete)) {
                                validInput = true;
                            }
                        }

                        if (validInput == true) {
                            // remove account record from accounts csv
                            CSVHandler.removeRecord(delete, "Accounts.csv");

                            // remove account number from customer's accounts csv
                            custAccountsRecord = accounts[0]; // write username
                            for (int k = 1; k < accounts.length; k++) {
                                // if account number is not the account to be deleted, append to record
                                if (!accounts[k].equals(delete)) {
                                    custAccountsRecord = custAccountsRecord + "," + accounts[k]; // append account numbers
                                }
                            }
                            CSVHandler.updateCSV(customer.getUsername(), "CustomerAccounts.csv", custAccountsRecord);
                            System.out.println("Account deleted successfully.");
                        }
                        else {
                            BankUI.printInvalid();
                        }
                    } 
                    catch (InputMismatchException | NumberFormatException e) {
                        BankUI.printInvalid();
                    }
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
                    System.out.println("New bank account created successfully.");
                }
                else {
                    // go to transact menu for selected account
                    for (int j = 1; j < accounts.length; j++) {
                        if (Integer.parseInt(choice) == j) {
                            transactMenu(bank, customer, accounts[j]);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println(e);
                continue;
            }
        }
    }

    /**
     * Prints the regular user menu
     * @param loggedInAccount
     */
    public static void displayAccountMenu(Account loggedInAccount) {
        System.out.println("------------------------------------");
        System.out.println("Account number: " + loggedInAccount.getAccountNum());
        System.out.println("Transaction history: " );
        System.out.println(loggedInAccount.getHistory());
        System.out.println("Transfer limit: $" + Account.convert2DP(loggedInAccount.getTransLimit()));
        System.out.println("Balance: $" + Account.convert2DP(loggedInAccount.getBalance()));
        System.out.println("1. Transfer Funds");
        System.out.println("2. Change transfer limit");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Display Loan Details");
        System.out.println("6. Get a loan");
        System.out.println("7. Pay loan");
        System.out.println("8. Credit Card");
        System.out.println("9. Go back to accounts menu");
        System.out.println("10. Logout");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }

    /**
     * Handles the Money Transfer operation.
     * Asks the user prompts and transfers the money using the Bank class
     * @param bank The active Bank instance
     * @param loggedInAccount The active user's account
     */
    public static void performTransfer(Bank bank, Account loggedInAccount) {
        try {
            System.out.print("Enter the account number to transfer money to: ");
            String transferAccountNum = scanner.nextLine();
            System.out.print("Enter the amount to transfer: $");
            String transferAmount = scanner.nextLine();
            // scanner.nextLine();     // Consumes the \n after the double
            bank.transferMoney(loggedInAccount.getAccountNum(), transferAccountNum, Double.parseDouble(transferAmount));
        }
        catch (InputMismatchException | NumberFormatException e) {
            BankUI.printInvalid();
        }
    }

    /**
     * Handles the updating of transfer limit operation.
     * Prompts the user for input and updates the limit through the account class.
     * @param loggedInAccount The active user's account
     */
    public static void updateTransferLimit(Account loggedInAccount) {
        while (true) {
            System.out.println("------------------------------------");
            System.out.println("Current transfer limit: $" + Account.convert2DP(loggedInAccount.getTransLimit()));
            System.out.print("Enter new transfer limit: $");
            try {
                String newTransLimitStr = scanner.nextLine();
                double newTransLimit = Double.parseDouble(newTransLimitStr);
                loggedInAccount.setTransferLimit(newTransLimit);
                CSVHandler.updateCSV(loggedInAccount.getAccountNum(), "Accounts.csv", loggedInAccount.convertToCSV());
                System.out.println("Transfer limit changed successfully!");
                break;
            } catch (InputMismatchException | NumberFormatException e) {
                BankUI.printInvalid();
            }
        }
    }
    
    /**
     * Handles the deposit operation.
     * Prompts the user the amount to deposit and deposit it through the account class
     * @param loggedInAccount The active user's account
     */
    public static void performDeposit(Account loggedInAccount) {
        try {
            System.out.print("Enter the amount to deposit: $");
            String depositAmount = scanner.nextLine();
            // scanner.nextLine();     // Consumes the \n after the double
            loggedInAccount.deposit(Double.parseDouble(depositAmount));
        }
        catch (InputMismatchException | NumberFormatException e) {
            BankUI.printInvalid();
        }
    }

    /**
     * Handles the withdrawal operation.
     * Prompts the user the amount to withdraw and withdraws it through the account class
     * @param loggedInAccount The active user's account
     */
    public static void performWithdrawal(Account loggedInAccount) {
        try {
            System.out.print("Enter the amount to withdraw: $");
            String withdrawAmount = scanner.nextLine();
            // scanner.nextLine();     // Consumes the \n after the double
            loggedInAccount.withdraw(Double.parseDouble(withdrawAmount));
        }
        catch (InputMismatchException | NumberFormatException e) {
            BankUI.printInvalid();
        }
    }

    /**
     * Handles the creation of loans.
     * Prompts the user for the loan details and calls the external loan component with the information provided.
     * @param loggedInAccount The active user's account
     */
    public static void createLoan(Account loggedInAccount) {
        
        if (loggedInAccount.getLoanId() != null) {
            System.out.println("You already have an active loan.");
            return;
        }
        else{
            try{
                System.out.print("Loan amount: ");
                float principal = Float.parseFloat(scanner.nextLine());
                System.out.print("Loan term (1 to 7 years): ");
                int loanTermMonths = Integer.parseInt(scanner.nextLine()) * 12;
                LocalDate date = LocalDate.now();
                // hard coded annual flat rate of 6.0%
                double interestRate = 0.06;
                loggedInAccount.applyForLoan(principal, interestRate, date, loanTermMonths);
                loggedInAccount.displayLoanDetails();
            }catch (NumberFormatException e) {
                BankUI.printInvalid();
            }
        }
    
    }

    /**
     * Handles the payment of loan operation.
     * Prompts the user for the amount to pay the loan and pays the loan using the external loan component.
     * @param loggedInAccount The active user's account
     */
    public static void payLoan(Account loggedInAccount){

        if (loggedInAccount.getLoanId() == null) {
            System.out.println("No active loan for this account.");
            return;
        }
        else{
            try{
                    System.out.print("Enter the amount to pay: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    if (amount > loggedInAccount.getBalance()) {
                        System.out.println("Insufficient balance to pay loan.");
                        return;
                    }
                    else if (amount > loggedInAccount.getTransLimit()){
                        System.out.println("Transfer amount exceeds account transfer limit. Please increase transfer limit before trying again.");
                        return;
                    }
                    else{
                        loggedInAccount.makeLoanPayment(amount);
                        if (loggedInAccount.getLoanRepayment() <= 0) {
                            loggedInAccount.setBalance(loggedInAccount.getBalance() + (amount - loggedInAccount.getLoanRepayment()));
                            loggedInAccount.deleteLoan();
                        }
                        loggedInAccount.displayLoanDetails();
                        loggedInAccount.setBalance(loggedInAccount.getBalance() - amount);
                    }
    
                }
            catch (NumberFormatException e) {
                BankUI.printInvalid();
    
                }
            }
    
        }

    /**
     * Displays the user's menu and handles the user's chocies in the menu.
     * @param bank The active Bank instance
     * @param customer The active Customer
     * @param accNum The account number of the account
     */
    public static void transactMenu(Bank bank, Customer customer, String accNum) {
        Account loggedInAccount = new Account(accNum);

        while (true) {
            displayAccountMenu(loggedInAccount);
            int choice = BankUI.getUserChoice();

            switch (choice) {
                case 1:
                    // Transfer money
                    performTransfer(bank, loggedInAccount);
                    break;
                case 2:
                    // Change transfer limit
                    updateTransferLimit(loggedInAccount);
                    break;
                case 3:
                    // Deposit
                    performDeposit(loggedInAccount);
                    break;
                case 4:
                    // Withdraw
                    performWithdrawal(loggedInAccount);
                    break;
                case 5:
                    loggedInAccount.displayLoanDetails();
                    break;
                case 6:
                    createLoan(loggedInAccount);
                    break;
                case 7:
                    payLoan(loggedInAccount);
                    break;
                case 8:
                    // Credit card
                    createCreditCard();
                    break;
                case 9:
                    // go back to accounts menu
                    return;
                case 10:
                    System.out.println("Logging out...");
                    displayMainMenu(bank);
                default:
                    printInvalid();
                    break;
            }
        }
    }

    /**
     * Prints the invalid input message
     */
    public static void printInvalid() {
        System.out.println("------------------------------------");
        System.out.println("Invalid input. Please try again.");
    }
   
    /**
     * Gets the user's choice from the terminal
     * @return Returns the user's choice back
     */
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

}