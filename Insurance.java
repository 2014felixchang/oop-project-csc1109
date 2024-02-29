import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Insurance {
    // The CoverageOption enum represents the different coverage options for an insurance policy.
    // Each option has a different value associated with it.
    public enum CoverageOption {
        BASIC(1000), STANDARD(2000), PREMIUM(3000);
        private final int value;
        // Constructor for CoverageOption enum.
        CoverageOption(int value){
            this.value=value;
        }
        // Getter method for the value of the coverage option.
        public int getValue(){
            return value;
        }
    }
    // The PolicyTenure enum represents the different policy tenures for an insurance policy.
    // Each tenure has a different number of years associated with it.
    public enum PolicyTenure {
        FIVE_YEARS(5), TEN_YEARS(10), FIFTEEN_YEARS(15), TWENTY_YEARS(20);
        private int years = 0;
        // Constructor for PolicyTenure enum.
        PolicyTenure(int year){
            this.years=year;
        }
         // Getter method for the number of years of the policy tenure.
        public int getYears(){
            return years;
        }
    }
    // The PremiumFrequency enum represents the different premium payment frequencies for an insurance policy.
    // Each frequency has a different number of months associated with it.
    public enum PremiumFrequency {
        MONTHLY(1), QUARTERLY(3), SEMI_ANNUALLY(6), ANNUALLY(12);
        private final int months;
        PremiumFrequency(int months){
            this.months=months;
        }
        public int getMonths(){
            return months;
        }
    }
    // The PolicyType enum represents the different types of insurance policies.
    public enum PolicyType {
        LIFE, HEALTH, ACCIDENT
    }
    // Instance variables for the Insurance class.
    private String policyNumber;
    private Instant policyStartDate;
    private Instant policyEndDate;
    private PolicyType policyType;
    private CoverageOption coverageOption;
    private PolicyTenure policyTenure;
    private PremiumFrequency premiumFrequency;

    // Constructor for the Insurance class.
    public Insurance(int policyTypeIndex, String startDateString, int coverageOptionIndex, int policyTenureIndex, int premiumFrequencyIndex) {
        try {
            // Assigning values to the instance variables based on the provided indices.
            this.policyType = PolicyType.values()[policyTypeIndex - 1];
            this.coverageOption = CoverageOption.values()[coverageOptionIndex - 1];
            this.policyTenure = PolicyTenure.values()[policyTenureIndex - 1];
            this.premiumFrequency = PremiumFrequency.values()[premiumFrequencyIndex - 1];
            this.policyNumber = generatePolicyNumber();
            
            // Parsing the start date string into a Date object.
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
            Date startDate = null;
            try {
                startDate = dateFormat.parse(startDateString);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter a date in the format yyyy-MM-dd HH:mm:ss.");
                startDate = new Date(); // Use the current date as a fallback
            }
            // Converting the start date to a ZonedDateTime object.
            ZonedDateTime startDateZoned = ZonedDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
            this.policyStartDate = startDate.toInstant();
            // Calculating the end date based on the policy tenure.
            ZonedDateTime endDateZoned = startDateZoned.plusYears(this.policyTenure.getYears());
            this.policyEndDate = endDateZoned.toInstant();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid policy type index. Please enter a number between 1 and " + PolicyType.values().length);
        }
    }
    
    // Method to generate a random policy number.
    private String generatePolicyNumber() {
        return UUID.randomUUID().toString();
    }
    // Getter and setter methods for the instance variables.
    public String getPolicyNumber() {
        return policyNumber;
    }

    public PolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    public CoverageOption getCoverageOption() {
        return coverageOption;
    }

    public void setCoverageOption(CoverageOption coverageOption) {
        this.coverageOption = coverageOption;
    }

    public PolicyTenure getPolicyTenure() {
        return policyTenure;
    }

    public void setPolicyTenure(PolicyTenure policyTenure) {
        this.policyTenure = policyTenure;
    }

    public PremiumFrequency getPremiumFrequency() {
        return premiumFrequency;
    }

    public void setPremiumFrequency(PremiumFrequency premiumFrequency) {
        this.premiumFrequency = premiumFrequency;
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
    // Method to check if the policy is currently active.
    public boolean isPolicyActive() {
        Instant currentDate = Instant.now();
        return currentDate.isAfter(policyStartDate) && currentDate.isBefore(policyEndDate);
    }
    // Method to calculate the premium for the policy.
    public double[][] calculatePremium() {
        int basePremium = coverageOption.getValue();
        int frequencyFactor = premiumFrequency.getMonths();
        int tenureFactor = policyTenure.getYears();

        double premiumPerPeriod = (double) basePremium / frequencyFactor;
        double totalPremium = premiumPerPeriod * tenureFactor;

        double gstRate = 0.09;
        double gstForPremiumPerPeriod = premiumPerPeriod * gstRate;
        double gstForTotalPremium = totalPremium * gstRate;

        double premiumPerPeriodWithGST = premiumPerPeriod + gstForPremiumPerPeriod;
        double totalPremiumWithGST = totalPremium + gstForTotalPremium;

        premiumPerPeriod = Math.round(premiumPerPeriod * 100.0) / 100.0;
        totalPremium = Math.round(totalPremium * 100.0) / 100.0;
        premiumPerPeriodWithGST = Math.round(premiumPerPeriodWithGST * 100.0) / 100.0;
        totalPremiumWithGST = Math.round(totalPremiumWithGST * 100.0) / 100.0;

        return new double[][] {
            {premiumPerPeriod, totalPremium},
            {premiumPerPeriodWithGST, totalPremiumWithGST}
        };
    }
    // Method to display the details of the policy.
    public void displayPolicyDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss");
        formatter = formatter.withZone(ZoneId.of("Asia/Singapore"));

        double[][] premiums = calculatePremium();

        System.out.println("Policy Number: " + policyNumber);
        System.out.println("Policy Type: " + policyType);
        System.out.println("Coverage Option: " + coverageOption);
        System.out.println("Policy Tenure: " + policyTenure);
        System.out.println("Premium Frequency: " + premiumFrequency);
        System.out.println("Policy Start Date: " + formatter.format(ZonedDateTime.ofInstant(policyStartDate, ZoneId.of("Asia/Singapore"))));
        System.out.println("Policy End Date: " + formatter.format(ZonedDateTime.ofInstant(policyEndDate, ZoneId.of("Asia/Singapore"))));
        System.out.println("Premium per period (before GST): $" + premiums[0][0]);
        System.out.println("Premium per period (after GST): $" + premiums[1][0]);
        System.out.println("Total premium (before GST): $" + premiums[0][1]);
        System.out.println("Total premium (after GST): $" + premiums[1][1]);
    }
}