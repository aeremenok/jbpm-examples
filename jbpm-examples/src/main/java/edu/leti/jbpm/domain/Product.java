package edu.leti.jbpm.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/** @author eav Date: 23.07.11 Time: 0:07 */
@Entity
public class Product {

    @Id
    private long id;

    public Product( final long id ) {
        super();
        this.id = id;
    }

    public Product() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId( final long id ) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + "]";
    }

}
