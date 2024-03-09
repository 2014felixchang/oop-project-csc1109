import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A utility class for loading environment variables from a file into the system properties.
 * <p>
 * This class provides a static method to read key-value pairs from a specified file,
 * where each line contains an environment variable and its value separated by an '=' character.
 * The method trims any leading or trailing whitespace from the keys and values before setting them
 * as system properties.
*/
public class EnvLoader {

    /**
     * Loads environment variables from a file and sets them as system properties.
     * Each line in the file should be in the format "KEY=VALUE". Lines that do not conform to this format
     * will be ignored. This method trims whitespace from both the keys and values before setting them.
     * <p>
     * If the file cannot be read (e.g., if it does not exist or is not accessible), the method
     * will catch the IOException, print the stack trace, and continue without setting any properties.
     *
     * @param filePath The path to the file containing the environment variables to load.
     *                 This should be a valid path to a readable file.
    */
    public static void loadEnvVariables(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    System.setProperty(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
