/**
 * 
 */
package edu.leti.jbpm.handlers;

import org.apache.log4j.Logger;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.exe.ExecutionContext;

import edu.leti.jbpm.Variables;

/**
 * @author eav 2011
 */
public class RedirectToPayPortal implements ActionHandler {
    private static final Logger log = Logger.getLogger( RedirectToPayPortal.class );

    @Override
    public void execute( final ExecutionContext executionContext ) {
        final long productId = (Long) executionContext.getVariable( Variables.PRODUCT_ID );
        final String pnr = (String) executionContext.getVariable( Variables.PNR );
        log.info( "redirecting to pay portal with product=" + productId + ", pnr=" + pnr );
    }
}
