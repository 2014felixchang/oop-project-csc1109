import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class CreditCard {
    protected Long cardNumber;
    protected String cardNumLast4Digit;
    protected LocalDate cardExpiryDate;
    protected double annualFee;
    protected double interestRate;
    protected int benefits;
    protected String cardType;
    protected double ownLimit;
    protected double creditLimit;
    protected double creditBill;
    protected int accountNum;

    public CreditCard(Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate, double annualFee, double interestRate, int benefits, String cardType, double ownLimit, double creditLimit, double creditBill, int accountNum) {
        this.cardNumber = cardNumber;
        this.cardNumLast4Digit = cardNumLast4Digit;
        this.cardExpiryDate = cardExpiryDate;
        this.annualFee = annualFee;
        this.interestRate = interestRate;
        this.benefits = benefits;
        this.cardType = cardType;
        this.ownLimit = ownLimit;
        this.creditLimit = creditLimit;
        this.creditBill = creditBill;
        this.accountNum = accountNum;
    }

    public int getAccountNum(){
        return accountNum;
    }

    public void setAccountNum(int accountNum){
        this.accountNum = accountNum;
    }

    public Long getCardNumber(){
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber, boolean generateNew){
        if (generateNew){
            Long min = 10000000000000000L;
            Long max = 99999999999999999L;
            this.cardNumber = ThreadLocalRandom.current().nextLong(min, max + 1L);
        }
        else {
        this.cardNumber = cardNumber;
        }
    }

    public String getCardNumLast4Digit(){
        return cardNumLast4Digit;
    }

    public void setCardNumLast4Digit(String last4Digit, boolean generateNew){
        if (generateNew) {
            long cardNum = this.getCardNumber();
            String new4Digit = String.format("%04d", cardNum % 10000L);
            this.cardNumLast4Digit = new4Digit;
        }
        else {
            this.cardNumLast4Digit = last4Digit;
        }
    }

    public LocalDate getCardExpiryDate(){
        return cardExpiryDate;
    }

    public void setCardExpiryDate(LocalDate cardExpiryDate){
        this.cardExpiryDate = cardExpiryDate;
    }

    public double getAnnualFee(){
        return annualFee;
    }

    public void setAnnualFee(double annualFee){
        this.annualFee = annualFee;
    }

    public double getInterestRate(){
        return interestRate;
    }

    public void setInterestRate(double interestRate){
        this.interestRate = interestRate;
    }

    public int getBenefits(){
        return benefits;
    }

    public void setBenefits(int benefits){
        this.benefits = benefits;
    }

    public String getCardType(){
        return cardType;
    }

    public void setCardType(String cardType){
        this.cardType = cardType;
    }

    public double getOwnLimit(){
        return ownLimit;
    }

    public void setOwnLimit(double ownLimit){
        this.ownLimit = ownLimit;
    }

    public double getCreditLimit(){
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit){
        this.creditLimit = creditLimit;
    }

    public double getCreditBill(){
        return creditBill;
    }

    public void setCreditBill(double creditBill){
        this.creditBill = creditBill;
    }

    public void payBill(double amount){
        if (amount > this.creditBill){
            System.out.println("Amount exceeds the credit bill");
        }
        else {
            this.creditBill -= amount;
        }
    }

    public void makeTransactions(double amount){
        if (amount + this.creditBill > this.creditLimit){
            System.out.println("Amount exceeds credit limit");
        }
        else if (amount + this.creditBill > this.ownLimit){
            System.out.println("Amount exceeds own limit");}
        else {
            this.creditBill += amount;
        }
    }

    public void displayCreditCardDetails(){
        System.out.println("Credit Card Details: ");
        System.out.println("Full Credit Card Number: " + this.cardNumber);
        System.out.println("Last 4 Digit of Credit Card Number: XXXX XXXX XXXX " + this.cardNumLast4Digit);
        System.out.println("Card Type: " + this.cardType);
        System.out.println("Card Expiry Date: " + String.valueOf(this.cardExpiryDate));
        System.out.println("Interest Rate: " + this.interestRate);
        System.out.println("Annual Fee: " + this.annualFee);
        System.out.println("Benefit: " + this.benefits);
        System.out.println("User set Limit: " + this.ownLimit);
        System.out.println("Card Limit: " + this.creditLimit);
        System.out.println("Bill: " + this.creditBill);
        System.out.println("Account Number: " + this.accountNum);
    }

}
