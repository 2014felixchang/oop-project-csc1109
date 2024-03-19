import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * The Loan class handles all logic and operations related to loans. 
 * The class calculates the monthly payment to be made using the Amortization Loan formula.
 * The class includes a constructor, getter and setter methods, along with methods that handles loan related computations.
 * Note: Some setter methods are not included due to privacy and security concerns.
 * 
 */
public class Loan {
    private String accountId; //To associate the loan to a specified account holder. Change data type if necsssary
    private BigDecimal loanAmount; //Principle (original) loan amount
    private BigDecimal finalLoanAmount; //Total amount to be paid after interest rate
    private BigDecimal monthlyPaymentAmount; //Amount to be paid monthly after interest rate
    private BigDecimal loanBalance; //Balance of the loan after monthly payments
    private BigDecimal outstandingBalance; //To keep track of the amount to be paid over time
    private BigDecimal interestRate;
    private int repaymentPeriod; //Value in months
    private LocalDate startLoanDate;
    private int lateCounter; //To check if account holder have any outstanding payments
    private int lateCounterRecord; //To keep a record of the number of late payments. Used to determine credit score
    private MathContext roundto7DP = new MathContext(7, RoundingMode.HALF_UP); //For arithmetic rounding purposes

    private static final double MINUMUM_LOAN_AMOUNT = 1000;
    private static final BigDecimal LATE_PAYMENT_FEE = new BigDecimal(150);

    /**
     * Constructor. All money related double data type parameters are converted to BigDecimal.
     * Also validates if the loan is within constraints
     * @param accountId To specify the account holder to associate the loan. Change the data type if necessary
     * @param loanAmount The amount to be loaned
     * @param interestRate Interest Rate of the loan
     * @param repaymentPeriod The amount of time (in years) need for account holder to pay the loan
     * @param startLoanDate The start date of the loan
     */
    public Loan(String accountId, double loanAmount, double interestRate, int repaymentPeriod, LocalDate startLoanDate) {
        if (validateInputs(loanAmount, interestRate, repaymentPeriod) == false) {
            System.out.println("Invalid inputs, please check again");
            System.exit(0);
        }

        this.accountId = accountId;
        this.loanAmount = new BigDecimal(loanAmount);
        this.outstandingBalance = new BigDecimal(0);
        this.interestRate = new BigDecimal(interestRate);
        this.repaymentPeriod = repaymentPeriod;
        this.startLoanDate = startLoanDate;
        this.lateCounter = 0;
        this.lateCounterRecord = 0;
    }

    //Getter and Setter methods
    /**
     * Getter method for principle (original) loan amount
     * 
     * @return the principle (original) loan amount
     */
    public BigDecimal getPrincipleAmount() {
        return loanAmount;
    }


    /**
     * Getter method for monthly payment amount
     * @return The amount to be paid monthly
     */
    public BigDecimal getMonthlyPaymentAmount() {
        return monthlyPaymentAmount;
    }


    /**
     * Getter method to retreive the final loan amount (interest included)
     * @return The total loan amount including interest
     */
    public BigDecimal getFinalLoanAmount() {
        return finalLoanAmount;
    }


    /**
     * Getter method to retreive loan balance
     * @return The remaining amount of loan owed
     */
    public BigDecimal getLoanBalance() {
        return loanBalance;
    }


