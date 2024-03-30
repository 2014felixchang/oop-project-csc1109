// Source code is decompiled from a .class file using FernFlower decompiler.
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Security {
   private static Security instance;
   private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

   private Security() {
   }

   public static Security getInstance() {
      if (instance == null) {
         instance = new Security();
      }

      return instance;
   }

   public static String hashPassword(String var0) throws NoSuchAlgorithmException {
      MessageDigest var1 = MessageDigest.getInstance("SHA-256");
      var1.update(var0.getBytes());
      byte[] var2 = var1.digest();
      return bytesToHex(var2);
   }

   private static String bytesToHex(byte[] var0) {
      StringBuilder var1 = new StringBuilder();
      byte[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte var5 = var2[var4];
         String var6 = Integer.toHexString(255 & var5);
         if (var6.length() == 1) {
            var1.append('0');
         }

         var1.append(var6);
      }

      return var1.toString();
   }

   public static void logAccountAction(int var0, String var1) {
      LocalDateTime var2 = LocalDateTime.now();
      String var3 = var2.format(formatter);
      String var4 = var3 + " : " + var1;
      String var5 = "" + var0 + "_log.txt";

      try {
         BufferedWriter var6 = new BufferedWriter(new FileWriter(var5, true));

         try {
            var6.write(var4);
            var6.newLine();
         } catch (Throwable var10) {
            try {
               var6.close();
            } catch (Throwable var9) {
               var10.addSuppressed(var9);
            }

            throw var10;
         }

         var6.close();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   public static String genUniqueId() {
      return UUID.randomUUID().toString();
   }

   public static void customerLogIn(Map<String, Customer> var0) {
      Scanner var1 = new Scanner(System.in);

      int var2;
      for(var2 = 0; var2 < 3; ++var2) {
         try {
            System.out.print("Enter Your Name: ");
            String var3 = var1.nextLine();
            var1.nextLine();
            System.out.print("Enter Your Password: ");
            String var4 = var1.nextLine();
            String var5 = hashPassword(var4);
            Customer var6 = (Customer)var0.get(var3);
            if (var6 != null && var6.checkPassword(var5)) {
               System.out.println("Login successful!");
               return;
            }

            System.out.println("Login failed. Please try again.");
         } catch (Exception var7) {
            System.out.println("An error occurred: " + var7.getMessage());
            var1.nextLine();
         }
      }

      if (var2 >= 3) {
         System.out.println("Maximum login attempts exceeded. The program will now exit.");
         System.exit(0);
      }

      var1.close();
   }

   public static void enterPin(CreditCard var0) {
      Scanner var1 = new Scanner(System.in);
      System.out.print("Enter PIN Number: ");
      int var2 = var1.nextInt();

      try {
         if (var0.checkPin(var0, var2)) {
            System.out.println("PIN verification successful!");
         } else {
            System.out.println("PIN does not match. Access denied.");
         }
      } catch (Exception var7) {
         System.out.println("An error occurred while trying to verify the PIN.");
         var7.printStackTrace();
      } finally {
         var1.close();
      }

   }

   public static void enterPassword(Customer var0) {
      Scanner var1 = new Scanner(System.in);
      System.out.print("Enter PIN: ");
      String var2 = var1.next();

      try {
         if (var0.getAccountPassword().equals(var2)) {
            System.out.println("Password verification successful!");
         } else {
            System.out.println("Password does not match. Access denied.");
         }
      } catch (Exception var7) {
         System.out.println("An error occurred while trying to verify the password.");
         var7.printStackTrace();
      } finally {
         var1.close();
      }

   }

   public static void resetPin(CreditCard var0) {
      Scanner var1 = new Scanner(System.in);
      System.out.print("Enter current PIN: ");
      int var2 = var1.nextInt();

      try {
         if (var0.checkPin(var0, var2)) {
            System.out.print("Enter new PIN: ");
            int var3 = var1.nextInt();
            if (isValidPin(var3)) {
               System.out.print("Re-enter new PIN: ");
               int var4 = var1.nextInt();
               if (var3 == var4) {
                  var0.setPin(var3);

                  try {
                     updatePinInCsv("..data/creditcards.csv", var0, var3);
                  } catch (Exception var10) {
                     var10.printStackTrace();
                  }

                  System.out.println("PIN has been successfully reset.");
               } else {
                  System.out.println("The new PIN numbers do not match. Please try again.");
               }
            } else {
               System.out.println("The new PIN must be an integer with at least 6 digits.");
            }
         } else {
            System.out.println("Current PIN verification failed. Access denied.");
         }
      } catch (Exception var11) {
         System.out.println("An error occurred while trying to reset the PIN.");
         var11.printStackTrace();
      } finally {
         var1.close();
      }

   }

   public static void updatePinInCsv(String var0, CreditCard var1, int var2) {
      File var3 = new File(var0);
      File var4 = new File(var0);

      try {
         BufferedReader var5 = new BufferedReader(new FileReader(var4));

         try {
            BufferedWriter var6 = new BufferedWriter(new FileWriter(var3));

            String var7;
            try {
               for(; (var7 = var5.readLine()) != null; var6.write(var7 + System.lineSeparator())) {
                  String[] var8 = var7.split(",");
                  if ((long)Integer.parseInt(var8[1]) == var1.getccNumber()) {
                     var8[5] = String.valueOf(var2);
                     var7 = String.join(",", var8);
                  }
               }
            } catch (Throwable var11) {
               try {
                  var6.close();
               } catch (Throwable var10) {
                  var11.addSuppressed(var10);
               }

               throw var11;
            }

            var6.close();
         } catch (Throwable var12) {
            try {
               var5.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }

            throw var12;
         }

         var5.close();
      } catch (IOException var13) {
         var13.printStackTrace();
      }

      if (!var4.delete()) {
         System.out.println("Could not delete original file");
      } else {
         if (!var3.renameTo(var4)) {
            System.out.println("Could not rename temporary file");
         }

      }
   }

   private static boolean isValidPin(int var0) {
      return String.valueOf(var0).length() == 6;
   }
}
