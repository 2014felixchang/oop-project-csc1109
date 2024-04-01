import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
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

    public CreditCard(Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate2, double annualFee, double interestRate, int benefits, String cardType, double ownLimit, double creditLimit, double creditBill, int accountNum) {
        this.cardNumber = cardNumber;
        this.cardNumLast4Digit = cardNumLast4Digit;
        this.cardExpiryDate = cardExpiryDate2;
        this.annualFee = annualFee;
        this.interestRate = interestRate;
        this.benefits = benefits;
        this.cardType = cardType;
        this.ownLimit = ownLimit;
        this.creditLimit = creditLimit;
        this.creditBill = creditBill;
        this.accountNum = accountNum;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber, boolean generateNew) {
        if (generateNew) {
            Long min = 10000000000000000L;
            Long max = 99999999999999999L;
            this.cardNumber = ThreadLocalRandom.current().nextLong(min, max + 1L);
        } else {
            this.cardNumber = cardNumber;
        }
    }

    public String getCardNumLast4Digit() {
        return cardNumLast4Digit;
    }

    public void setCardNumLast4Digit(String last4Digit, boolean generateNew) {
        if (generateNew) {
            long cardNum = this.getCardNumber();
            String new4Digit = String.format("%04d", cardNum % 10000L);
            this.cardNumLast4Digit = new4Digit;
        } else {
            this.cardNumLast4Digit = last4Digit;
        }
    }

    public LocalDate getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(LocalDate cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public double getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(double annualFee) {
        this.annualFee = annualFee;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getBenefits() {
        return benefits;
    }

    public void setBenefits(int benefits) {
        this.benefits = benefits;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public double getOwnLimit() {
        return ownLimit;
    }

    public void setOwnLimit(double ownLimit) {
        this.ownLimit = ownLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getCreditBill() {
        return creditBill;
    }

    public void setCreditBill(double creditBill) {
        this.creditBill = creditBill;
    }

    public void payBill(double amount) {
        if (amount > this.creditBill) {
            System.out.println("Amount exceeds the credit bill");
        } else {
            this.creditBill -= amount;
        }
    }

    public void makeTransactions(double amount) {
        if (amount + this.creditBill > this.creditLimit) {
            System.out.println("Amount exceeds credit limit");
        } else if (amount + this.creditBill > this.ownLimit) {
            System.out.println("Amount exceeds own limit");
        } else {
            this.creditBill += amount;
        }
    }

    public void displayCreditCardDetails() {
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

class CreditCardRewards extends CreditCard {
    public CreditCardRewards(Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate, double annualFee, double interestRate, int benefits, double ownLimit, double creditLimit, double creditBill, int accountNum) {
        super(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "Rewards Credit Card", ownLimit, creditLimit, creditBill, accountNum);
    }

    @Override
    public int getBenefits() {
        return benefits;
    }

    @Override
    public void setBenefits(int benefits) {
        this.benefits = benefits;
    }

    public void claimRewards(String item) {
        if (Integer.valueOf(item) == 1 && this.benefits >= 300) {
            System.out.println("$30 Grab Voucher has been claimed, 300 Points has been deducted.");
            this.benefits -= 300;
        } else if (Integer.valueOf(item) == 2 && this.benefits >= 500) {
            System.out.println("$50 Shopee Voucher has been claimed, 500 Points has been deducted.");
            this.benefits -= 500;
        } else if (Integer.valueOf(item) == 3 && this.benefits >= 3630) {
            System.out.println("Apple Airpods Pro (2nd Generation) (USB-C) has been claimed, 3630 Points has been deducted.");
            this.benefits -= 3630;
        } else if (Integer.valueOf(item) == 4 && this.benefits >= 5040) {
            System.out.println("Apple 10.2-inch iPad Wi-Fi 64GB Space Grey has been claimed, 5040 Points has been deducted.");
            this.benefits -= 5040;
        } else {
            System.out.println("Invalid card input or not enough points");
        }

        System.out.println("Remaining Points: " + this.benefits);
    }
}

class CreditCardStudent extends CreditCard {
    public CreditCardStudent(Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate, double annualFee, double interestRate, int benefits, double ownLimit, double creditLimit, double creditBill, int accountNum) {
        super(cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "Student Credit Card", ownLimit, creditLimit, creditBill, accountNum);
    }

    @Override
    public int getBenefits() {
        return benefits;
    }

    @Override
    public void setBenefits(int benefits) {
        this.benefits = benefits;
    }

    public void payBill(double amount, int cashback) {
        if (amount + (double) cashback > this.creditBill) {
            System.out.println("Amount exceeds the credit bill");
        } else {
            System.out.println("Credit Bill (Before payment): " + this.creditBill);
            this.creditBill -= (amount + (double) cashback);
            this.benefits -= cashback;
            System.out.println("Successful payment of $" + amount + " with $" + cashback + " cashback");
            System.out.println("Credit Bill (After payment): " + this.creditBill);
            System.out.println("Remaining Cashback: " + this.benefits);
        }
    }

}
class CreditCardTravel extends CreditCard{
    public CreditCardTravel (Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate, double annualFee, double interestRate, int benefits, double ownLimit, double creditLimit, double creditBill, int accountNum) { 
        super (cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "Travel Credit Card", ownLimit, creditLimit, creditBill, accountNum);
    }
    @Override
    public int getBenefits(){
        return benefits;
    }
    @Override
    public void setBenefits(int benefits){
        this.benefits = benefits;
    }

    public void claimBenefits(String item){
        if (Integer.valueOf(item) == 1 && this.benefits >= 50) {
            System.out.println("Free airplane merchandise has been claimed, 50 Points has been deducted.");
            this.benefits -= 50;
         } else if (Integer.valueOf(item) == 2 && this.benefits >= 600) {
            System.out.println("$200 dinning Voucher has been claimed, 600 Points has been deducted.");
            this.benefits -= 500;
         } else if (Integer.valueOf(item) == 3 && this.benefits >= 10000) {
            System.out.println("One Way trip within Asia has been claimed, 10000 Points has been deducted.");
            this.benefits -= 10000;
         } else {
            System.out.println("Invalid card input or not enough points");
         }
   
         System.out.println("Remaining Miles: " + this.benefits);
        }
    }

