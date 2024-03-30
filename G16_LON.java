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
   private double monthlyPayment;
   private int loanTermMonths;
   private double loanRepayment;
   private double ExcessPayment;
   private boolean repaymentFlag;
   private int monthofpayment;

   public G16_LON(double var1, double var3, LocalDate var5, int var6) {
      this.principal = var1;
      this.interestRate = var3;
      this.loanStartDate = var5;
      this.loanTermMonths = var6;
      this.loanRepayment = this.getTotalPayment();
      this.monthlyPayment = this.calculateMonthlyPayment();
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

   public void setLoanEndDate(int var1) {
      this.loanTermMonths = var1;
   }

   public LocalDate getLoanEndDate() {
      return this.loanStartDate.plus(Period.ofMonths(this.loanTermMonths));
   }

   public int getLoanTermMonths() {
      return this.loanTermMonths;
   }

   public double getLoanRepayment() {
      return this.loanRepayment;
   }

   public void payLoan(double var1) {
      this.loanRepayment = (double)Math.round(this.loanRepayment - var1);
      if (var1 > this.monthlyPayment) {
         this.repaymentFlag = true;
         this.ExcessPayment = var1 - this.monthlyPayment;
         this.monthofpayment = LocalDate.now().getMonthValue();
      } else {
         this.monthlyPayment -= var1;
      }

   }

   public void setExcessPayment(double var1) {
      this.ExcessPayment = var1;
   }

   public double getExcessPayment() {
      return this.ExcessPayment;
   }

   public double getTotalPayment() {
      return (double)Math.round(this.calculateMonthlyPayment() * (double)this.loanTermMonths);
   }

   public double calculateMonthlyPayment() {
      double var1 = this.interestRate / 12.0;
      double var3 = this.principal * var1 * Math.pow(1.0 + var1, (double)this.loanTermMonths) / (Math.pow(1.0 + var1, (double)this.loanTermMonths) - 1.0);
      return var3;
   }

   public double getMonthlyPayment() {
      if (this.repaymentFlag && LocalDate.now().getMonthValue() != this.monthofpayment) {
         this.setMonthlyPayments(this.calculateMonthlyPayment() - this.ExcessPayment);
         this.repaymentFlag = false;
         this.ExcessPayment = 0.0;
      }

      return this.monthlyPayment;
   }

   public void setMonthlyPayments(double var1) {
      this.monthlyPayment = var1;
   }

   public void setPrincipal(double var1) {
      if (var1 > 0.0) {
         this.principal = var1;
      } else {
         throw new IllegalArgumentException("The principal must be a positive number.");
      }
   }

   public void setInterestRate(double var1) {
      if (var1 >= 0.0) {
         this.interestRate = var1;
      } else {
         throw new IllegalArgumentException("The interest rate cannot be negative.");
      }
   }

   public void setLoanStartDate(LocalDate var1) {
      this.loanStartDate = var1;
   }

   public void setLoanTermMonths(int var1) {
      if (var1 > 0) {
         this.loanTermMonths = var1;
      } else {
         throw new IllegalArgumentException("The loan term must be a positive integer.");
      }
   }

   public void displayLoanDetails() {
      DateTimeFormatter var1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      System.out.println("Loan ID                  : " + this.loanId);
      System.out.printf("Interest Rate            : %.2f%%\n", this.getInterestRate() * 100.0);
      System.out.printf("Loan Amount              : $%.2f\n", this.getPrincipal());
      System.out.printf("Total Repayment          : $%.2f\n", this.getTotalPayment());
      if (this.getMonthlyPayment() <= 0.0) {
         System.out.println("No more payment required for this month");
      } else {
         System.out.printf("Monthly Payment          : $%.2f\n", this.getMonthlyPayment());
      }

      System.out.printf("Loan Duration (months)   : %d\n", this.getLoanTermMonths());
      System.out.printf("Payment remaining        : $%.2f\n", this.getLoanRepayment());
      PrintStream var10000 = System.out;
      String var10001 = var1.format(this.getLoanStartDate());
      var10000.println("Loan Start Date          : " + var10001);
      var10000 = System.out;
      var10001 = var1.format(this.getLoanEndDate());
      var10000.println("Loan End Date            : " + var10001);
   }
}
