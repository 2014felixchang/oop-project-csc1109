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

public class BankUI {
    private static Scanner scanner = new Scanner(System.in);
    

    public static void printMainMenu(Bank bank) {
        System.out.println("------------------------------------");
        System.out.println("Welcome to " + bank.getBankName() + "! Please give us your money!");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.println("4. Branches");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }

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
                case 4:
                    BankUI.viewBranches(bank);
                    break;
                
                default:
                    BankUI.printInvalid();
                    break;
            }
        }
    }

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
            CreditCard cc = new CreditCard(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "", ownLimit, creditLimit, creditBill, accountNum);
            if (benefits >= 3000) {
                cc.setCardType("Travel Credit Card");
            } else if (benefits >= 500) {
                cc.setCardType("Rewards Credit Card");
            } else if (benefits >= 200) {
                cc.setCardType("Student Credit Card");
            } else {
                cc.setCardType("Standard");
            }

            // Now you can call methods on this instance
            cc.displayCreditCardDetails();

            // Example of using the extended class CreditCardRewards
            CreditCardRewards ccRewards = new CreditCardRewards(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, ownLimit, creditLimit, creditBill, accountNum);
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
            

            // Example of using the extended class CreditCardStudent
            CreditCardStudent ccStudent = new CreditCardStudent(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, ownLimit, creditLimit, creditBill, accountNum);
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

            // Example of using the extended class CreditCardTravel
            CreditCardTravel ccTravel = new CreditCardTravel(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, ownLimit, creditLimit, creditBill, accountNum);
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
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }


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

    public static void printAdminMenu(Customer admin) {
        System.out.println("------------------------------------");
        System.out.println("Welcome to the admin menu, " + admin.getUsername() + "!");
        System.out.println("1. View all customers");
        System.out.println("2. Add a new customer");
        System.out.println("3. Remove a customer");
        System.out.println("4. Unlock a customer account");
        System.out.println("5. Add a new branch to branches information page");
        System.out.println("6. Remove a branch from branches information page");
        System.out.println("7. Logout");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }

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
                    Branch.addNewBranch();
                    break;
                case 6:
                    Branch.removeBranch();
                    break;
                case 7:
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
                else{
                    // go to transact menu for selected account
                    for (int j = 0; j < accounts.length; j++) {
                        if (Integer.parseInt(choice) == j+1) {
                            transactMenu(bank, customer, accounts[j]);
                        }
                    }
                }
            } catch (InputMismatchException | NumberFormatException e) {
                BankUI.printInvalid();
                continue;
            }
        }
    }

    public static void displayAccountMenu(Account loggedInAccount) {
        System.out.println("------------------------------------");
        System.out.println("Account number: " + loggedInAccount.getAccountNum());
        System.out.println("Transfer limit: $" + Account.convert2DP(loggedInAccount.getTransLimit()));
        System.out.println("Balance: $" + Account.convert2DP(loggedInAccount.getBalance()));
        System.out.println("1. Transfer Funds");
        System.out.println("2. Change transfer limit");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Currency Exchange");
        System.out.println("6. Display Loan Details");
        System.out.println("7. Get a loan");
        System.out.println("8. Pay loan");
        System.out.println("9. Create Insurance Policy");
        System.out.println("10. Go back to accounts menu");
        System.out.println("11. Logout");
        System.out.println("12. Credit Card");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }

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

    public static void performCurrencyExchange(Account loggedInAccount) {
        ForeignExchange foreignExchange = new ForeignExchange();
        foreignExchange.displayRates();
        System.out.println("Enter the currency to convert from (SGD/USD/JPY):");
        String fromCurrency = scanner.next();
        double exchangeAmount;
        if (fromCurrency.equals("SGD")){
            System.out.println("Enter the amount to exchange: ");
            exchangeAmount = scanner.nextDouble();
            scanner.nextLine();     // Consumes the \n after the double
            if(loggedInAccount.getBalance() < exchangeAmount){
                System.out.println("Not enough money in balance!");
                return;
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
        if(toCurrency.equals("SGD")){
            System.out.println("Converted amount: " + convertedAmount + " SGD");
            System.out.println("Adding to balance...");
            loggedInAccount.setBalance(loggedInAccount.getBalance() + convertedAmount);
        }else{
            System.out.println("Converted amount: " + convertedAmount + " " + toCurrency);
            System.out.println("Dispensing amount...");
        }
    }

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
                    // Foreign Exchange
                    performCurrencyExchange(loggedInAccount);
                    break;
                case 6:
                    loggedInAccount.displayLoanDetails();
                    break;
                case 7:
                    createLoan(loggedInAccount);
                    break;
                case 8:
                    payLoan(loggedInAccount);
                    break;
                case 9:
                    createNewInsurancePolicy();
                    // go back to accounts menu
                    break;
                case 10:
                    // go back to accounts menu
                    return;
                case 11:
                    System.out.println("Logging out...");
                    return;
                case 12:
                    // Credit card
                    createCreditCard();
                    break;
                default:
                    printInvalid();
                    break;
            }
        }
    }
    //create policy
    public static void createNewInsurancePolicy() {
        try {
            // Prompt user for input
            System.out.println("Enter policy type (1 for LIFE, 2 for HEALTH, 3 for ACCIDENT): ");
            int policyTypeIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
    
            System.out.println("Enter coverage option (1 for BASIC($1000), 2 for STANDARD($2000), 3 for PREMIUM($3000)): ");
            int coverageOptionIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
    
            System.out.println("Enter policy tenure (1 for FIVE_YEARS, 2 for TEN_YEARS, 3 for FIFTEEN_YEARS, 4 for TWENTY_YEARS): ");
            int policyTenureIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
    
            System.out.println("Enter premium frequency (1 for MONTHLY, 2 for QUARTERLY, 3 for SEMI_ANNUALLY, 4 for ANNUALLY): ");
            int premiumFrequencyIndex = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
    
            System.out.println("Enter policy start date (yyyy-MM-dd): ");
            String startDateString = scanner.next();
            scanner.nextLine(); // Consume newline character
    
            System.out.println("Enter age : ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
    
            boolean smoker = false; // Default value
            if (policyTypeIndex == 1 || policyTypeIndex == 2) {
                System.out.println("Are you a smoker? Additonal $500 (1 for Yes, 2 for No): ");
                int smokerInput = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                smoker = (smokerInput == 1);
            }
    
            boolean pastInjuries = false; // Default value
            if (policyTypeIndex == 3) {
                System.out.println("Do you have past injuries? Addition $1000 (1 for Yes, 2 for No): ");
                int pastInjuriesInput = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                pastInjuries = (pastInjuriesInput == 1);
            }
    
            // Create and display the insurance policy
            InsurancePolicy insurancePolicy = null;
            switch (policyTypeIndex) {
                case 1:
                    insurancePolicy = new LifeInsurance(startDateString, InsurancePolicy.CoverageOption.values()[coverageOptionIndex - 1],
                        InsurancePolicy.PolicyTenure.values()[policyTenureIndex - 1], InsurancePolicy.PremiumFrequency.values()[premiumFrequencyIndex - 1],
                        age, smoker);
                    break;
                case 2:
                    insurancePolicy = new HealthInsurance(startDateString, InsurancePolicy.CoverageOption.values()[coverageOptionIndex - 1],
                        InsurancePolicy.PolicyTenure.values()[policyTenureIndex - 1], InsurancePolicy.PremiumFrequency.values()[premiumFrequencyIndex - 1],
                        age, smoker);
                    break;
                case 3:
                    insurancePolicy = new AccidentInsurance(startDateString, InsurancePolicy.CoverageOption.values()[coverageOptionIndex - 1],
                        InsurancePolicy.PolicyTenure.values()[policyTenureIndex - 1], InsurancePolicy.PremiumFrequency.values()[premiumFrequencyIndex - 1],
                        age, pastInjuries);
                    break;
                default:
                    System.out.println("Invalid policy type.");
                    return;
            }
    
            // Display policy details
            System.out.println(insurancePolicy.displayPolicyDetails());
    
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
        } catch (InsurancePolicy.PolicyException e) {
            System.out.println("Error creating insurance policy: " + e.getMessage());
        }
    }
    
    public static void viewBranches(Bank bank) {
        ArrayList<Branch> branches = new ArrayList<Branch>();
        try (BufferedReader br = new BufferedReader(new FileReader("Branches.csv"))){
            String branchInfo;
            while ((branchInfo = br.readLine()) != null) {
                String attributes[] = branchInfo.split(",");
                Branch branch = new Branch(Integer.parseInt(attributes[0]), attributes[1], attributes[2], LocalTime.parse(attributes[3]), LocalTime.parse(attributes[4]));
                branches.add(branch);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(e);
            return;
        }
        while (true) {
            System.out.println("------------------------------------");
            System.out.println("These are our branches!");
            for (Branch branch : branches) {
                System.out.println("------------------------------------");
                branch.printBranchInfo();
            }
            System.out.println("------------------------------------");
            System.out.print("Press enter to return to main menu. ");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                return;
            }
        }
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