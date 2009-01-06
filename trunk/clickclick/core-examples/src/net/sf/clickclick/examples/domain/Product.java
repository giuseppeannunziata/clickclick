package net.sf.clickclick.examples.domain;

import java.io.Serializable;

/**
 *
 * @author Bob Schellink
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    public Product(String name) {
        setName(name);
    }
    
    public Product() {
        
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return "Product -> [" + getName() + "]";
    }
}

