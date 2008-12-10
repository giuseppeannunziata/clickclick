package net.sf.clickclick.control.html.table;

/**
 *
 * @author Bob Schellink
 */
public class HeaderCell extends Cell {

    public HeaderCell() {
    }
    
    public HeaderCell(String name) {
        super(name);
    }
    
     public String getTag() {
        return "th";
    }
}
