import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// GUIDES :
/**------------------------------------------------------------------------------------*/
// In order to import javax :
// 1. download javax.mail.jar from https://github.com/javaee/javamail/releases
// 2. download activation.jar(1.1.1) from https://jar-download.com/artifact-search/activation [this page's second option]
// 3. create a lib directory under your project directory and store this 2 jar file into it 

// In order to use email 2FA :
// 1. create a new gmail
// 2. get the "app password" from google account
// 3. create a .env file under project directory 
// 4. put   email={gmail}
//          password={app_password}
// 5. use EnvLoader.java to get the env var to use in performTwoFactorAuthentication function

// When compiling
// 1. use "javac -cp ".;lib/* <example.java example2.java etc>" */

// when running 
// 1. use "java -cp ".;lib/* ATM.java"

/**------------------------------------------------------------------------------------*/

/**
 * Provides security features including hashing data, validating account numbers and PINs, 
 * managing PIN attempt counts, and handling two-factor authentication (2FA) via email.
 * <p>
 * This class includes both static and instance methods. Static methods offer utilities such as
 * hashing data with SHA-256, validating account numbers and PIN formats, and managing PIN attempt counts.
 * Instance methods focus on two-factor authentication, including sending a 2FA code to a user's email,
 * verifying the 2FA code, and managing 2FA attempts.
 * <p>
 * Usage involves creating an instance of the Security class, setting the user's email, and then
 * invoking methods to perform 2FA or to use the static utility methods as needed.
*/
public class Security {
    private static final int MAX_PIN_ATTEMPTS = 3;
    private static Map<String, Integer> pinAttemptCounts = new HashMap<>();
    private String userEmail;
    private int MAX_2FACODE_ATTEMPTS = 0;
    private boolean is2FASuccessful = false;
    private String verificationCode;

    /**
     * Constructs a new Security instance with a default, empty user email. The user email can be updated later using the setter method.
    */
    public Security() {
        this.userEmail = ""; // Default email, to be updated via setter
    }

    /**
     * Hashes the given data using the SHA-256 algorithm.
     *
     * @param data The string data to be hashed.
     * @return A hexadecimal string representation of the hashed data.
     * @throws RuntimeException if the SHA-256 algorithm is not available or if the character encoding is unsupported.
    */
    public static String hashDataWithSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates the format of an account number. The expected format is "1234-5678-9999".
     *
     * @param accountNumber The account number string to validate.
     * @return true if the account number matches the expected format, false otherwise.
    */
    public static boolean validateAccountNumber(String accountNumber) {
        return accountNumber.matches("\\d{4}-\\d{4}-\\d{4}");
    }

    /**
     * Validates the format of a PIN. The expected format is a 6-digit number.
     *
     * @param pin The PIN string to validate.
     * @return true if the PIN matches the expected format, false otherwise.
    */
    public static boolean validatePIN(String pin) {
        return pin.matches("\\d{6}");
    }

    /**
     * Increments the PIN attempt count for a given username and checks if the account should be locked based on the maximum number of allowed attempts.
     *
     * @param username The username for which the PIN attempt count is to be incremented.
     * @return true if the number of attempts has reached the maximum limit, false otherwise.
    */
    public static boolean incrementAndCheckPinAttempt(String username) {
        int attempts = pinAttemptCounts.getOrDefault(username, 0) + 1;
        pinAttemptCounts.put(username, attempts);
        return attempts >= MAX_PIN_ATTEMPTS;
    }

    /**
     * Resets the PIN attempt counter for a specific user.
     *
     * @param username The username for whom the PIN attempt count is to be reset.
    */
    public static void resetPinAttempt(String username) {
        pinAttemptCounts.remove(username);
    }

    /**
     * Simulates two-factor authentication (2FA) by sending a verification code to the user's email address.
     *
     * @return The verification code sent to the user, or null if the user's email is not provided.
     * @throws RuntimeException if there is an error sending the email.
    */
    public String performTwoFactorAuthentication() {
        EnvLoader.loadEnvVariables(".env");
        if (userEmail == null || userEmail.isEmpty()) {
            System.out.println("User email is not provided.");
            return null;
        }
        // Sender's email credentials
        final String from = System.getProperty("email"); // Replace with your email
        final String password = System.getProperty("password"); // Replace with your password

        // Recipient's email address
        String to = userEmail;

        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS

        // Create a session with authentication
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(from));

