/**
 * 
 */
package edu.leti.jbpm.handlers;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import edu.leti.jbpm.Transitions;
import edu.leti.jbpm.Variables;
import edu.leti.jbpm.domain.Voucher;
import edu.leti.jbpm.stub.ChaosMonkey;

/**
 * @author eav 2011
 */
public class RequestVoucher implements ActionHandler {
    private static final Logger log = Logger.getLogger( RequestVoucher.class );

    @Override
    public void execute( final ExecutionContext executionContext ) {
        final long productId = (Long) executionContext.getVariable( Variables.PRODUCT_ID );
        final String pnr = (String) executionContext.getVariable( Variables.PNR );

        final Voucher voucher = requestVoucher( productId, pnr );
        if ( voucher != null ) {
            executionContext.setVariable( Variables.VOUCHER_ID, voucher.getId() );
            executionContext.leaveNode( Transitions.VOUCHER_RECEIVED );
        } else {
            executionContext.leaveNode( Transitions.VOUCHER_REJECTED );
        }
    }

    private Voucher requestVoucher( final long productId, final String pnr ) {
        log.info( "requesting voucher for product=" + productId + ", pnr=" + pnr );
        try {
            TimeUnit.SECONDS.sleep( 1 );
        } catch ( final InterruptedException e ) {
            Thread.currentThread().interrupt();
        }

        final Voucher voucher = ChaosMonkey.M.shouldRejectVoucher( productId )
            ? null
            : new Voucher( 1L, "<html>Hotel Florida</html>" );

        log.info( "received voucher " + voucher );
        return voucher;
    }
}
