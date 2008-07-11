package net.sf.clickclick.control.html.table;

import net.sf.click.Control;
import net.sf.click.control.AbstractContainer;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.Text;

/**
 *
 * @author Bob Schellink
 */
public class Cell extends AbstractContainer {

    //private String text;
    
    public Cell() {
    }
    
    public Cell(String name) {
        super(name);
    }
    
    public void setText(String str) {
        Text text = new Text(str);
        add(text);
    }

    public void setText(Text text) {
        add(text);
    }
    
    public String getTag() {
        return "td";
    }
    
    public Row getRow() {
        return (Row) getParent();
    }
    
    public void render(HtmlStringBuffer buffer) {
        if (getTag() != null) {
            renderTagBegin(getTag(), buffer);
            buffer.closeTag();
            renderContent(buffer);
            renderTagEnd(getTag(), buffer);
        } else {
            if(hasControls()) {
                renderContent(buffer);
            }
        }
    }
    
    protected void renderChildren(HtmlStringBuffer buffer) {
        if(hasControls()) {
            for(int i = 0; i < getControls().size(); i++) {
                Control control = (Control) getControls().get(i);
                control.render(buffer);
            }
        }
    }

     public static void main(String[] args) {
        Cell cell = new Cell("mycell");
        System.out.println(cell);
        System.out.println(cell.getRow());
    }
}