    /**
     * Getter method to retreive outstanding balance
     * @return The amount owed by account holder
     */
    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }


    /**
     * Getter method to retreive the interest rate
     * @return The interest rate of the loan in %
     */
    public BigDecimal getInterestRate() {
        return interestRate;
    }


    /**
     * Getter method to retreive the repayment period AKA the duration of the loan
     * @return The repayment period in months
     */
    public int getRepaymentPeriod() {
        return repaymentPeriod;
    }
    
    /**
     * Setter method to set the details of a Loan
     */
    public void setLoan() {
        calculateLoanDetails();
    }


    /**
     * Calculates the amount to be paid monthly with interest applied, also sets the total loan amount and the loan balance.
     * This method uses the Amortization Loan formula to calculate monthly payments.
     * @see <a href="https://www.moneygeek.com/personal-loans/calculate-loan-payments/#calculating-amortizing-loans">Amortization Loan Formula </a>
     */
    private void calculateLoanDetails() {
        BigDecimal rate;
        BigDecimal monthlyRate;
        BigDecimal monthlyRateDivisor = new BigDecimal(12);
        BigDecimal monthlyRateTemp;
        BigDecimal x;
        BigDecimal y;
        BigDecimal z;
        
        monthlyRate = (interestRate.divide(new BigDecimal(100)));
        monthlyRate = monthlyRate.divide(monthlyRateDivisor, roundto7DP);

        monthlyRateTemp = monthlyRate.add(new BigDecimal(1));
        rate = monthlyRateTemp.pow(repaymentPeriod);
        
        x = rate.subtract(new BigDecimal(1));
        y = monthlyRate.multiply(rate);
        
        z = x.divide(y, roundto7DP);
        monthlyPaymentAmount = loanAmount.divide(z, roundto7DP);
        finalLoanAmount = monthlyPaymentAmount.multiply(new BigDecimal(repaymentPeriod));

        //Round the values to match money format
        monthlyPaymentAmount = monthlyPaymentAmount.setScale(2, RoundingMode.HALF_UP);
        finalLoanAmount = finalLoanAmount.setScale(2, RoundingMode.HALF_UP);
        
        //Set the loan balance
        loanBalance = finalLoanAmount;
    }


    /**
     * Method to check if monthly payments are due.
     * At the 10th of each month, the bank will request for a loan payment.
     * A counter is used to keep track of late payments. A late payment fee is added if account holder fails to make payment by the next payment cycle.
     * Paying late will affect credit score.
     * 
     * @throws DateTimeException Occurs if there is an issue with date format when caluating between 2 dates
     */
    protected void checkIfPaymentDue() {
        try {
            LocalDate currentDate = LocalDate.now(); //Actual Implementation
            LocalDate testDate = LocalDate.of(2024, 3, 10); //Example Implementation (for testing purposes)

            if (currentDate.getDayOfMonth() == 10) {
                outstandingBalance = outstandingBalance.add(monthlyPaymentAmount);
                //To simulate late payment, change lateCounter value in constructor class to >0
                if (lateCounter >= 1) {
                    lateCounterRecord++;
                    outstandingBalance = outstandingBalance.add(LATE_PAYMENT_FEE);
                    System.out.println("You have outstanding payments from the previous month, a late payment fee has been added to your Outstanding Balance");
                    System.out.println("Your new Outstanding Balance is: " + getOutstandingBalance());
                }

                lateCounter++;
                System.out.println("Your monthly payments are due. Please make your payments to avoid any late payment fees");
            }
        } 
        catch (DateTimeException e) {
            e.printStackTrace();
        }
    }


    /**
     * Method to allow account holder to pay loans. The method only allows exact payments to be made.
     * Deducts the amount paid from loanBalance and outstandingBalance
     * @param paymentInput The amount of payment the account holder is paying in an instance
     */
    protected void payLoan(String paymentInput) {
        BigDecimal payment = new BigDecimal(paymentInput);
        System.out.println("Current Outstanding Amount: " + getOutstandingBalance());
        System.out.println("You will be paying: " + payment);
        
        if (loanBalance.compareTo(payment) < 0 || monthlyPaymentAmount.compareTo(payment) < 0) {
            System.out.println("Excess payment detected, please pay according to what you owe.");
        }
        else if (payment.equals(outstandingBalance)){
            loanBalance = loanBalance.subtract(payment);
            outstandingBalance = outstandingBalance.subtract(payment);
            lateCounter = 0;
            System.out.println("Payment success! New Outstanding Amount: " + getOutstandingBalance() + "\nRemaining Loan Balance: " + getLoanBalance());
        }
        else {
            System.out.println("Please pay the exact amount");
        }
    }

    /**
     * Method to validate inputs before passing into constructor.
     * Principle Loan must be more than the minimum loan amount.
     * Interest rate should not be more than 100
     * Repayment Period should not exceed 1200 months (100 years)
     * @param loanAmount
     * @param interestRate
     * @param repaymentPeriod
     * @return boolean value FALSE if any of the above scenario match, otherwise TRUE
     */
    protected boolean validateInputs(double loanAmount, double interestRate, int repaymentPeriod) {
        if (loanAmount  < MINUMUM_LOAN_AMOUNT || interestRate > 100 || repaymentPeriod > 1200) {
            return false;
        }
        else {
            return true;
        }
    }
}