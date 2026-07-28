package com.example.fraud_detection.Transaction_service.entity;


import java.util.Optional;
public class TransactionEnums {
    
   public enum Currency {
    USD("United States Dollar", "$"),
    EUR("Euro", "€"),
    JPY("Japanese Yen", "¥"),
    GBP("British Pound Sterling", "£"),
    AUD("Australian Dollar", "$"),
    CAD("Canadian Dollar", "CA$"),
    CHF("Swiss Franc", "CHF"),
    CNY("Chinese Yuan", "¥"),
    HKD("Hong Kong Dollar", "$"),
    NZD("New Zealand Dollar", "$");

    private final String displayName;
    private final String symbol;

    // Constructor
    Currency(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    // Getters
    public String getDisplayName() {
        return this.displayName;
    }

    public String getSymbol() {
        return this.symbol;
    }

    // Helper method to print clean information
    public String getDescription() {
        return this.name() + " - " + this.displayName + " (" + this.symbol + ")";
    }


    public static Optional<Currency> fromString(String rawString) {
        if (rawString == null) return Optional.empty();

            String s = rawString.trim().toUpperCase();

            // match enum name eg unitest states dollar
            for (Currency c : values()) {
                if (c.name().equals(s)) return Optional.of(c);
            }
            //match symbol $
            for (Currency c : values()) {
                if (c.getSymbol().equals(rawString.trim())) return Optional.of(c);
            }

            // display name (case-insensitive)
            for (Currency c : values()) {
                if (c.getDisplayName().equalsIgnoreCase(rawString.trim())) return Optional.of(c);
            }

       return Optional.empty();
}

}

public enum Country {
    USA,
    CAN,
    GBR,
    AUS,
    DEU,
    FRA,
    JPN,
    IND,
    BRA,
    MEX;

    public static Country fromString(String rawString){
        if(rawString != null){
            String cleanRawString = rawString.trim().toUpperCase();
            for(Country c: Country.values()){
                if(c.name().equals(cleanRawString)){
                    return c;
                }
            }

        }
        return null;
    }
}

public enum MerchantCategory {
    GROCERY,
    RESTAURANT,
    GAS_STATION,
    ELECTRONICS,
    CLOTHING,
    ENTERTAINMENT,
    TRAVEL,
    HEALTHCARE,
    UTILITIES,
    AUTOMOTIVE;

     public static MerchantCategory fromString(String rawString){
        if(rawString != null){
            String cleanRawString = rawString.trim().toUpperCase();
            for(MerchantCategory c: MerchantCategory.values()){
                if(c.name().equals(cleanRawString)){
                    return c;
                }
            }

        }
        return null;
    }




}
   
public enum TransactionStatus{
    PENDING, 
    APPROVED,
    FLAGGED,
    DECLINED;


     public static TransactionStatus fromString(String rawString){
        if(rawString != null){
            String cleanRawString = rawString.trim().toUpperCase();
            for(TransactionStatus c: TransactionStatus.values()){
                if(c.name().equals(cleanRawString)){
                    return c;
                }
            }

        }
        return null;
    }
}
}
