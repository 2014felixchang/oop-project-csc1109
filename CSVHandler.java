import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVHandler {

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
            custAccWriter.write(data[0]);
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
    
    // Returns a customer object given a username
    public static Customer retrieveCustomer(String username) {
        try (BufferedReader bR = new BufferedReader(new FileReader("CustomerInfo.csv"))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(username) == true) {
                    Customer customer = new Customer(accountData[0], accountData[1], accountData[2], accountData[3]);
                    return customer;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    // Append new account to CSV
    public static void addAccountToCSV(Account account) {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter("Accounts.csv", true))){
            bW.write(account.convertToCSV());
            bW.newLine();
        }   
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Retrieve account, if it exists in file, else return null
    public static String getAccountFromCSV(String accountNum) {
        try (BufferedReader bR = new BufferedReader(new FileReader("Accounts.csv"))){
            String currentLine;
            while ((currentLine = bR.readLine()) != null) {
                String accountData[] = currentLine.split(",");
                if (accountData[0].equals(accountNum) == true) {
                    return currentLine;
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
    
    // Returns string of a customer's accounts' numbers
    public static String getCustAccsFromCSV(String username) {
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

    // General function to update a CSV given a filepath, new record and a key (string)
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

    // public static void updateAccountRecord(Account account) {
    //     String currentLine;
    //     String accountInfo = account.convertToCSV();
    //     try (
    //         BufferedReader bR = new BufferedReader(new FileReader("Accounts.csv")); 
    //         BufferedWriter bW = new BufferedWriter(new FileWriter("temp.csv", false))
    //         ) 
    //     {
    //         while ((currentLine = bR.readLine()) != null) {
    //             String accountData[] = currentLine.split(",");
    //             if (accountData[0].equals(account.getAccountNum()) == false) {
    //                 // if current line is NOT the account to update, write line to temp file
    //                 bW.write(currentLine, 0, currentLine.length());
    //                 bW.newLine();
    //             }
    //             else {
    //                 // else write new account info to temp file
    //                 bW.write(accountInfo, 0, accountInfo.length());
    //                 bW.newLine();
    //             }
    //         }
    //     } 
    //     catch (IOException e) {
    //         System.err.println(e);
    //     }
    //     try {
    //         Path accPath = Paths.get("Accounts.csv");
    //         Path tempPath = Paths.get("temp.csv");
    //         // delete old file and rename temp file to accounts.csvs
    //         Files.delete(accPath);
    //         Files.move(tempPath, accPath);
    //         // File dump = new File("Accounts.csv");
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //     }
    // }

    // public void updateCustomerAccounts(String newAccNum, Customer customer) {
    //     String currentLine;
    //     String newAccInfo = customer.getCustomerAccounts() + "," + newAccNum;
    //     try (
    //         BufferedReader bR = new BufferedReader(new FileReader("CustomerAccounts.csv")); 
    //         BufferedWriter bW = new BufferedWriter(new FileWriter("temp.csv", false))
    //         ) 
    //     {
    //         while ((currentLine = bR.readLine()) != null) {
    //             String accountData[] = currentLine.split(",");
    //             if (accountData[0].equals(customer.getUsername()) == false) {
    //                 bW.write(currentLine, 0, currentLine.length());
    //                 bW.newLine();
    //             }
    //             else {
    //                 bW.write(newAccInfo, 0, newAccInfo.length());
    //                 bW.newLine();
    //             }
    //         }
    //     } 
    //     catch (IOException e) {
    //         System.err.println(e);
    //     }
    //     try {
    //         Path accPath = Paths.get("CustomerAccounts.csv");
    //         Path tempPath = Paths.get("temp.csv");
    //         // delete old file and rename temp file to accounts.csvs
    //         Files.delete(accPath);
    //         Files.move(tempPath, accPath);
    //         // File dump = new File("Accounts.csv");
    //     }
    //     catch (IOException e) {
    //         System.out.println(e);
    //     }
    // }
}