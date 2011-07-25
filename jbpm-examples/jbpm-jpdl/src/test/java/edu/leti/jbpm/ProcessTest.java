package edu.leti.jbpm;

import static org.testng.Assert.assertEquals;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/** @author eav Date: 23.07.11 Time: 0:08 */
public class ProcessTest {
    private static final Logger log = Logger.getLogger( ProcessTest.class );
    private JbpmConfiguration configuration;

    @BeforeClass
    public void setUp() {
        configuration = JbpmConfiguration.getInstance( "jbpm.cfg.xml" );

        deployProcessDefinition( configuration );

        configuration.startJobExecutor();
    }

    @AfterClass
    public void tearDown() {
        try {
            configuration.getJobExecutor().stopAndJoin();
        } catch ( final Exception e ) {
            log.error( e, e );
        }

        configuration.close();
    }

    @Test
    public void process() throws InterruptedException {
        final long processId = startProcess();
        TimeUnit.SECONDS.sleep( 5 );
        completePayment( processId );

        TimeUnit.SECONDS.sleep( 5 );
        final JbpmContext context = configuration.createJbpmContext();
        try {
            final ProcessInstance instance = context.loadProcessInstance( processId );

            assertEquals( instance.getContextInstance().getVariable( Variables.VOUCHER_ID ), 1L );
            assert instance.hasEnded() : instance.getRootToken().getNode().getName();
        } finally {
            context.close();
        }
    }

    private void completePayment( final long processId ) {
        final JbpmContext context = configuration.createJbpmContext();
        try {
            final ProcessInstance instance = context.loadProcessInstance( processId );

            assertEquals( instance.getContextInstance().getVariable( Variables.PNR ), "PNR1" );
            assertEquals( instance.getRootToken().getNode().getName(), "Redirect to payportal" );

            instance.signal( Transitions.PAYMENT_COMPLETE );
        } finally {
            context.close();
        }
    }

    private long startProcess() {
        final JbpmContext context = configuration.createJbpmContext();

        final Map<String, Object> parameters = ImmutableMap.<String, Object> of( Variables.PRODUCT_ID,
            1L,
            Variables.CUSTOMER_EMAIL,
            "john.smith@example.com" );

        try {
            final ProcessInstance instance = context.newProcessInstance( "travel" );
            instance.getContextInstance().addVariables( parameters );
            instance.signal();

            return instance.getId();
        } finally {
            context.close();
        }
    }

    private void deployProcessDefinition( final JbpmConfiguration configuration ) {
        final ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource( "travel.jpdl.xml" );
        final JbpmContext context = configuration.createJbpmContext();
        try {
            context.deployProcessDefinition( processDefinition );
        } finally {
            context.close();
        }
    }
}
