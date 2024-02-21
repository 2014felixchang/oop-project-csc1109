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

    // Static map to store all customers
    private static Map<String, Customer> customers = new HashMap<>();

    // Constructor for the Customer class
    public Customer(String username, String password, String role, String id) {
        this.username = username;
        this.role = role;
        this.id = id;
        this.password = password;
    }

    public static boolean loginCustomer(String username, String password) {
        // Retrieve the Customer object for the provided username
        Customer customer = CSVHandler.retrieveCustomer(username);
        // Check if the customer exists
        if (customer == null) {
            return false;
        }
        else {
            String storedHash = customer.getPassword();
            String passwordHash = PasswordHasher.hashPassword(password);
            if (storedHash.equals(passwordHash)) {
                return true;
            }
            else {
                return false;
            }
        }
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

    // Method to check if a username is available
    public static boolean isUsernameAvailable(String username) {
        // Check if the username exists in the map
        return !customers.containsKey(username);
    }
    
    public void setPassword(String password) {
        this.password = PasswordHasher.hashPassword(password);
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
    
    // Getter methods for customer details
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    // Method to return all customer details as an array
    public String[] getDetails() {
        return new String[] {username, password, role, id};
    }
    
    // Returns a String array of only the customer's accounts' numbers
    public String[] getCustomerAccounts() {
        String custAccInfo[] = CSVHandler.getCustAccsFromCSV(this.username).split(",");
        String accounts[] = new String[0];

        for (int i = 1; i < custAccInfo.length; i++) {
            accounts = new String[custAccInfo.length-1];
            accounts[i-1] = custAccInfo[i];
        }
        
        return accounts;
    }
}