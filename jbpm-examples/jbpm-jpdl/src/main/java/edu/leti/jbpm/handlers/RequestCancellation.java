/**
 * 
 */
package edu.leti.jbpm.handlers;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import edu.leti.jbpm.Variables;

/**
 * @author eav 2011
 */
public class RequestCancellation implements ActionHandler {
    private static final Logger log = Logger.getLogger( RequestCancellation.class );

    @Override
    public void execute( final ExecutionContext executionContext ) {
        final long productId = (Long) executionContext.getVariable( Variables.PRODUCT_ID );
        final String pnr = (String) executionContext.getVariable( Variables.PNR );

        requestCancellation( productId, pnr );

        executionContext.leaveNode();
    }

    private void requestCancellation( final long productId, final String pnr ) {
        log.info( "requesting cancellation for product=" + productId + ", pnr=" + pnr );
        try {
            TimeUnit.SECONDS.sleep( 1 );
        } catch ( final InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
        log.info( "cancellation confirmed" );
    }
}
