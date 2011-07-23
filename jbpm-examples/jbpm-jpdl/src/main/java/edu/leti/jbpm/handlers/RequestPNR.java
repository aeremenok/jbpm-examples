package edu.leti.jbpm.handlers;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import edu.leti.jbpm.Variables;

/**
 * @author eav 2011
 */
public class RequestPNR implements ActionHandler {
    private static final Logger log = Logger.getLogger( RequestPNR.class );

    @Override
    public void execute( final ExecutionContext executionContext ) {
        final Long productId = (Long) executionContext.getVariable( Variables.PRODUCT_ID );
        log.info( "sending PNR request for product=" + productId );

        final String pnr = requestPnr( productId );

        executionContext.setVariable( Variables.PNR, pnr );
    }

    private String requestPnr( final Long productId ) {
        try {
            TimeUnit.SECONDS.sleep( 5 );
        } catch ( final InterruptedException e ) {
            Thread.currentThread().interrupt();
        }

        return "PNR1";
    }
}
