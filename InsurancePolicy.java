import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.text.DecimalFormat;

public abstract class InsurancePolicy {

    private String policyNumber;
    private LocalDate policyStartDate;
    private LocalDate policyEndDate;
    private CoverageOption coverageOption;
    private PolicyTenure policyTenure;
    private PremiumFrequency premiumFrequency;
    private int age;

    public InsurancePolicy(String startDateString, CoverageOption coverageOption, PolicyTenure policyTenure,
                           PremiumFrequency premiumFrequency, int age) throws PolicyException {
        this.coverageOption = coverageOption;
        this.policyTenure = policyTenure;
        this.premiumFrequency = premiumFrequency;
        this.age = age;
        this.policyNumber = generatePolicyNumber();
        parseDates(startDateString);
    }

    protected String getPolicyNumber() {
        return policyNumber;
    }

    protected LocalDate getPolicyStartDate() {
        return policyStartDate;
    }

    protected LocalDate getPolicyEndDate() {
        return policyEndDate;
    }

    protected CoverageOption getCoverageOption() {
        return coverageOption;
    }

    protected PolicyTenure getPolicyTenure() {
        return policyTenure;
    }

    protected PremiumFrequency getPremiumFrequency() {
        return premiumFrequency;
    }

    protected int getAge() {
        return age;
    }

    protected abstract double calculateBasePremium();

    private String generatePolicyNumber() {
        return UUID.randomUUID().toString();
    }

