import java.io.*;

// Define a Customer class
public class Customer {
    // Declare private fields for customer details
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;

    // Constructor for the Customer class
    public Customer(String name, String address, String phoneNumber, String email, String dateOfBirth) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
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
}

// Define a class to write Customer data to a CSV file
class CustomerCSVWriter {
    // Method to append a customer's details to a CSV file
    public static void appendCustomerToCSV(Customer customer) {
        // Get the customer's details as an array
        String[] data = customer.getDetails();

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