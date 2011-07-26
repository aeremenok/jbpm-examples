package edu.leti.jbpm;

import static org.testng.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.testng.annotations.Test;

import edu.leti.jbpm.stub.ChaosMonkey;

/** @author eav Date: 23.07.11 Time: 0:08 */
public class SuccessfulProcessTest extends ProcessTest {
    @SuppressWarnings( "unused" )
    private static final Logger log = Logger.getLogger( SuccessfulProcessTest.class );

    private long processId;

    @Test
    public void startProcess() throws Exception {
        final JbpmContext context = configuration.createJbpmContext();

        try {
            final ProcessInstance instance = context.newProcessInstance( "travel" );
            final ContextInstance contextInstance = instance.getContextInstance();

            contextInstance.setVariable( Variables.PRODUCT_ID, ChaosMonkey.GOOD_PRODUCT_ID );
            contextInstance.setVariable( Variables.CUSTOMER_EMAIL, "john.smith@example.com" );

            instance.signal();

            processId = instance.getId();
        } finally {
            context.close();
        }

        TimeUnit.SECONDS.sleep( 5 );
        makeAssertions( processId, new ProcessAssertions() {
            @Override
            public void makeAssertions( final ProcessInstance freshInstance ) {
                assertEquals( freshInstance.getContextInstance().getVariable( Variables.PNR ), "PNR1" );
                assertEquals( freshInstance.getRootToken().getNode().getName(), "Redirect to payportal" );
            }
        } );
    }

    @Test( dependsOnMethods = "startProcess" )
    public void completePayment() throws Exception {
        final JbpmContext context = configuration.createJbpmContext();
        try {
            final ProcessInstance instance = context.loadProcessInstance( processId );
            instance.signal( Transitions.PAYMENT_COMPLETE );
        } finally {
            context.close();
        }

        TimeUnit.SECONDS.sleep( 5 );
        makeAssertions( processId, new ProcessAssertions() {
            @Override
            public void makeAssertions( final ProcessInstance freshInstance ) {
                assertEquals( freshInstance.getContextInstance().getVariable( Variables.VOUCHER_ID ), 1L );
                assert freshInstance.hasEnded();
                assertEquals( freshInstance.getRootToken().getNode().getName(), "Product booked successfully" );
            }
        } );
    }
}