    private void parseDates(String startDateString) throws PolicyException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate startDate = LocalDate.parse(startDateString, formatter);
            this.policyStartDate = startDate;
            this.policyEndDate = startDate.plusYears(this.policyTenure.getYears());
        } catch (DateTimeParseException e) {
            throw new PolicyException("Invalid date format. Please use 'yyyy-MM-dd'.");
        }
    }

    public boolean isPolicyActive() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(policyStartDate) && currentDate.isBefore(policyEndDate);
    }

    public Map<String, Double> calculatePremium() throws PolicyException {
        Map<String, Double> premiums = new HashMap<>();
        double basePremiumBeforeModifiers = calculateBasePremium();
    
        // Calculate age-based price modifier within calculateBasePremium() method
        double basePremiumAfterModifiers = basePremiumBeforeModifiers;
    
        double premiumPerPeriod = basePremiumAfterModifiers / getPremiumFrequency().getMonths();
        int totalPeriods = getPremiumFrequency().getMonths() * getPolicyTenure().getYears();
        double totalPremium = premiumPerPeriod * totalPeriods;
        double gst = totalPremium * 0.09; // GST rate is 9%
        double totalPremiumWithGST = totalPremium + gst;
        double gstPerPeriod=gst/totalPeriods;
        double premiumPerPeriodWithGST = premiumPerPeriod + gstPerPeriod;
        premiums.put("basePremiumBeforeModifiers", basePremiumBeforeModifiers);
        premiums.put("basePremiumAfterModifiers", basePremiumAfterModifiers);
        premiums.put("premiumPerPeriod", premiumPerPeriod);
        premiums.put("totalPremium", totalPremium);
        premiums.put("gst", gst);
        premiums.put("totalPremiumWithGST", totalPremiumWithGST);
        premiums.put("gstPerPeriod",gstPerPeriod);
        premiums.put("premiumPerPeriodWithGST", premiumPerPeriodWithGST);
        return premiums;
    }
    
    public String displayPolicyDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(getPolicyType()).append(" Policy Details:\n");
        sb.append("Policy Number: ").append(getPolicyNumber()).append("\n");
        sb.append("Coverage Option: ").append(getCoverageOption()).append("\n");
        sb.append("Policy Tenure: ").append(getPolicyTenure()).append("\n");
        sb.append("Premium Frequency: ").append(getPremiumFrequency()).append("\n");
        sb.append("Policy Start Date: ").append(getPolicyStartDate()).append("\n");
        sb.append("Policy End Date: ").append(getPolicyEndDate()).append("\n");

        try {
            Map<String, Double> premiums = calculatePremium();
            double basePremiumAfterModifiers = premiums.get("basePremiumAfterModifiers");
            double premiumPerPeriod = premiums.get("premiumPerPeriod");
            double totalPremium = premiums.get("totalPremium");
            double gst = premiums.get("gst");
            double totalPremiumWithGST = premiums.get("totalPremiumWithGST");
            double gstPerPeriod = premiums.get("gstPerPeriod");
            double premiumPerPeriodWithGST = premiums.get("premiumPerPeriodWithGST");

            DecimalFormat df = new DecimalFormat("#.00");

            sb.append("Base Premium (Before Modifier): $").append(df.format(getCoverageOption().getValue())).append("\n");

            // Display age price added based on policy type
            if (this instanceof LifeInsurance) {
                sb.append("Age Price Added: $").append(df.format(((LifeInsurance) this).agePriceAdded())).append("\n");
            } else if (this instanceof HealthInsurance) {
                sb.append("Age Price Added: $").append(df.format(((HealthInsurance) this).agePriceAdded())).append("\n");
            } else if (this instanceof AccidentInsurance) {
                sb.append("Age Price Added: $").append(df.format(((AccidentInsurance) this).agePriceAdded())).append("\n");
            }

            // Display smoking price and injuries price based on policy type
            if (this instanceof LifeInsurance) {
                sb.append("Smoker Price: $").append(df.format(((LifeInsurance) this).isSmoker() ? 500.00 : 0.00)).append("\n");
            } else if (this instanceof HealthInsurance) {
                sb.append("Smoker Price: $").append(df.format(((HealthInsurance) this).isSmoker() ? 500.00 : 0.00)).append("\n");
            } else if (this instanceof AccidentInsurance) {
                sb.append("Injuries Price: $").append(df.format(((AccidentInsurance) this).hasPastInjuries() ? 1000.00 : 0.00)).append("\n");
            }

            sb.append("Base Premium (After Modifiers): $").append(df.format(basePremiumAfterModifiers)).append("\n");
            sb.append("Premium Per Period: $").append(df.format(premiumPerPeriod)).append("\n");
            sb.append("GST Per Period: $").append(gstPerPeriod).append("\n"); // Display GST per period
            sb.append("Premium Per Period (With GST): $").append(premiumPerPeriodWithGST).append("\n");
            sb.append("Total Premium: $").append(df.format(totalPremium)).append("\n");
            sb.append("GST (9%): $").append(df.format(gst)).append("\n");
            sb.append("Total Premium (With GST): $").append(df.format(totalPremiumWithGST)).append("\n");

        } catch (PolicyException e) {
            sb.append("Error calculating premiums: ").append(e.getMessage()).append("\n");
        }

        return sb.toString();
    }

    

    protected abstract String getPolicyType();

    public enum CoverageOption {
        BASIC(1000),
        STANDARD(2000),
        PREMIUM(3000);

        private final int value;

        CoverageOption(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum PolicyTenure {
        FIVE_YEARS(5),
        TEN_YEARS(10),
        FIFTEEN_YEARS(15),
        TWENTY_YEARS(20);

        private final int years;

        PolicyTenure(int years){
            this.years = years;
        }

        public int getYears() {
            return years;
        }
    }

    public enum PremiumFrequency {
        MONTHLY(1),
        QUARTERLY(3),
        SEMI_ANNUALLY(6),
        ANNUALLY(12);

        private final int months;

        PremiumFrequency(int months) {
            this.months = months;
        }

        public int getMonths() {
            return months;
        }
    }

    public static class PolicyException extends Exception {
        public PolicyException(String message) {
            super(message);
        }
    }
}

class LifeInsurance extends InsurancePolicy {
    private boolean smoker;
    private double agePriceAdded;

    public LifeInsurance(String startDateString, CoverageOption coverageOption, PolicyTenure policyTenure,
                         PremiumFrequency premiumFrequency, int age, boolean smoker) throws PolicyException {
        super(startDateString, coverageOption, policyTenure, premiumFrequency, age);
        this.smoker = smoker;
    }

    @Override
    protected double calculateBasePremium() {
        double basePremium = getCoverageOption().getValue();
        agePriceAdded = getAge() * 10;
        if (smoker) {
            basePremium += 500;
        }
        return basePremium + agePriceAdded;
    }

    @Override
    protected String getPolicyType() {
        return "LIFE";
    }

    // Method to check if the policyholder is a smoker
    public boolean isSmoker() {
        return smoker;
    }
    public double agePriceAdded(){
        return agePriceAdded;
    }
}

class HealthInsurance extends InsurancePolicy {
    private boolean smoker;
    private double agePriceAdded;

    public HealthInsurance(String startDateString, CoverageOption coverageOption, PolicyTenure policyTenure,
                           PremiumFrequency premiumFrequency, int age, boolean smoker) throws PolicyException {
        super(startDateString, coverageOption, policyTenure, premiumFrequency, age);
        this.smoker = smoker;
    }

    @Override
    protected double calculateBasePremium() {
        double basePremium = getCoverageOption().getValue();
        agePriceAdded = getAge() * 10;
        if (smoker) {
            basePremium += 500;
        }
        return basePremium + agePriceAdded;
    }

    @Override
    protected String getPolicyType() {
        return "HEALTH";
    }

    // Method to check if the policyholder is a smoker
    public boolean isSmoker() {
        return smoker;
    }
    public double agePriceAdded(){
        return agePriceAdded;
    }
}

class AccidentInsurance extends InsurancePolicy {
    private boolean pastInjuries;
    private double agePriceAdded;

    public AccidentInsurance(String startDateString, CoverageOption coverageOption, PolicyTenure policyTenure,
                             PremiumFrequency premiumFrequency, int age, boolean pastInjuries) throws PolicyException {
        super(startDateString, coverageOption, policyTenure, premiumFrequency, age);
        this.pastInjuries = pastInjuries;
    }

    @Override
    protected double calculateBasePremium() {
        double basePremium = getCoverageOption().getValue();
        agePriceAdded = getAge() * 10;
        if (pastInjuries) {
            basePremium += 1000;
        }
        return basePremium + agePriceAdded;
    }

    @Override
    protected String getPolicyType() {
        return "ACCIDENT";
    }

    // Method to check if the policyholder has past injuries
    public boolean hasPastInjuries() {
        return pastInjuries;
    }
    public double agePriceAdded(){
        return agePriceAdded;
    }
}
