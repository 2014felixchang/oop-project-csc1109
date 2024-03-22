
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Define a Customer class
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

    // Constructor for the Customer class
    public Customer(String username, String password, String role, String id) {
        super(username, password, role, id);
    }
    
    public String getName() {
        return name;
    }

    // Method to return customer's administrative info as a comma-separated string
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

    // Method to return customer's personal details as a comma-separated string
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

    public boolean isLocked() {
        return this.locked;
    }

    public int getFailedAttempts() {
        return this.failedAttempts;
    }

    public void setFailedAttempts(int attempts) {
        try {
            this.failedAttempts = attempts;
        } catch (Exception e) {
            System.out.println("An error occurred while setting failed attempts: " + e.getMessage());
        }
    }

    public void setLocked(boolean locked) {
        try {
            this.locked = locked;
        } catch (Exception e) {
            System.out.println("An error occurred while setting locked status: " + e.getMessage());
        }
    }

    public static void removeCustomer(String username) {
        try {
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

class User {
    protected String username;
    protected String password;
    protected String role;
    protected String id;

    public User(String username, String password, String role, String id) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public String customerInfoToCSV() {
        try {
            String userRecord = username+","+password+","+role+","+id;
            return userRecord;
        } catch (Exception e) {
            System.out.println("An error occurred while converting user info to CSV: " + e.getMessage());
            return null;
        }
    }

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