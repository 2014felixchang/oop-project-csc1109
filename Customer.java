import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Define a Customer class
public class Customer {
    // Declare private fields for customer details
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private String username;
    private int password;

    // Static map to store all customers
    private static Map<String, Customer> customers = new HashMap<>();

    // Constructor for the Customer class
    public Customer(String name, String address, String phoneNumber, String email, String dateOfBirth, String username, String password) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.password = password;
    }

    // Method to register a new customer
    public static void registerCustomer(String name, String address, String phoneNumber, String email, String dateOfBirth, String username, String password) {
        // Create a new Customer object
        Customer customer = new Customer(name, address, phoneNumber, email, dateOfBirth, username, password);
        // Add the customer to the map
        customers.put(username, customer);
    }

    // Method to login a customer
    public static Customer loginCustomer(String username, String password) {
        // Check if the username exists in the map
        if (customers.containsKey(username)) {
            // Get the customer object from the map
            Customer customer = customers.get(username);
            // Check if the password matches the customer's password
            if (customer.password.equals(password)) {
                // Return the customer object
                return customer;
            }
        }
        // Return null if the username or password is incorrect
        return null;
    }

    // Method to check if a username is available
    public static boolean isUsernameAvailable(String username) {
        // Check if the username exists in the map
        return !customers.containsKey(username);
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
        return new String[] {name, address, phoneNumber, email, dateOfBirth};
    }
    public void appendToCSV() {
        // Get the customer's details as an array
        String[] data = getDetails();

        try {
            // Create a FileWriter in append mode
            FileWriter fileWriter = new FileWriter("filename.csv", true); 
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            // Write the customer's details to the file, separated by commas
            bufferedWriter.write(String.join(",", data));
            // Add a new line to the file
            bufferedWriter.newLine(); 
            // Close the BufferedWriter
            bufferedWriter.close();
        } catch (IOException e) {
            // Handle any exceptions that occur
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}


