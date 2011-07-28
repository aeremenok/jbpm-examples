/**
 * 
 */
package edu.leti.jbpm.handlers;

import java.util.concurrent.TimeUnit;

import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.leti.jbpm.Variables;

/**
 * @author eav 2011
 */
public enum PayPortal {
    P;
    private static final Logger log = LoggerFactory.getLogger( PayPortal.class );

    public void doRedirect( final ProcessInstance instance ) throws InterruptedException {
        final String currentNodeName = instance.getRootToken().getNode().getName();
        assert currentNodeName.equals( "Awaiting payment" ) : currentNodeName;

        final ContextInstance contextInstance = instance.getContextInstance();
        final long productId = (Long) contextInstance.getVariable( Variables.PRODUCT_ID );
        final String pnr = (String) contextInstance.getVariable( Variables.PNR );
        log.info( "redirecting to pay portal with product={}, pnr={}", productId, pnr );

        TimeUnit.SECONDS.sleep( 5 );
    }
}
