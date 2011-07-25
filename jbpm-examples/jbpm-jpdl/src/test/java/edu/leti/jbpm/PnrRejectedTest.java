/**
 * 
 */
package edu.leti.jbpm;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.testng.annotations.Test;

/**
 * @author eav 2011
 */
public class PnrRejectedTest extends ProcessTest {
    @SuppressWarnings( "unused" )
    private static final Logger log = Logger.getLogger( PnrRejectedTest.class );

    private long processId;

    @Test
    public void startProcess() throws Exception {
        final JbpmContext context = configuration.createJbpmContext();

        try {
            final ProcessInstance instance = context.newProcessInstance( "travel" );
            final ContextInstance contextInstance = instance.getContextInstance();

            contextInstance.setVariable( Variables.PRODUCT_ID, 2L );

            instance.signal();

            processId = instance.getId();
        } finally {
            context.close();
        }

        TimeUnit.SECONDS.sleep( 5 );
        makeAssertions( processId, new ProcessAssertions() {
            @Override
            public void makeAssertions( final ProcessInstance freshInstance ) {
                final ContextInstance contextInstance = freshInstance.getContextInstance();
                assert !contextInstance.hasVariable( Variables.PNR ) : contextInstance.getVariable( Variables.PNR );
                assert freshInstance.hasEnded();
                assertEquals( freshInstance.getRootToken().getNode().getName(), "Product booking failed" );
            }
        } );
    }

}
