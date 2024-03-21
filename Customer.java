
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Define a Customer class
public class Customer {

    private String username;
    private String password;
    private String role;
    private String id;
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
        this.username = username;
        this.role = role;
        this.id = id;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return String.valueOf(password);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // // Method to return all customer details as an array
    // public String[] getDetails() {
    //     return new String[] {username, password, role, id};
    // }

    // Method to return customer's administrative info as a comma-separated string
    public String customerInfoToCSV() {
        String custRecord = username+","+password+","+role+","+id+","+failedAttempts+","+locked;
        return custRecord;
    }

    // Method to return customer's personal details as a comma-separated string
    public String personalDetailsToCSV() {
        String custRecord = username+","+name+","+address+","+phoneNumber+","+email+","+dateOfBirth;
        return custRecord;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public int getFailedAttempts() {
        return this.failedAttempts;
    }

    public void setFailedAttempts(int attempts) {
        this.failedAttempts = attempts;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public static boolean loginCustomer(String username, String password) {
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
    }
    
    
    

    public static Customer registerCustomer(String username, String password, String role, String id) {
        // Create a new Customer object with the provided details
        Customer newCustomer = new Customer(username, password, role, id);
        // Add the new customer to the customers map
        customers.put(username, newCustomer);
        // Write the new customer's administrative info to CustomerInfo csv
        CSVHandler.addRecord("CustomerInfo.csv", newCustomer.customerInfoToCSV());
        // Write the new customer's personal details to CustomerDetails csv
        // CSVHandler.addRecord("CustomerDetails.csv", customer.personalDetailsToCSV());

        String newAccNum = Bank.generateAccNum();
        Account newAccount = new Account(newAccNum);
        CSVHandler.addRecord("Accounts.csv", newAccount.convertToCSV());
        
        // update CustomerAccounts.csv with new account added to customer
        String newCustAccounts = username+","+newAccNum;
        CSVHandler.addRecord("CustomerAccounts.csv", newCustAccounts);

        return newCustomer;
    }
    
    // Returns a String array of only the customer's accounts' numbers
    // public String[] getCustomerAccounts() {
    //     String custAccInfo = CSVHandler.getCustAccsFromCSV(this.username);
    //     if (custAccInfo == null) {
    //         return new String[0]; // return an empty array if no accounts found
    //     }
    //     String[] accounts = custAccInfo.split(",");
    //     for (int i = 1; i < accounts.length; i++) {
    //         accounts[i-1] = accounts[i];
    //     }
    //     return Arrays.copyOf(accounts, accounts.length - 1);
    // }

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