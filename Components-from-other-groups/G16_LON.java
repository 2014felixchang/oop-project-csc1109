// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class G16_LON {
   private String loanId = UUID.randomUUID().toString();
   private double principal;
   private double interestRate;
   private LocalDate loanStartDate;
   private int loanTermMonths;
   private double loanRepayment;

   public G16_LON(double var1, double var3, LocalDate var5, int var6) {
      this.principal = var1;
      this.interestRate = var3;
      this.loanStartDate = var5;
      this.loanTermMonths = var6;
      this.loanRepayment = this.getTotalPayment();
   }

   public String getLoanId() {
      return this.loanId;
   }

   public double getPrincipal() {
      return this.principal;
   }

   public double getInterestRate() {
      return this.interestRate;
   }

   public LocalDate getLoanStartDate() {
      return this.loanStartDate;
   }

   public LocalDate getLoanEndDate() {
      return this.loanStartDate.plus(Period.ofMonths(this.loanTermMonths));
   }

   public double getLoanTermMonths() {
      return (double)this.loanTermMonths;
   }

   public double getLoanRepayment() {
      return this.loanRepayment;
   }

   public void payLoan(double var1) {
      this.loanRepayment = (double)Math.round(this.loanRepayment - var1);
   }

   public double getTotalPayment() {
      return (double)Math.round(this.getMonthlyPayment() * (double)this.loanTermMonths);
   }

   public double getMonthlyPayment() {
      double var1 = this.interestRate / 12.0;
      return this.principal * var1 * Math.pow(1.0 + var1, (double)this.loanTermMonths) / (Math.pow(1.0 + var1, (double)this.loanTermMonths) - 1.0);
   }

   public void displayLoanDetails() {
      DateTimeFormatter var1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      System.out.println("Loan ID                  : " + this.loanId);
      System.out.printf("Interest Rate            : %.2f%%\n", this.getInterestRate() * 100.0);
      System.out.printf("Loan Amount              : $%.2f\n", this.getPrincipal());
      System.out.printf("Total Repayment          : $%.2f\n", this.getTotalPayment());
      System.out.printf("Monthly Payment          : $%.2f\n", this.getMonthlyPayment());
      System.out.printf("Loan Duration (months)   : %.0f\n", this.getLoanTermMonths());
      System.out.printf("Payment remaining        : %.2f\n", this.getLoanRepayment());
      PrintStream var10000 = System.out;
      String var10001 = var1.format(this.getLoanStartDate());
      var10000.println("Loan Start Date          : " + var10001);
      var10000 = System.out;
      var10001 = var1.format(this.getLoanEndDate());
      var10000.println("Loan End Date            : " + var10001);
   }
}
