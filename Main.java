import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("**** Welcome to SIT Bank ****");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("4. Exit App");
            System.out.println("***********************************");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Handle login
                    break;
                case 2:
                    // Handle registration
                    break;
                case 4:
                    System.out.println("Exiting the application...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }
}