/**
 *
 */
package edu.leti.jbpm.stub;

import org.jbpm.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Allows skipping mail server setup and configuration for testing purposes
 *
 * @author eav 2011
 */
public class MailStub extends Mail {
    private static final Logger log = LoggerFactory.getLogger( MailStub.class );

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
