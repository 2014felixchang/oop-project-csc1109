import java.time.LocalTime;

/**
 * Branch class:
 * Represents the bank's retail branches (branch id, branch name, address, opening time, closing time).
 * For use in the CLI, to display information of all branches for customers who need services which require a bank employee at a branch.
 */
public class Branch {
    private int branchID;
    private String branchName;
    private String address;
    private LocalTime openingTime;
    private LocalTime closingTime;
    
    /**
     * Constructor to create new branch objects
     * 
     * @param branchID The unique ID for a branch
     * @param branchName The unique name for a branch, like "Ang Mo Kio Branch"
     * @param address The address of the branch
     * @param openingTime The time that the branch opens
     * @param closingTime The time that the branch closes
     */
    public Branch(int branchID, String branchName, String address, LocalTime openingTime, LocalTime closingTime) {
        try {
            this.branchID = branchID;
            this.branchName = branchName;
            this.address = address;
            this.openingTime = openingTime;
            this.closingTime = closingTime;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid parameters given");
        }
    }

    /**
     * Getter method to get branch ID
     * @return the branch ID
     */
    public int getBranchID() {
        return this.branchID;
    }

    /**
     * Getter method to get branch name
     * @return the branch name
     */
    public String getBranchName() {
        return this.branchName;
    }

    /**
     * Getter method to get branch address
     * @return the branch address
     */
    public String getBranchAddress() {
        return this.address;
    }

    /**
     * Getter method to get branch opening time
     * @return the branch opening time
     */
    public LocalTime getOpeningTime() {
        return this.openingTime;
    }

    /**
     * Getter method to get branch closing time
     * @return the branch closing time
     */
    public LocalTime getClosingTime() {
        return this.closingTime;
    }

    /**
     * Setter method to set branch name
     * @param newName the new name that will be given to the branch
     */
    public void setBranchName(String newName) {
        this.branchName = newName;
    }

    /**
     * Setter method to set branch ID
     * @param newID the new ID that will be given to the branch
     */
    public void setBranchID(int newID) {
        this.branchID = newID;
    }

    /**
     * Setter method to set branch address
     * @param newAddress the new address that will be given to the branch
     */
    public void setBranchAddress(String newAddress) {
        this.address = newAddress;
    }

    /**
     * Setter method to set branch opening time
     * @param newTime the new timing that the branch will open at
     */
    public void setOpeningTime(LocalTime newTime) {
        this.openingTime = newTime;
    }

    /**
     * Setter method to set branch closing time
     * @param newTime the new timing that the branch will close at
     */
    public void setClosingTime(LocalTime newTime) {
        this.closingTime = newTime;
    }

    /**
     * Converter method to convert the branch's attributes into comma-separated values
     * @return a string of comma-separated values that are the branch's attributes
     */
    public String convertToCSV() {
        return branchID+","+branchName+","+address+","+openingTime+","+closingTime;
    }
    
    /**
     * Prints branch object's information, such as the name, address, opening and closing time.
     */
    public void printBranchInfo() {
        System.out.println("Branch name: " + this.getBranchName());
        System.out.println("Address: " + this.getBranchAddress());
        System.out.println("Opening time: " + this.getOpeningTime());
        System.out.println("Closing time: " + this.getClosingTime());
    }
}

