import java.time.LocalDate;

public class CreditCardTravel extends CreditCard{
    public CreditCardTravel (Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate, double annualFee, double interestRate, int benefits, double ownLimit, double creditLimit, double creditBill, int accountNum) { 
        super (cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "Travel Credit Card", ownLimit, creditLimit, creditBill, accountNum);
    }

    public int getBenefits(){
        return benefits;
    }

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

