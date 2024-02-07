import java.io.*;

public class Customer {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;

    public Customer(String name, String address, String phoneNumber, String email, String dateOfBirth) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
        //hihi
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String[] getDetails() {
        return new String[] {name, address, phoneNumber, email, dateOfBirth};
    }
}

class CustomerCSVWriter {
    public static void appendCustomerToCSV(Customer customer) {
        String[] data = customer.getDetails();

        try {
            FileWriter fileWriter = new FileWriter("filename.csv", true); // Set the second parameter to true for append mode
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(String.join(",", data));
            bufferedWriter.newLine(); // Add this line if you want to append a new line to the file
            bufferedWriter.close();
        } catch (IOException e) {
            // Exception handling
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Customer customer = new Customer("John Doe", "123 Main St", "555-555-5555", "johndoe@example.com", "01-01-1981");
        appendCustomerToCSV(customer);
    }
}