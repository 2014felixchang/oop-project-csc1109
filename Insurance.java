import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Insurance {
    private String policyNumber;
    private double coverageAmount;
    private double premium;
    private Instant policyStartDate;
    private Instant policyEndDate;
    private String policyType;

    public Insurance(String policyNumber, double coverageAmount, double premium, String policyType) {
        this.policyNumber = policyNumber;
        this.coverageAmount = coverageAmount;
        this.premium = premium;
        this.policyType = policyType;
        this.policyStartDate = Instant.now(); // Set start date to current date and time
        this.policyEndDate = Instant.now(); // Set end date to current date and time
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public Instant getPolicyStartDate() {
        return policyStartDate;
    }

    public void setPolicyStartDate(Instant policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public Instant getPolicyEndDate() {
        return policyEndDate;
    }

    public void setPolicyEndDate(Instant policyEndDate) {
        this.policyEndDate = policyEndDate;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public boolean isPolicyActive() {
        Instant currentDate = Instant.now();
        return currentDate.isAfter(policyStartDate) && currentDate.isBefore(policyEndDate);
    }

    public double calculatePremium() {
        // Add your logic to calculate the premium
        return 0.0;
    }

    public void displayPolicyDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss");
        formatter = formatter.withZone(ZoneId.of("Asia/Singapore"));
        System.out.println("Policy Number: " + policyNumber);
        System.out.println("Coverage Amount: " + coverageAmount);
        System.out.println("Premium: " + premium);
        System.out.println("Policy Start Date: " + formatter.format(ZonedDateTime.ofInstant(policyStartDate, ZoneId.of("Asia/Singapore"))));
        System.out.println("Policy End Date: " + formatter.format(ZonedDateTime.ofInstant(policyEndDate, ZoneId.of("Asia/Singapore"))));
        System.out.println("Policy Type: " + policyType);
    }
}