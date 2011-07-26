/**
 * 
 */
package edu.leti.jbpm;

/**
 * @author eav 2011
 */
public interface Transitions {
    String PNR_RECEIVED = "PNR received";
    String PNR_REJECTED = "PNR rejected";

    String PAYMENT_REJECTED = "payment rejected";
    String PAYMENT_COMPLETE = "payment complete";

    String VOUCHER_RECEIVED = "Voucher received";
    String VOUCHER_REJECTED = "Voucher rejected";
}
