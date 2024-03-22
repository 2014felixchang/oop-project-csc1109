import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Handles reading and writing information to and from file
 */
public class CSVHandler {

    /**
     * Appends a new record to the csv file specified
     * 
     * @param filepath the csv file name
     * @param newRecord the new record
     */
    public static void addRecord(String filepath, String newRecord) {
        try (
            BufferedWriter bW = new BufferedWriter(new FileWriter(filepath, true));
        ) {
            // Write the customer's details to the file, separated by commas
            bW.write(newRecord);
            // Add a new line to the file
            bW.newLine();
        } 
        catch (IOException e) {
            // Handle any exceptions that occur
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Returns an existing Customer object given a username. If username does not exist in file CustomerInfo.csv, then return null.
     * 
     * @param username the username of the customer
     * @return the customer object
     */
    public static Customer retrieveCustomer(String username) {
        try (BufferedReader bR = new BufferedReader(new FileReader("CustomerInfo.csv"))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(username)) {
                    Customer customer = new Customer(accountData[0], accountData[1], accountData[2], accountData[3]);
                    // Set failed attempts and locked status
                    customer.setFailedAttempts(Integer.parseInt(accountData[4]));
                    customer.setLocked("true".equals(accountData[5]));
                    return customer;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Finds and returns the record that has a first column value equal to key in the CSV file
     * 
     * @param key the identifier for a record
     * @param filepath the CSV file name
     * @return
     */
    public static String getRecord(String key, String filepath) {
        try (BufferedReader bR = new BufferedReader(new FileReader(filepath))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(key)) {
                    return currentLine;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Finds the record in the file with the same first column value as the key, and updates that record
     * 
     * @param key the identifier for a record
     * @param filepath the CSV file name
     * @param newRecord the new string of info to be updated
     */
    public static void updateCSV(String key, String filepath, String newRecord) {
        String currentLine;
        try (
            BufferedReader bR = new BufferedReader(new FileReader(filepath)); 
            BufferedWriter bW = new BufferedWriter(new FileWriter("temp.csv", false))
            ) 
        {
            while ((currentLine = bR.readLine()) != null) {
                String data[] = currentLine.split(",");
                if (data[0].equals(key) == false) {
                    bW.write(currentLine, 0, currentLine.length());
                    bW.newLine();
                }
                else {
                    bW.write(newRecord, 0, newRecord.length());
                    bW.newLine();
                }
            }
        } 
        catch (IOException e) {
            System.err.println(e);
        }
        try {
            Path accPath = Paths.get(filepath);
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

    /**
     * Removes a record that has a first column value equal to key from the csv file
     * 
     * @param key the identifier for a record
     * @param filepath the CSV file name
     */
    public static void removeRecord(String key, String filepath) {
        String currentLine;
        try (
            BufferedReader bR = new BufferedReader(new FileReader(filepath));
            BufferedWriter bW = new BufferedWriter(new FileWriter("temp.csv", false))
        ) {
            while ((currentLine = bR.readLine()) != null) {
                String data[] = currentLine.split(",");
                // If the username in a line doesn't match the given username, write the line to the temp file
                boolean matchFound = Arrays.stream(data).anyMatch(key::equals);
                if (!matchFound) {
                    bW.write(currentLine, 0, currentLine.length());
                    bW.newLine();
                }
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }
        try {
            Path givenPath = Paths.get(filepath);
            Path tempPath = Paths.get("temp.csv");
            // delete old file and rename temp file to CustomerInfo.csv
            Files.delete(givenPath);
            Files.move(tempPath, givenPath);
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
    
    /**
     * Appends a customer's personal details to CustomerDetails.csv
     * 
     * @param username
     * @param name
     * @param address
     * @param phoneNumber
     * @param email
     * @param dateOfBirth
     */
    public static void addCustomerDetailsToCSV(String username, String name, String address, String phoneNumber, String email, String dateOfBirth) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("CustomerDetails.csv", true))) {
            String record = String.join(",", username, name, address, phoneNumber, email, dateOfBirth);
            writer.write(record);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to CustomerDetails.csv: " + e.getMessage());
        }
    }
}