import java.time.LocalDate;

public class CreditCardRewards extends CreditCard{
    public CreditCardRewards (Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate, double annualFee, double interestRate, int benefits, double ownLimit, double creditLimit, double creditBill, int accountNum) { 
        super (cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "Rewards Credit Card", ownLimit, creditLimit, creditBill, accountNum);
    }

    public int getBenefits(){
        return benefits;
    }

    public void setBenefits(int benefits){
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
