// need to add in validation check like does the account exist before transferring money or removing account etc

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {
    private String bankName;
    private List<Account> accounts;
    private List<String> accountNums;

    public Bank(String bankName){
        this.bankName = bankName;
        this.accounts = new ArrayList<>();
        this.accountNums = new ArrayList<>();
    }

    public static void main(String[] args) {
        Bank bank1 = new Bank("ABC Bank");
        // Create a customer to test
        Customer customer = new Customer(null, null, null, null, null, null, 0, bank1);
        // Make 3 accounts to test (there should be an X amount of pre-made accounts in the future csv)
        Account account1 = new Account("111", 50.00, 0);
        Account account2 = new Account("222", 50.00, 0);
        Account account3 = new Account("333", 50.00, 0);
        bank1.addAccount(account1);
        bank1.addAccount(account2);
        bank1.addAccount(account3);

        // When customer success login to their account, need to have their current loggedInAccount
        Account loggedInAccount = account1;

        // Check if account added
        // bank1.getAccountNumList();

        // Logged in user sees this menu
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Welcome to " + bank1.getBankName() + "!");
            System.out.println("1. Transfer Money");
            System.out.println("2. xxxxx");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter destination account number:");
                    String destinationAccount = scanner.next();
                    System.out.println("Enter amount to transfer ($): ");
                    double transferAmount = scanner.nextDouble();
                    System.out.println("Confirm transfer $" + transferAmount + " to " + destinationAccount + "? [y/n]");
                    String confirm = scanner.next();
                    if (confirm.equals("y")){
                        bank1.transferMoney(loggedInAccount.getAccountNum(), destinationAccount, transferAmount);
                        System.out.println("Your new balance: $" + loggedInAccount.getBalance());
                        System.out.println("Returning to menu...");

                        // Check if transfer successful (DELETE THIS afterwards)
                        // bank1.displayAccountInfo("111");
                        // bank1.displayAccountInfo("222");

                        break;
                    }else if (confirm.equals("n")){
                        System.out.println("Transfer cancelled! Returning to menu...");
                        break;
                    }

                case 2:
                    // TBD
                    break;

                case 3:
                    // TBD
                    // This needs to return to the register/login menu
                    // for now it will jus exit the app
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Returning to menu...");
            }
        }

    }

    public void transferMoney(String sourceAccountNum, String destinationAccountNum, double amount){
        Account sourceAccount = getAccountObj(sourceAccountNum);
        Account destinationAccount = getAccountObj(destinationAccountNum);

        if (sourceAccount.getBalance() < amount) {
            System.out.println("Insufficient balance to transfer funds.");
            return;
        }
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        sourceAccount.addHistory("Transfered $" + amount + " to Account Number: " + destinationAccount.getAccountNum());
        destinationAccount.addHistory ("Received $" + amount + " from Account Number: " + sourceAccount.getAccountNum());
        System.out.println("\nTransfer Successful!");
    }

    public Account getAccountObj(String accountNum){
        // need to check if account exists
        int accountIndex = accountNums.indexOf(accountNum);
        Account tempAccount = accounts.get(accountIndex);
        return tempAccount;
    }

    public List<String> getAccountNumList(){
        System.out.println(accountNums);
        return accountNums;
    }

    public void addAccount(Account account){
        accounts.add(account);
        accountNums.add(account.getAccountNum());
    }

    public void removeAccount(String accountNum){
        // need to check if account exists for it to be removed
        int accountIndex = accountNums.indexOf(accountNum);
        accounts.remove(accountIndex);
    }

    public void displayAccountInfo(String accountNum){
        Account tempAccount = getAccountObj(accountNum);
        tempAccount.displayAccountInfo();
    }

    public void setAccountTransferLimit(String accountNum, double newLimit){
        // TO-BE IMPLEMENTED
        // Take in from CLI when customer enter the input and pass it onto their account's setting

        // Account tempAccount = getAccount(accountNum);
        // tempAccount.setAccountTransferLimit():
    }


    public String getBankName(){
        return bankName;
    }

    
}
