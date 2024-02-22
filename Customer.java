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

    // Method to return all customer details as an array
    public String[] getDetails() {
        return new String[] {username, password, role, id};
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
        Customer customer = CSVHandler.retrieveCustomer(username);
        if (customer == null) {
            return false;
        }
        else if (customer.isLocked()) {
            System.out.println("The account is locked and cannot be accessed.");
            return false;
        }
        else {
            String storedHash = customer.getPassword();
            String passwordHash = PasswordHasher.hashPassword(password);
            if (storedHash.equals(passwordHash)) {
                customer.setFailedAttempts(0);
                CSVHandler.updateCustomer(customer); // Update the customer in the CSV file
                return true;
            }
            else {
                customer.setFailedAttempts(customer.getFailedAttempts() + 1);
                if (customer.getFailedAttempts() >= 3) {
                    customer.setLocked(true);
                }
                CSVHandler.updateCustomer(customer); // Update the customer in the CSV file
                return false;
            }
        }
    }


    public void unlockAccount() {
        this.locked = false;
        this.failedAttempts = 0;
    }

    public static void registerCustomer(String username, String password, String role, String id) {
        // Create a new Customer object with the provided details
        Customer customer = new Customer(username, password, role, id);
        // Add the new customer to the customers map
        customers.put(username, customer);
        // Write the new customer's details to the CSV file
        CSVHandler.addCustomerToCSV(customer);

        // call generate random acount num
        String randomAccNum = Bank.generateAccNum();
        // create new account obj, save account to csv
        Account newAccount = new Account(randomAccNum);
        CSVHandler.addAccountToCSV(newAccount);
        // update CustomerAccounts.csv with new account added to customer account info
        String newCustomerRecord = CSVHandler.getCustAccsFromCSV(customer.getUsername()) + "," + randomAccNum;
        CSVHandler.updateCSV(customer.getUsername(), "CustomerAccounts.csv", newCustomerRecord);
    }
    
    // Returns a String array of only the customer's accounts' numbers
    public String[] getCustomerAccounts() {
        String custAccInfo = CSVHandler.getCustAccsFromCSV(this.username);
        if (custAccInfo == null) {
            return new String[0]; // return an empty array if no accounts found
        }
        String[] accounts = custAccInfo.split(",");
        for (int i = 1; i < accounts.length; i++) {
            accounts[i-1] = accounts[i];
        }
        return Arrays.copyOf(accounts, accounts.length - 1);
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