package src;
/**
 * Description: The ForeignExchange class represents the available exchange rates within a Bank 
 * and handles the conversion of currency
 */

public class ForeignExchange {
    private double sgdToUsdRate = 0.75;
    private double usdToSgdRate = 1.24;

    /**
     * Converts an amount of money from one currency to another
     * 
     * @param fromCurrency The original currency to be converted
     * @param toCurrency The target currency
     * @param amount The amount of money that is to be converted
     * @return The converted amount of money
     */
    public double convert(String fromCurrency, String toCurrency, double amount){
        System.out.println("Converting " + amount + " " + fromCurrency + " to " + toCurrency);
        
        if(fromCurrency == "SGD" && toCurrency == "USD"){
            return amount * sgdToUsdRate;
        }
        if(fromCurrency == "USD" && toCurrency == "SGD"){
            return amount * usdToSgdRate;
        }

        return amount;
    }
}
