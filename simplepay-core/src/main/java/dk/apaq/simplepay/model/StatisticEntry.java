
package dk.apaq.simplepay.model;

import dk.apaq.framework.common.beans.finance.PaymentType;

/**
 * Javadoc
 */
public class StatisticEntry {

    private String merchantId;
    private int hour;
    private long amount;
    private int americanExpressCount;
    private int dankortCount;
    private int dinersCount;
    private int jcbCount;
    private int mastercardCount;
    private int visaCount;
    private int visaElectronCount;
    private int unknownCount;

    public StatisticEntry(String merchantId, int hour, long amount, int americanExpressCount, int dankortCount, int dinersCount, int jcbCount, int mastercardCount, int visaCount, int visaElectronCount, int unknownCount) {
        this.merchantId = merchantId;
        this.hour = hour;
        this.amount = amount;
        this.americanExpressCount = americanExpressCount;
        this.dankortCount = dankortCount;
        this.dinersCount = dinersCount;
        this.jcbCount = jcbCount;
        this.mastercardCount = mastercardCount;
        this.visaCount = visaCount;
        this.visaElectronCount = visaElectronCount;
        this.unknownCount = unknownCount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public int getHour() {
        return hour;
    }

    public long getAmount() {
        return amount;
    }

    public int getAmericanExpressCount() {
        return americanExpressCount;
    }

    public int getDankortCount() {
        return dankortCount;
    }

    public int getDinersCount() {
        return dinersCount;
    }

    public int getJcbCount() {
        return jcbCount;
    }

    public int getMastercardCount() {
        return mastercardCount;
    }

    public int getVisaCount() {
        return visaCount;
    }

    public int getVisaElectronCount() {
        return visaElectronCount;
    }

    public int getUnknownCount() {
        return unknownCount;
    }
    
}
