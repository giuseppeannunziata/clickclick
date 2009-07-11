package net.sf.clickclick.examples.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A book class.
 */
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

    private String name;
    
    private String author;

    private String isbn;
    
    private List categories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List getCategories() {
        if (categories == null) {
            categories = new ArrayList();
        }
        return categories;
    }

    public void setCategories(List categories) {
        this.categories = categories;
    }
}
