/**
 * 
 */
package edu.leti.jbpm;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author eav 2011
 */
public abstract class AbstractProcessTest {
    private static final Logger log = LoggerFactory.getLogger( AbstractProcessTest.class );
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
            log.error( e.getMessage(), e );
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
