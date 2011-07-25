/**
 * 
 */
package edu.leti.jbpm;

import org.apache.log4j.Logger;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author eav 2011
 */
public class ProcessTest {
    private static final Logger log = Logger.getLogger( ProcessTest.class );

    protected JbpmConfiguration configuration;

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

    private void deployProcessDefinition( final JbpmConfiguration configuration ) {
        final ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource( "travel.jpdl.xml" );
        final JbpmContext context = configuration.createJbpmContext();
        try {
            context.deployProcessDefinition( processDefinition );
        } finally {
            context.close();
        }
    }

    protected void makeAssertions( final long processId, final ProcessAssertions assertions ) throws Exception {
        final JbpmContext context = configuration.createJbpmContext();
        try {
            final ProcessInstance processInstance = context.loadProcessInstance( processId );
            assertions.makeAssertions( processInstance );
        } finally {
            context.close();
        }
    }
}
