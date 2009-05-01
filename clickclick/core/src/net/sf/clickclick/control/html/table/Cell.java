package net.sf.clickclick.control.html.table;

import org.apache.click.Control;
import org.apache.click.control.AbstractContainer;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.Text;
import org.apache.click.util.ClickUtils;

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

    public Cell(Control control) {
        add(control);
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

    public String getLabel() {
        return ClickUtils.toLabel(getName());
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
