
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

/**
 * The PasswordHasher class handles the hashing of the customer's password using SHA-256
 */
public class PasswordHasher {
    /**
     * Method to hash a password using SHA-256
     * @param password The plaintext password string
     * @return Returns the hashed password
     */
    public static String hashPassword(String password) {
        try {
            // Get an instance of MessageDigest that implements SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Digest the password bytes and get the hash
            byte[] messageDigest = md.digest(password.getBytes());
            
            // Convert the hash bytes into a BigInteger
            BigInteger no = new BigInteger(1, messageDigest);
            
            // Convert the BigInteger to a hexadecimal string
            String hashtext = no.toString(16);
            
            // Prepend zeros to the hexadecimal string until it is 32 characters long
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            
            // Return the hashed password
            return hashtext;
        } 
        catch (NoSuchAlgorithmException e) {
            // If the algorithm doesn't exist, throw a RuntimeException
            throw new RuntimeException(e);
        }
    }
}