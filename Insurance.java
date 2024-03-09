import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * The Insurance class represents an insurance policy.
 * It contains several enums to represent different aspects of the policy.
 */
public class Insurance {

    /**
     * The CoverageOption enum represents the different coverage options for an insurance policy.
     * Each option has a different value associated with it.
     */
    public enum CoverageOption {
        /**
         * Basic coverage option with a value of 1000.
         */
        BASIC(1000),

        /**
         * Standard coverage option with a value of 2000.
         */
        STANDARD(2000),

        /**
         * Premium coverage option with a value of 3000.
         */
        PREMIUM(3000);

        private final int value;

        /**
         * Constructor for CoverageOption enum.
         * @param value the value of the coverage option.
         */
        CoverageOption(int value){
            this.value=value;
        }

        /**
         * Getter method for the value of the coverage option.
         * @return the value of the coverage option.
         */
        public int getValue(){
            return value;
        }
    }

    /**
     * The PolicyTenure enum represents the different policy tenures for an insurance policy.
     * Each tenure has a different number of years associated with it.
     */
    public enum PolicyTenure {
        /**
         * Five years policy tenure.
         */
        FIVE_YEARS(5),

        /**
         * Ten years policy tenure.
         */
        TEN_YEARS(10),

        /**
         * Fifteen years policy tenure.
         */
        FIFTEEN_YEARS(15),

        /**
         * Twenty years policy tenure.
         */
        TWENTY_YEARS(20);

        private final int years;

        /**
         * Constructor for PolicyTenure enum.
         * @param years the number of years of the policy tenure.
         */
        PolicyTenure(int years){
            this.years = years;
        }

        /**
         * Getter method for the number of years of the policy tenure.
         * @return the number of years of the policy tenure.
         */
        public int getYears(){
            return years;
        }
    }

    /**
     * The PremiumFrequency enum represents the different premium payment frequencies for an insurance policy.
     * Each frequency has a different number of months associated with it.
     */
    public enum PremiumFrequency {
        /**
         * Monthly premium payment frequency.
         */
        MONTHLY(1),

        /**
         * Quarterly premium payment frequency.
         */
        QUARTERLY(3),

        /**
         * Semi-annually premium payment frequency.
         */
        SEMI_ANNUALLY(6),

        /**
         * Annually premium payment frequency.
         */
        ANNUALLY(12);

        private final int months;

        /**
         * Constructor for PremiumFrequency enum.
         * @param months the number of months of the premium payment frequency.
         */
        PremiumFrequency(int months){
            this.months=months;
        }

        /**
         * Getter method for the number of months of the premium payment frequency.
         * @return the number of months of the premium payment frequency.
         */
        public int getMonths(){
            return months;
        }
    }

    /**
     * The PolicyType enum represents the different types of insurance policies.
     */
    public enum PolicyType {
        /**
         * Life insurance policy.
         */
        LIFE,

        /**
         * Health insurance policy.
         */
        HEALTH,

        /**
         * Accident insurance policy.
         */
        ACCIDENT
    }


    // Instance variables for the Insurance class.
    private String policyNumber;
    private Instant policyStartDate;
    private Instant policyEndDate;
    private PolicyType policyType;
    private CoverageOption coverageOption;
    private PolicyTenure policyTenure;
    private PremiumFrequency premiumFrequency;

    /**
     * Constructor for the Insurance class.
     * @param policyTypeIndex the index of the policy type.
     * @param startDateString the start date of the policy.
     * @param coverageOptionIndex the index of the coverage option.
     * @param policyTenureIndex the index of the policy tenure.
     * @param premiumFrequencyIndex the index of the premium payment frequency.
     */
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

    /**
     * Method to generate a random policy number.
     * @return a random policy number.
     */
    private String generatePolicyNumber() {
        return UUID.randomUUID().toString();
    }

    /**
     * Getter method for the policy number.
     * @return the policy number.
     */
    public String getPolicyNumber() {
        return policyNumber;
    }

    /**
     * Getter method for the policy type.
     * @return the policy type.
     */
    public PolicyType getPolicyType() {
        return policyType;
    }

    /**
     * Setter method for the policy type.
     * @param policyType the policy type.
     */
    public void setPolicyType(PolicyType policyType) {
        this.policyType = policyType;
    }

    /**
     * Getter method for the coverage option.
     * @return the coverage option.
     */
    public CoverageOption getCoverageOption() {
        return coverageOption;
    }

    /**
     * Setter method for the coverage option.
     * @param coverageOption the coverage option.
     */
    public void setCoverageOption(CoverageOption coverageOption) {
        this.coverageOption = coverageOption;
    }

    /**
     * Getter method for the policy tenure.
     * @return the policy tenure.
     */
    public PolicyTenure getPolicyTenure() {
        return policyTenure;
    }

    /**
     * Setter method for the policy tenure.
     * @param policyTenure the policy tenure.
     */
    public void setPolicyTenure(PolicyTenure policyTenure) {
        this.policyTenure = policyTenure;
    }

    /**
     * Getter method for the premium payment frequency.
     * @return the premium payment frequency.
     */
    public PremiumFrequency getPremiumFrequency() {
        return premiumFrequency;
    }

    /**
     * Setter method for the premium payment frequency.
     * @param premiumFrequency the premium payment frequency.
     */
    public void setPremiumFrequency(PremiumFrequency premiumFrequency) {
        this.premiumFrequency = premiumFrequency;
    }

    /**
     * Getter method for the policy start date.
     * @return the policy start date.
     */
    public Instant getPolicyStartDate() {
        return policyStartDate;
    }

    /**
     * Setter method for the policy start date.
     * @param policyStartDate the policy start date.
     */
    public void setPolicyStartDate(Instant policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    /**
     * Getter method for the policy end date.
     * @return the policy end date.
     */
    public Instant getPolicyEndDate() {
        return policyEndDate;
    }

    /**
     * Setter method for the policy end date.
     * @param policyEndDate the policy end date.
     */
    public void setPolicyEndDate(Instant policyEndDate) {
        this.policyEndDate = policyEndDate;
    }

        /**
     * Method to check if the policy is currently active.
     * @return true if the policy is active, false otherwise.
     */
    public boolean isPolicyActive() {
        Instant currentDate = Instant.now();
        return currentDate.isAfter(policyStartDate) && currentDate.isBefore(policyEndDate);
    }

    /**
     * Method to calculate the premium for the policy.
     * @return a 2D array with the premium per period and total premium, both before and after GST.
     */
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

    /**
     * Method to display the details of the policy. It prints the policy number, type, 
     * coverage option, tenure, premium frequency, start and end dates, and the premium 
     * per period and total premium both before and after GST.
     */
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