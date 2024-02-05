/*
 * Account class:
 * - account info handling (display & manage acc info like acc numbers and types)
 * - balance management (check balance, show current, remaining, available)
 * - account operations (deposit, withdraw)
 */

public class Account {
    private int numOfAccount;
    private int[] accounts;
    private float[] accBalances;
    
    // Pull customer's bank accounts information from file and initialize attributes with information
    public Account() {}
    
    // find index of given accNum in accounts array, minus amount from accBalances array at the same index
    public void withdraw(int accNum, float amount) {}

    // find index of given accNum in accounts array, add amount to accBalances array at the same index
    public void deposit(int accNum, float amount) {}
}
