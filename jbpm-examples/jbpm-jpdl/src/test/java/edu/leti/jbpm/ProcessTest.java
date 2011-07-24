package edu.leti.jbpm;

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
    public void tearDown() throws InterruptedException {
        try {
            configuration.getJobExecutor().stop();
        } catch ( final Exception e ) {
            log.error( e, e );
        }

        //        try {
        //            configuration.dropSchema();
        //        } catch ( final Exception e ) {
        //            log.error( e, e );
        //        }
        TimeUnit.SECONDS.sleep( 5 );
        configuration.close();
    }

    @Test
    public void process() throws InterruptedException {
        final JbpmContext context = configuration.createJbpmContext();

        final Map<String, Object> parameters = ImmutableMap.<String, Object> of( Variables.PRODUCT_ID, 1L );

        try {
            final ProcessInstance instance = context.newProcessInstance( "travel" );
            instance.getContextInstance().addVariables( parameters );
            instance.signal();

            TimeUnit.SECONDS.sleep( 5 );
            assert instance.getContextInstance().hasVariable( Variables.PNR );
            //            log.debug( Joiner.on( "\n" ).join( activeNodes ) );
            //            final Node currentNode = instance.getRootToken().getNode();
            //            assertEquals( currentNode.getName(), "Redirect to payportal" );
        } finally {
            context.close();
        }

        //        instance.signal( "payment complete" );
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
