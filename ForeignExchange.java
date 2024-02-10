public class ForeignExchange {
    private double sgdToUsdRate = 0.75;
    private double usdToSgdRate = 1.24;

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
