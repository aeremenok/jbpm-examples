/**
 * 
 */
package edu.leti.jbpm;

import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.testng.annotations.Test;

import edu.leti.jbpm.handlers.PayPortal;
import edu.leti.jbpm.stub.ChaosMonkey;

/**
 * @author eav 2011
 */
public class VoucherRejectedTest extends ProcessTest {
    private long processId;

    @Test
    public void startProcess() throws Exception {
        final JbpmContext context = configuration.createJbpmContext();

        try {
            final ProcessInstance instance = context.newProcessInstance( "travel" );
            final ContextInstance contextInstance = instance.getContextInstance();

            contextInstance.setVariable( Variables.PRODUCT_ID, ChaosMonkey.NO_VOUCHER );
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
            }
        } );
    }

    @Test( dependsOnMethods = "startProcess" )
    public void completePayment() throws Exception {
        final JbpmContext context = configuration.createJbpmContext();
        try {
            final ProcessInstance instance = context.loadProcessInstance( processId );

            PayPortal.P.doRedirect( instance );

            instance.signal( Transitions.PAYMENT_COMPLETE );
        } finally {
            context.close();
        }

        TimeUnit.SECONDS.sleep( 5 );
        makeAssertions( processId, new ProcessAssertions() {
            @Override
            public void makeAssertions( final ProcessInstance freshInstance ) {
                final ContextInstance ctx = freshInstance.getContextInstance();

                assert !ctx.hasVariable( Variables.VOUCHER_ID ) : ctx.getVariable( Variables.VOUCHER_ID );
                assertEquals( freshInstance.getRootToken().getNode().getName(), "Rollback the payment" );
            }
        } );
    }

    @Test( dependsOnMethods = "completePayment" )
    public void completePaymentRollbackTask() throws Exception {
        final JbpmContext context = configuration.createJbpmContext();

        final String actorId = "Agent";
        try {
            @SuppressWarnings( "unchecked" )
            final List<TaskInstance> taskList = context.getTaskList( actorId );
            assertEquals( taskList.size(), 1 );

            final TaskInstance taskInstance = taskList.get( 0 );
            assertEquals( taskInstance.getName(), "Rollback the payment from admin console" );

            taskInstance.start();
            TimeUnit.SECONDS.sleep( 3 );
            taskInstance.end();
        } finally {
            context.close();
        }

        TimeUnit.SECONDS.sleep( 5 );
        makeAssertions( processId, new ProcessAssertions() {
            @Override
            public void makeAssertions( final ProcessInstance freshInstance ) {
                assertEquals( freshInstance.getRootToken().getNode().getName(), "Product booking failed" );
                assert freshInstance.hasEnded();
            }
        } );
    }
}
