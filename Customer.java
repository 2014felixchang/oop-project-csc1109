
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Customer class inherits the User class in and represents a customer for the Bank
 */
public class Customer extends User{
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private int failedAttempts = 0;
    private boolean locked = false;

    // Static map to store all customers
    private static Map<String, Customer> customers = new HashMap<>();

    /**
     * Constructor for the Customer class
     * @param username The username of the customer
     * @param password The password of the customer
     * @param role The role of the customer (User/Admin)
     * @param id The customer's id
     */
    public Customer(String username, String password, String role, String id) {
        super(username, password, role, id);
    }
    
    /**
     * Getter for the name of the customer
     * @return The name of the customer
     */
    public String getName() {
        return name;
    }

    /**
     * Method to return customer's administrative info as a comma-separated string
     */
    @Override
    public String customerInfoToCSV() {
        try {
            String custRecord = super.customerInfoToCSV() + "," + failedAttempts + "," + locked;
            return custRecord;
        } catch (Exception e) {
            System.out.println("An error occurred while converting customer info to CSV: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to return customer's personal details as a comma-separated string
     */
    @Override
    public String personalDetailsToCSV() {
        try {
            String custRecord = super.personalDetailsToCSV() + "," + name + "," + address + "," + phoneNumber + "," + email + "," + dateOfBirth;
            return custRecord;
        } catch (Exception e) {
            System.out.println("An error occurred while converting personal details to CSV: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to check whether the customer is locked out of the account
     * @return A boolean of true or false
     */
    public boolean isLocked() {
        return this.locked;
    }

    /**
     * Getter for the amount of failed log in attempts by the customer
     * @return The amount of failed log in attempts
     */
    public int getFailedAttempts() {
        return this.failedAttempts;
    }

    /**
     * Setter for setting the amount of failed log in attempts
     * @param attempts The amount of failed log in attempts
     */
    public void setFailedAttempts(int attempts) {
        try {
            this.failedAttempts = attempts;
        } catch (Exception e) {
            System.out.println("An error occurred while setting failed attempts: " + e.getMessage());
        }
    }

    /**
     * Setter for whether the customer's account is locked due to failed log in attempts
     * @param locked Boolean of True (Locked) or False (not locked)
     */
    public void setLocked(boolean locked) {
        try {
            this.locked = locked;
        } catch (Exception e) {
            System.out.println("An error occurred while setting locked status: " + e.getMessage());
        }
    }

    /**
     * Method to handle the Admin's action of unlocking a customer's account
     */
    public static void unlockCustomerAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the username of the customer to unlock: ");
        String usernameToUnlock = scanner.nextLine();
        Customer customerToUnlock = CSVHandler.retrieveCustomer(usernameToUnlock);
        if (customerToUnlock != null) {
            customerToUnlock.setLocked(false);
            customerToUnlock.setFailedAttempts(0);
            CSVHandler.updateCSV(usernameToUnlock, "CustomerInfo.csv", customerToUnlock.customerInfoToCSV());
            System.out.println("Account unlocked successfully.");
        } else {
            System.out.println("Customer not found.");
        }
    }

    /**
     * Method for the admin to remove a customer from its records
     */
    public static void removeCustomer() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the username of the customer to remove: ");
            String username = scanner.nextLine();

            String accountsToRemove = CSVHandler.getRecord(username, "CustomerAccounts.csv");
            if (accountsToRemove == null) {
                System.out.println("Username does not exist.");
            }
            else {
                String[] accountsToRemoveArray = accountsToRemove.split(",");
                // start from the second column, index 1, since first col is username
                for (int i = 1; i < accountsToRemoveArray.length; i++) {
                    CSVHandler.removeRecord(accountsToRemoveArray[i], "Accounts.csv");
                }
                CSVHandler.removeRecord(username, "CustomerInfo.csv");
                CSVHandler.removeRecord(username, "CustomerAccounts.csv");
                CSVHandler.removeRecord(username, "CustomerDetails.csv");

                // Remove the customer from the customers map
                customers.remove(username);

                System.out.println("Customer removed successfully."); 
            }
        } catch (Exception e) {
            System.out.println("An error occurred during customer removal: " + e.getMessage());
        }
    }

    /**
     * Method for the customer to log in to their accounts
     * @param username The username provided by the customer
     * @param password The password provided by the customer
     * @return Returns True for Success and False for Failure of the log in process
     */
    public static boolean loginCustomer(String username, String password) {
        try {
            // Retrieve customer from CSV
            Customer customer = CSVHandler.retrieveCustomer(username);
            // Check if customer exists
            if (customer == null) {
                System.out.println("No such user exists.");
                return false;
            }
            // Check if account is locked
            if (customer.isLocked()) {
                System.out.println("The account is locked and cannot be accessed.");
                return false;
            }
            // Hash the input password
            String passwordHash = PasswordHasher.hashPassword(password);
            // Check if the hashed password matches the one stored
            if (!customer.getPassword().equals(passwordHash)) {
                // Increment failed login attempts
                customer.setFailedAttempts(customer.getFailedAttempts() + 1);
                // Check if failed attempts exceed the limit
                if (customer.getFailedAttempts() >= 3) {
                    // Lock the account if there are 3 or more failed attempts
                    customer.setLocked(true);
                    System.out.println("Account is locked due to 3 failed attempts. Please contact admin.");
                }
                // Update the customer record in CSV
                CSVHandler.updateCSV(username, "CustomerInfo.csv", customer.customerInfoToCSV());
                return false;
            } else {
                // Successful login logic here (if needed)
                return true;
            }
        } catch (Exception e) {
            System.out.println("An error occurred during login: " + e.getMessage());
            return false;
        }
    } 

    /**
     * Method for a new registration of a new customer
     * @param username The username provided by the customer
     * @param password The password provided by the customer
     * @param role The role of the account (User/Admin)
     * @param id The id provided by the customer
     * @return Returns the newly-made customer object based on the information provided
     */
    public static Customer registerCustomer(String username, String password, String role, String id) {
        try {
            // Convert role to "Admin" if it equals "admin" (ignoring case)
            if (role.equalsIgnoreCase("admin")) {
                role = "Admin";
            }

            // Create a new Customer object with the provided details
            Customer newCustomer = new Customer(username, password, role, id);
            // Add the new customer to the customers map
            customers.put(username, newCustomer);
            // Write the new customer's info to CustomerInfo.csv
            CSVHandler.addRecord("CustomerInfo.csv", newCustomer.customerInfoToCSV());

            String newAccNum = Bank.generateAccNum();
            Account newAccount = new Account(newAccNum);
            CSVHandler.addRecord("Accounts.csv", newAccount.convertToCSV());
            
            // update CustomerAccounts.csv with new account added to customer
            String newCustAccounts = username+","+newAccNum;
            CSVHandler.addRecord("CustomerAccounts.csv", newCustAccounts);

            return newCustomer;
        } catch (Exception e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method for the admin to add a new customer
     */
    public static void addNewCustomer() {
        try {
            Scanner scanner = new Scanner(System.in);
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
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("An error occurred while adding a new customer: " + e.getMessage());
        }
    }
    
    /**
     * Method for the admin to view all the customers
     */
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
    
}

/**
 * The User class represents the basic core attributes of a typical user in a Bank
 */
class User {
    protected String username;
    protected String password;
    protected String role;
    protected String id;

    /**
     * Constructor for the User class
     * @param username The username provided
     * @param password The password provided
     * @param role The role of the user (User/Admin)
     * @param id The id provided
     */
    public User(String username, String password, String role, String id) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    /**
     * Getter for the username
     * @return Returns the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the role
     * @return Returns the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Getter for the password
     * @return Returns the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the id
     * @return Returns the id
     */
    public String getId() {
        return id;
    }

    /**
     * Method to build the string of user information for CSV input
     * @return The string that is built
     */
    public String customerInfoToCSV() {
        try {
            String userRecord = username+","+password+","+role+","+id;
            return userRecord;
        } catch (Exception e) {
            System.out.println("An error occurred while converting user info to CSV: " + e.getMessage());
            return null;
        }
    }

    /**
     * Method to build the string to input the username into the CSV
     * @return The username string that has been built
     */
    public String personalDetailsToCSV() {
        try {
            String userRecord = username;
            return userRecord;
        } catch (Exception e) {
            System.out.println("An error occurred while converting user personal details to CSV: " + e.getMessage());
            return null;
        }
    }
}