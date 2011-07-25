/**
 * 
 */
package edu.leti.jbpm.domain;

import javax.persistence.Id;

/**
 * @author eav 2011
 */
public class Voucher {
    @Id
    private long id;
    private String body;

    public Voucher( final long id, final String body ) {
        super();
        this.id = id;
        this.body = body;
    }

    public Voucher() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody( final String body ) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Voucher [id=" + id + ", body=" + body + "]";
    }

}
