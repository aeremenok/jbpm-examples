/**
 *
 */
package edu.leti.jbpm.stub;

/**
 * Introduces errors into process handling, depending on processId
 *
 * @author eav 2011
 */
public enum ChaosMonkey {
    M;

    public static final long GOOD_PRODUCT_ID = 1L;
    public static final long NO_PNR = 2L;
    public static final long NO_VOUCHER = 3L;

    public boolean shouldRejectPnr( final long productId ) {
        return productId == NO_PNR;
    }

    public boolean shouldRejectVoucher( final long productId ) {
        return productId == NO_VOUCHER;
    }
}
