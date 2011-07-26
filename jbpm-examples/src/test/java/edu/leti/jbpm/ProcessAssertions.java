/**
 * 
 */
package edu.leti.jbpm;

import org.jbpm.graph.exe.ProcessInstance;

/**
 * @author eav 2011
 */
public interface ProcessAssertions {
    void makeAssertions( ProcessInstance freshInstance ) throws Exception;
}
