import java.util.Scanner;

public class BankUI {
    private static Scanner scanner = new Scanner(System.in);

    public static void displayMainMenu() {
        System.out.println("------------------------------------");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
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
            System.out.println("Welcome to the admin menu, " + admin.getUsername() + "!");
            System.out.println("1. View all customers");
            System.out.println("2. Add a new customer");
            System.out.println("3. Remove a customer");
            System.out.println("4. Unlock a customer account");
            System.out.println("5. Logout");
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
                    CSVHandler.removeCustomer(usernameToRemove);
                    System.out.println("Customer removed successfully."); 
                    break;
                case "4":
                    System.out.print("Enter the username of the customer to unlock: ");
                    String usernameToUnlock = scanner.nextLine();
                    Customer customerToUnlock = CSVHandler.retrieveCustomer(usernameToUnlock);
                    if (customerToUnlock != null) {
                        CSVHandler.updateCustomerLockStatus(usernameToUnlock, "0");
                        System.out.println("Account unlocked successfully.");
                    } else {
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
                    Bank.main(null);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public static int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();     // Consumes the \n after the integer
        return choice;
    }

    // ... more methods for other menus and user input
}