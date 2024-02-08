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
        Account account1 = new Account("111", 50.00, 0);
        Account account2 = new Account("222", 50.00, 0);
        Account account3 = new Account("333", 50.00, 0);
        bank1.addAccount(account1);
        bank1.addAccount(account2);
        bank1.addAccount(account3);

        // Check if account added
        bank1.getAccountNums();

        bank1.transferMoney(account1, account2, 10.00);

        // Check if transfer successful
        bank1.displayAccountInfo("111");
        bank1.displayAccountInfo("222");
    }

    public void transferMoney(Account sourceAccount, Account destinationAccount, double amount){
        if (Double.parseDouble(sourceAccount.getBalance()) < amount) {
            System.out.println("Insufficient balance to transfer funds.");
            return;
        }
        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);
        // (Waiting for account to make addHistory())
        // sourceAccount.addHistory("Transfered $" + amount + " to Account Number: " + destinationAccount.getAccountNum());
        // destinationAccount.addHistory ("Received $" + amount + " from Account Number: " + sourceAccount.getAccountNum());
    }

    public List<String> getAccountNums(){
        System.out.println(accountNums);
        return accountNums;
    }

    public void addAccount(Account account){
        accounts.add(account);
        accountNums.add(account.getAccountNum());
    }

    public void removeAccount(){
        
    }

    public void displayAccountInfo(String accountNum){
        int accountIndex = accountNums.indexOf(accountNum);
        Account tempAccount = accounts.get(accountIndex);
        tempAccount.displayAccountInfo();
    }


    public String getBankName(){
        return bankName;
    }
}
