import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
public class Insurance {
    public enum CoverageOption {
        BASIC(1000), STANDARD(2000), PREMIUM(3000);
        private final int value;
        CoverageOption(int value){
            this.value=value;
        }
        public int getValue(){
            return value;
        }
    }
    public enum PolicyTenure {
        FIVE_YEARS(5), TEN_YEARS(10), FIFTEEN_YEARS(15), TWENTY_YEARS(20);
        private int years = 0;
        PolicyTenure(int year){
            this.years=year;
        }
        public int getYears(){
            return years;
        }
    }
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
    public enum PolicyType {
        LIFE, HEALTH, ACCIDENT
    }
    private String policyNumber;
    private Instant policyStartDate;
    private Instant policyEndDate;
    private PolicyType policyType;
    private CoverageOption coverageOption;
    private PolicyTenure policyTenure;
    private PremiumFrequency premiumFrequency;

    public Insurance(int policyTypeIndex, String startDateString, String endDateString, int coverageOptionIndex, int policyTenureIndex, int premiumFrequencyIndex) {
        try {
            this.policyType = PolicyType.values()[policyTypeIndex - 1];
            this.coverageOption = CoverageOption.values()[coverageOptionIndex - 1];
            this.policyTenure = PolicyTenure.values()[policyTenureIndex - 1];
            this.premiumFrequency = PremiumFrequency.values()[premiumFrequencyIndex - 1];
            this.policyNumber = generatePolicyNumber();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = dateFormat.parse(startDateString);
                // Parse the end date string into a Date object
                endDate = dateFormat.parse(endDateString);
            } catch (ParseException e) {
                // If the date string is not in the correct format, print an error message and return
                System.out.println("Invalid date format. Please enter a date in the format yyyy-MM-dd.");
                return;
            }
            // Get the current time    
            LocalTime currentTime = LocalTime.now();
            // Convert the start date to an Instant, then to a LocalDateTime, then to a LocalDate, and finally back to an Instant
            // The current time is used as the time part of the date    
            this.policyStartDate = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault())
                    .toLocalDate()
                    .atTime(currentTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
            // Do the same for the end date
            this.policyEndDate = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault())
                    .toLocalDate()
                    .atTime(currentTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant();
        } catch (ArrayIndexOutOfBoundsException e) {
            // If an invalid index was provided, print an error message
            System.out.println("Invalid policy type index. Please enter a number between 1 and " + PolicyType.values().length);
        }
    }
    // Generate a random policy number
    private String generatePolicyNumber() {
        return UUID.randomUUID().toString();
    }
    // Getter for the policy number
    public String getPolicyNumber() {
        return policyNumber;
    }
    // Getter for the policy type
    public PolicyType getPolicyType() {
        return policyType;
    }
    // Setter for the policy type
    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }
    // Getter for the coverage option
    public CoverageOption getCoverageOption() {
        return coverageOption;
    }
    // Setter for the coverage option
public void setCoverageOption(CoverageOption coverageOption) {
    this.coverageOption = coverageOption;
}

// Getter for the policy tenure
public PolicyTenure getPolicyTenure() {
    return policyTenure;
}

// Setter for the policy tenure
public void setPolicyTenure(PolicyTenure policyTenure) {
    this.policyTenure = policyTenure;
}

// Getter for the premium frequency
public PremiumFrequency getPremiumFrequency() {
    return premiumFrequency;
}

// Setter for the premium frequency
public void setPremiumFrequency(PremiumFrequency premiumFrequency) {
    this.premiumFrequency = premiumFrequency;
}

// Getter for the policy start date
public Instant getPolicyStartDate() {
    return policyStartDate;
}

// Setter for the policy start date
public void setPolicyStartDate(Instant policyStartDate) {
    this.policyStartDate = policyStartDate;
}

// Getter for the policy end date
public Instant getPolicyEndDate() {
    return policyEndDate;
}

// Setter for the policy end date
public void setPolicyEndDate(Instant policyEndDate) {
    this.policyEndDate = policyEndDate;
}

// Method to check if the policy is active
public boolean isPolicyActive() {
    Instant currentDate = Instant.now();
    return currentDate.isAfter(policyStartDate) && currentDate.isBefore(policyEndDate);
}

// Method to calculate the premium
public double[][] calculatePremium() {
    // Calculate the base premium, frequency factor, and tenure factor
    int basePremium = coverageOption.getValue();
    int frequencyFactor = premiumFrequency.getMonths();
    int tenureFactor = policyTenure.getYears();

    // Calculate the premium per period and total premium
    double premiumPerPeriod = (double) basePremium / frequencyFactor;
    double totalPremium = premiumPerPeriod * tenureFactor;

    // Calculate the 9% GST for the premium per period and total premium
    double gstRate = 0.09;
    double gstForPremiumPerPeriod = premiumPerPeriod * gstRate;
    double gstForTotalPremium = totalPremium * gstRate;

    // Add the GST to the original premium amounts
    double premiumPerPeriodWithGST = premiumPerPeriod + gstForPremiumPerPeriod;
    double totalPremiumWithGST = totalPremium + gstForTotalPremium;

    // Round the premium amounts to 2 decimal places
    premiumPerPeriod = Math.round(premiumPerPeriod * 100.0) / 100.0;
    totalPremium = Math.round(totalPremium * 100.0) / 100.0;
    premiumPerPeriodWithGST = Math.round(premiumPerPeriodWithGST * 100.0) / 100.0;
    totalPremiumWithGST = Math.round(totalPremiumWithGST * 100.0) / 100.0;

    // Return the premium amounts in a 2D array
    return new double[][] {
        {premiumPerPeriod, totalPremium},
        {premiumPerPeriodWithGST, totalPremiumWithGST}
    };
}

// Method to display the policy details
public void displayPolicyDetails() {
    // Create a formatter for the date and time
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd HH:mm:ss");
    formatter = formatter.withZone(ZoneId.of("Asia/Singapore"));

    // Calculate the premiums
    double[][] premiums = calculatePremium();

    // Print the policy details
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