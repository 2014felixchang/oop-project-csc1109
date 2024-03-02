
/**
 * Description: The ForeignExchange class represents the available exchange rates within a Bank 
 * and handles the conversion of currency
 */

public class ForeignExchange {
    private double sgdToUsdRate;
    private double sgdToJpyRate;
    private double usdToSgdRate;
    private double jpyToSgdRate;
    private double usdToJpyRate;
    private double jpyToUsdRate;

    /**
     * Constructor for ForeignExchange class that sets up the exchange rates (getMarketRate())
     */
    public ForeignExchange(){
        getMarketRate();
    }

    /**
     * Constructor for ForeignExchange class with custom exchange rates in parameters
     * (not using our rates in getMarketRate())
     * 
     * @param sgdToUsdRate The 1 SGD to X USD rate, X is the parameter amount
     * @param sgdToJpyRate The 1 SGD to X JPY rate, X is the parameter amount
     * @param usdToSgdRate The 1 USD to X SGD rate, X is the parameter amount
     * @param jpyToSgdRate The 1 JPY to X SGD rate, X is the parameter amount
     * @param usdToJpyRate The 1 USD to X JPY rate, X is the parameter amount
     * @param jpyToUsdRate The 1 JPY to X USD rate, X is the parameter amount
     */
    public ForeignExchange(double sgdToUsdRate, double sgdToJpyRate, double usdToSgdRate, double jpyToSgdRate, double usdToJpyRate, double jpyToUsdRate){
        setSgdToUsdRate(sgdToUsdRate);
        setSgdToJpyRate(sgdToJpyRate);
        setUsdToSgdRate(usdToSgdRate);
        setJpyToSgdRate(jpyToSgdRate);
        setUsdToJpyRate(usdToJpyRate);
        setJpyToUsdRate(jpyToUsdRate);
    }

    /**
     * Converts an amount of money from one currency to another
     * 
     * @param fromCurrency The original currency to be converted
     * @param toCurrency The target currency
     * @param amount The amount of money that is to be converted
     * @return The converted amount of money
     */
    public double convert(String fromCurrency, String toCurrency, double amount){
        System.out.println("Converting " + String.format("%.2f", amount) + " " + fromCurrency + " to " + toCurrency);
        
        switch (fromCurrency) {
            case "SGD":
                if(toCurrency == "USD"){
                    return amount * sgdToUsdRate;
                }
                if(toCurrency == "JPY"){
                    return amount * sgdToJpyRate;
                }
                break;
            
            case "USD":
                if(toCurrency == "SGD"){
                    return amount * usdToSgdRate;
                }
                if(toCurrency == "JPY"){
                    return amount * usdToJpyRate;
                }
                break;
            
            case "JPY":
                if(toCurrency == "SGD"){
                    return amount * jpyToSgdRate;
                }
                if(toCurrency == "USD"){
                    return amount * jpyToUsdRate;
                }
                break;
            default:
                System.out.println("Error in converting");
                break;
        }
        return amount;
    }

    /**
     * Display the current exchange rates
     */
    public void displayRates(){
        System.out.println("Exchange Rates:");
        System.out.println("1 SGD = " + getSgdToUsdRate() + " USD = " + getSgdToJpyRate() + " JPY");
        System.out.println("1 USD = " + getUsdToSgdRate() + " SGD = " + getUsdToJpyRate() + " JPY");
        System.out.println("1 JPY = " + getJpyToSgdRate() + " SGD = " + getJpyToUsdRate() + " USD");
    }

    /**
     * Loads up the exchange rates from the market
     * (Currently the rates are coded in, unsure if possible/allowed to source the rate from Internet)
     */
    public void getMarketRate(){
        setSgdToUsdRate(0.75);
        setSgdToJpyRate(110);
        setUsdToSgdRate(1.24);
        setJpyToSgdRate(0.009);
        setUsdToJpyRate(150);
        setJpyToUsdRate(0.0067);
    }

    /**
     * Gets the SGD to USD Rate
     * 
     * @return The SGD to USD rate
     */
    public double getSgdToUsdRate(){
        return sgdToUsdRate;
    }

    /**
     * Gets the USD to SGD Rate
     * 
     * @return The USD to SGD rate
     */
    public double getUsdToSgdRate(){
        return usdToSgdRate;
    }

    /**
     * Gets the SGD to USD Rate
     * 
     * @return The SGD to USD rate
     */
    public double getSgdToJpyRate(){
        return sgdToJpyRate;
    }

    /**
     * Gets the JPY to SGD Rate
     * 
     * @return The JPY to SGD rate
     */
    public double getJpyToSgdRate(){
        return jpyToSgdRate;
    }

    /**
     * Gets the USD to JPY Rate
     * 
     * @return The USD to JPY rate
     */
    public double getUsdToJpyRate(){
        return usdToJpyRate;
    }

    /**
     * Gets the JPY to USD Rate
     * 
     * @return The JPY to USD rate
     */
    public double getJpyToUsdRate(){
        return jpyToUsdRate;
    }

    /**
     * Sets the SGD to USD Rate
     * 
     * @param rate The new rate to be set
     */
    public void setSgdToUsdRate(double rate){
        sgdToUsdRate = rate;
    }

    /**
     * Sets the USD to SGD Rate
     * 
     * @param rate The new rate to be set
     */
    public void setUsdToSgdRate(double rate){
        usdToSgdRate = rate;
    }

    
    /**
     * Sets the SGD to JPY Rate
     * 
     * @param rate The new rate to be set
     */
    public void setSgdToJpyRate(double rate){
        sgdToJpyRate = rate;
    }

    /**
     * Sets the JPY to SGD Rate
     * 
     * @param rate The new rate to be set
     */
    public void setJpyToSgdRate(double rate){
        jpyToSgdRate = rate;
    }

    /**
     * Sets the USD to JPY Rate
     * 
     * @param rate The new rate to be set
     */
    public void setUsdToJpyRate(double rate){
        usdToJpyRate = rate;
    }

    /**
     * Sets the JPY to USD Rate
     * 
     * @param rate The new rate to be set
     */
    public void setJpyToUsdRate(double rate){
        jpyToUsdRate = rate;
    }
}
