package edu.leti.jbpm.handlers;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import edu.leti.jbpm.Transitions;
import edu.leti.jbpm.Variables;
import edu.leti.jbpm.stub.ChaosMonkey;

/**
 * @author eav 2011
 */
public class RequestPNR implements ActionHandler {
    private static final Logger log = Logger.getLogger( RequestPNR.class );

    @Override
    public void execute( final ExecutionContext executionContext ) {
        final long productId = (Long) executionContext.getVariable( Variables.PRODUCT_ID );
        final String pnr = requestPnr( productId );

        if ( pnr != null ) {
            executionContext.setVariable( Variables.PNR, pnr );
            executionContext.leaveNode( Transitions.PNR_RECEIVED );
        } else {
            executionContext.leaveNode( Transitions.PNR_REJECTED );
        }
    }

    private String requestPnr( final long productId ) {
        log.info( "sending PNR request for product=" + productId );
        try {
            TimeUnit.SECONDS.sleep( 1 );
        } catch ( final InterruptedException e ) {
            Thread.currentThread().interrupt();
        }

        final String pnr = ChaosMonkey.M.shouldRejectPnr( productId )
            ? null
            : "PNR1";
        log.info( "received PNR=" + pnr );

        return pnr;
    }
}
