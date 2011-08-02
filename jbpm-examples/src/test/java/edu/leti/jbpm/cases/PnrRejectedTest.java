/**
 * 
 */
package edu.leti.jbpm.cases;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.testng.annotations.Test;

import edu.leti.jbpm.ProcessAssertions;
import edu.leti.jbpm.AbstractProcessTest;
import edu.leti.jbpm.Variables;
import edu.leti.jbpm.stub.ChaosMonkey;

/**
 * @author eav 2011
 */
public class PnrRejectedTest extends AbstractProcessTest {
    private long processId;

    @Test
    public void startProcess() throws Exception {

        final JbpmContext context = configuration.createJbpmContext();

        try {
            final ProcessInstance instance = context.newProcessInstance( "travel" );
            final ContextInstance contextInstance = instance.getContextInstance();

            contextInstance.setVariable( Variables.PRODUCT_ID, ChaosMonkey.NO_PNR );

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
                assertEquals( freshInstance.getRootToken().getNode().getName(), "Product booking failed" );
                assert freshInstance.hasEnded();
            }
        } );
    }

}
