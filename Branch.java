/**
 * Branch class:
 * - Represents the bank's retail branches (branch id, branch name, address, opening time, closing time)
 * - For use in the CLI, to display information of all branches for customers who need services which require a bank employee at a branch.
 */
public class Branch {
    private int branchID;
    private String branchName;
    private String address;
    private int openingTime;
    private int closingTime;
    
    /**
     * Constructor to create new branch objects
     * 
     * @param branchID
     * @param branchName
     * @param address
     * @param openingTime
     * @param closingTime
     */
    public Branch(int branchID, String branchName, String address, int openingTime, int closingTime) {
        this.branchID = branchID;
        this.branchName = branchName;
        this.address = address;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public int getBranchID() {
        return this.branchID;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public String getBranchAddress() {
        return this.address;
    }

    public int getOpeningTime() {
        return this.openingTime;
    }

    public int getClosingTime() {
        return this.closingTime;
    }

    public void setBranchName(String newName) {
        this.branchName = newName;
    }

    public void setBranchID(int newID) {
        this.branchID = newID;
    }

    public void setBranchAddress(String newAddress) {
        this.address = newAddress;
    }

    public void setOpeningTime(int newTime) {
        this.openingTime = newTime;
    }

    public void setClosingTime(int newTime) {
        this.closingTime = newTime;
    }

    public String convertToCSV() {
        return branchID+","+branchName+","+address+","+openingTime+","+closingTime;
    }
}
