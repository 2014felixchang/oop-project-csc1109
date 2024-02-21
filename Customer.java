import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// Define a Customer class
public class Customer {
    // Declare private fields for customer details
    private String name;
    // private Account account;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
    private String username;
    private String password;

    // Static map to store all customers
    private static Map<String, Customer> customers = new HashMap<>();

    // Constructor for the Customer class
    public Customer(String name, String address, String phoneNumber, String email, String dateOfBirth, String username, String password, Bank bank) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.password = password;

        // this.account = new Account("123", 10.0, 10);
        // bank.addAccount(this.account);
    }

    public String[] getAccounts() {
        String accounts[] = new String[0];

        try (BufferedReader bR = new BufferedReader(new FileReader("CustomerAccounts.csv"))){
            String currentLine;

            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(username) == true) {
                    String[] data = currentLine.split(",");
                    accounts = new String[data.length-1];

                    for (int i = 1; i < data.length; i++) {
                        accounts[i-1] = data[i];
                    }
                }
                return accounts;
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return accounts;
    }

    public static boolean loginCustomer(String username, String password) {
        // Retrieve the Customer object for the provided username
        String user = Customer.retrieveCustomerRecord(username);
        // Check if the customer exists
        if (user == null) {
            return false;
        }
        else {
            String data[] = user.split(",");
            String pswd = data[6];
            if (password.equals(pswd)) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static void registerCustomer(String name, String address, String phoneNumber, String email, String dateOfBirth, String username, String password) {
        // Create a new Customer object with the provided details
        Customer customer = new Customer(name, address, phoneNumber, email, dateOfBirth, username, password, new Bank("ABC Bank"));
        // Add the new customer to the customers map
        customers.put(username, customer);
        // Write the new customer's details to the CSV file
        CustomerCSVWriter.appendCustomerToCSV(customer);
    }


    // Method to check if a username is available
    public static boolean isUsernameAvailable(String username) {
        // Check if the username exists in the map
        return !customers.containsKey(username);
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
        return new String[] {name, address, phoneNumber, email, dateOfBirth, username, password};
    }

   

    /*
     * Process: update existing account record in csv
     */
    public void updateRecord(String newAccNum) {
        String currentLine;
        String newAccInfo = retrieveCustomerAccounts(username) + "," + newAccNum;
        
        try (
            BufferedReader bR = new BufferedReader(new FileReader("CustomerAccounts.csv")); 
            BufferedWriter bW = new BufferedWriter(new FileWriter("temp.csv", false))
            ) 
        {
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(this.username) == false) {
                    bW.write(currentLine, 0, currentLine.length());
                    bW.newLine();
                }
                else {
                    bW.write(newAccInfo, 0, newAccInfo.length());
                    bW.newLine();
                }
            }
        } 
        catch (IOException e) {
            System.err.println(e);
        }

        try {
            Path accPath = Paths.get("CustomerAccounts.csv");
            Path tempPath = Paths.get("temp.csv");
            // delete old file and rename temp file to accounts.csvs
            Files.delete(accPath);
            Files.move(tempPath, accPath);
            // File dump = new File("Accounts.csv");
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    
    // static method to get customer accounts info
    public static String retrieveCustomerAccounts(String username) {
        try (BufferedReader bR = new BufferedReader(new FileReader("CustomerAccounts.csv"))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(username) == true) {
                    return currentLine;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

     // static method to get customer info
     public static String retrieveCustomerRecord(String username) {
        try (BufferedReader bR = new BufferedReader(new FileReader("CustomerInfo.csv"))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[5].equals(username) == true) {
                    return currentLine;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
}

// Define a class to write Customer data to a CSV file
class CustomerCSVWriter {
    // Method to append a customer's details to a CSV file
    public static void appendCustomerToCSV(Customer customer) {
        // Get the customer's details as an array
        String[] data = customer.getDetails();

        try {
            // Create a BufferedWriter in append mode
            BufferedWriter custInfoWriter = new BufferedWriter(new FileWriter("CustomerInfo.csv", true));
            BufferedWriter custAccWriter = new BufferedWriter(new FileWriter("CustomerAccounts.csv", true));
            // Write the customer's details to the file, separated by commas
            custInfoWriter.write(String.join(",", data));
            custAccWriter.write(data[5]);
            // Add a new line to the file
            custInfoWriter.newLine(); 
            custAccWriter.newLine();
            // Close the BufferedWriter
            custInfoWriter.close();
            custAccWriter.close();
        } catch (IOException e) {
            // Handle any exceptions that occur
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
}