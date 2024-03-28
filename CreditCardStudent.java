import java.time.LocalDate;

public class CreditCardStudent extends CreditCard{
    public CreditCardStudent (Long cardNumber, String cardNumLast4Digit, LocalDate cardExpiryDate, double annualFee, double interestRate, int benefits, double ownLimit, double creditLimit, double creditBill, int accountNum) { 
        super (cardNumber, cardNumLast4Digit, cardExpiryDate, annualFee, interestRate, benefits, "Student Credit Card", ownLimit, creditLimit, creditBill, accountNum);
    }

    public int getBenefits(){
        return benefits;
    }

    public void setBenefits(int benefits){
        this.benefits = benefits;
    }

    public void payBill(double amount, int cashback) {
        if (amount + (double) cashback > this.creditBill){
            System.out.println("Amount exceeds the credit bill");
        }
        else {
            System.out.println("Credit Bill (Before payment): " + this.creditBill);
            this.creditBill -= (amount + (double) cashback);
            this.benefits -= cashback;
            System.out.println("Successful payment of $" + amount + " with $" + cashback + " cashback");
            System.out.println("Credit Bill (After payment): " + this.creditBill);
            System.out.println("Remaining Cashback: " + this.benefits);
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