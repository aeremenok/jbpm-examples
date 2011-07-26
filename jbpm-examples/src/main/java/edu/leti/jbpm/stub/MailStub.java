/**
 * 
 */
package edu.leti.jbpm.stub;

import org.apache.log4j.Logger;
import org.jbpm.mail.Mail;

/**
 * @author eav 2011
 */
public class MailStub extends Mail {
    private static final Logger log = Logger.getLogger( MailStub.class );

    public MailStub() {
        super();
    }

    public MailStub( final String template,
                     final String actors,
                     final String to,
                     final String bccActors,
                     final String bcc,
                     final String subject,
                     final String text ) {
        super( template, actors, to, bccActors, bcc, subject, text );
    }

    public MailStub( final String template,
                     final String actors,
                     final String to,
                     final String subject,
                     final String text ) {
        super( template, actors, to, subject, text );
    }

    @Override
    public void send() {
        try {
            super.send();
        } catch ( final Throwable e ) {
            log.error( "couldn't really send mail because of " + e );
        }
    }
}
