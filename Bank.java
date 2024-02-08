// need to add in validation check like does the account exist before transferring money or removing account etc

import java.util.ArrayList;
import java.util.List;

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

        // Make 3 accounts to test
        Account account1 = new Account("111", 50.00, 0);
        Account account2 = new Account("222", 50.00, 0);
        Account account3 = new Account("333", 50.00, 0);
        bank1.addAccount(account1);
        bank1.addAccount(account2);
        bank1.addAccount(account3);

        // When customer success login to their account, need to have their current loggedInAccount
        Account loggedInAccount = account1;

        // Check if account added
        bank1.getAccountNumList();

        bank1.transferMoney(loggedInAccount.getAccountNum(), "222", 10.00);

        // Check if transfer successful
        bank1.displayAccountInfo("111");
        bank1.displayAccountInfo("222");
    }

    public void transferMoney(String sourceAccountNum, String destinationAccountNum, double amount){
        Account sourceAccount = getAccountObj(sourceAccountNum);
        Account destinationAccount = getAccountObj(destinationAccountNum);

        if (Double.parseDouble(sourceAccount.getBalance()) < amount) {
            System.out.println("Insufficient balance to transfer funds.");
            return;
        }
        sourceAccount.setBalance( (Double.parseDouble(sourceAccount.getBalance())) - amount);
        destinationAccount.setBalance( (Double.parseDouble(destinationAccount.getBalance())) + amount);
        sourceAccount.addHistory("Transfered $" + amount + " to Account Number: " + destinationAccount.getAccountNum());
        destinationAccount.addHistory ("Received $" + amount + " from Account Number: " + sourceAccount.getAccountNum());
        // (Waiting for account to make addHistory())
        // sourceAccount.addHistory("Transfered $" + amount + " to Account Number: " + destinationAccount.getAccountNum());
        // destinationAccount.addHistory ("Received $" + amount + " from Account Number: " + sourceAccount.getAccountNum());
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
