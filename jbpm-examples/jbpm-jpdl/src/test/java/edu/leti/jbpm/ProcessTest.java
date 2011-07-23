package edu.leti.jbpm;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jbpm.JbpmConfiguration;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

/** @author eav Date: 23.07.11 Time: 0:08 */
public class ProcessTest {
    private static final Logger log = Logger.getLogger( ProcessTest.class );

    @Test
    public void process() {

        final JbpmConfiguration configuration = JbpmConfiguration.getInstance( "jbpm.cfg.xml" );
        configuration.startJobExecutor();
        //        final JbpmContext context = configuration.createJbpmContext();

        final ProcessDefinition processDefinition = ProcessDefinition.parseXmlResource( "travel.jpdl.xml" );
        //        context.deployProcessDefinition( processDefinition );

        final Map<String, Object> parameters = ImmutableMap.<String, Object> of( Variables.PRODUCT_ID, 1L );

        final ProcessInstance instance = processDefinition.createProcessInstance( parameters );
        instance.signal();

        //        instance.signal( "payment complete" );
    }
}