            // Set To: header field
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("2FA Code");

            // Generate a random 6-digit verification code
            this.verificationCode = generateVerificationCode();

            // Now set the actual message
            message.setText("Your 2FA verification code is: " + this.verificationCode);

            // Send message
            Transport.send(message);

            System.out.println("A 2FA code has been sent to your email (" + userEmail + "). Please enter the code to proceed:");
            // System.out.println("2FA code sent to email: " + userEmail);
            return verificationCode;
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies the input 2FA code against the generated verification code.
     *
     * @param inputCode The 2FA code provided by the user.
     * @return true if the input code matches the generated code and the attempt is within the allowed limit, false otherwise.
    */
    public boolean verifyTwoFactorAuthentication(String inputCode) {
        if (this.verificationCode.equals(inputCode)) {
            this.is2FASuccessful = true;
            this.MAX_2FACODE_ATTEMPTS = 0; // Reset MAX_2FACODE_ATTEMPTS for next time
            return true;
        } else {
            this.MAX_2FACODE_ATTEMPTS++;
            if (this.MAX_2FACODE_ATTEMPTS >= 3) {
                this.is2FASuccessful = false;
                return false; // Too many MAX_2FACODE_ATTEMPTS
            }
        }
        return false; // Incorrect code, but not yet locked out
    }

    /**
     * Retrieves the current number of 2FA code attempts.
     *
     * @return The number of 2FA code attempts.
    */
    public int getAttempts() {
        return MAX_2FACODE_ATTEMPTS;
    }

    /**
     * Checks if the two-factor authentication was successful.
     *
     * @return true if 2FA was successful, false otherwise.
    */
    public boolean getIs2FASuccessful() {
        return is2FASuccessful;
    }

    /**
     * Generates a random 6-digit verification code for two-factor authentication.
     *
     * @return A string representing the 6-digit verification code.
    */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Generate a random number between 100000 and 999999
        return String.valueOf(code);
    }

    /**
     * Gets the user's email address.
     *
     * @return The email address of the user.
    */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Sets the user's email address.
     *
     * @param userEmail The email address to be set for the user.
    */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Updates the lock status of a user in a CSV file. If locking, it adds a timestamp; if unlocking, it clears the lock timestamp.
     *
     * @param filePath The path to the CSV file containing user data.
     * @param username The username of the account to update.
     * @param isLocked The lock status to set for the user.
    */
    public static void updateIsLockedStatusToCSV(String filePath, String username, boolean isLocked) {
        File oldFile = new File(filePath);
        File newFile = new File("temp.csv");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(newFile, true)));
            Scanner scanner = new Scanner(oldFile);

            // Check and write the header first if present
            if (scanner.hasNextLine()) {
                String header = scanner.nextLine(); // Read the header
                pw.println(header); // Write the header to the new file
            }
    
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(",");
    
                // Assuming the CSV format is: username,password,name,isLocked
                if (tokens[0].equals(username)) {
                    // Update the isLocked status for the matching user
                    tokens[3] = String.valueOf(isLocked); // isLocked status

                    if (isLocked) {
                        // If locking the user, add/update the lock timestamp
                        tokens = Arrays.copyOf(tokens, 6); // Ensure the array has at least 6 elements
                        tokens[5] = LocalDateTime.now().format(formatter);
                    } else {
                        // If unlocking the user, clear the lock timestamp
                        if (tokens.length == 6) tokens[5] = "";
                    }

                    String updatedLine = String.join(",", Arrays.copyOf(tokens, tokens[5].isEmpty() ? 5 : 6));
                    pw.println(updatedLine);
                } else {
                    // Write other lines unchanged
                    pw.println(line);
                }
            }
    
            scanner.close();
            pw.flush();
            pw.close();
    
            // Replace the old file with the updated one
            if (!oldFile.delete()) {
                System.out.println("Could not delete the old file");
                return;
            }
            if (!newFile.renameTo(oldFile)) {
                System.out.println("Could not rename the new file");
            }
        } catch (Exception e) {
            System.out.println("Error updating isLocked status: " + e.getMessage());
        }
    }
}
