package net.sf.clickclick.control.html.table;

import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Bob Schellink
 */
public class HeaderCell extends Cell {

    private String headerTitle;

    public HeaderCell() {
    }

    public HeaderCell(String name) {
        super(name);
    }

    public String getHeaderTitle() {
        if (headerTitle == null) {
            headerTitle = getMessage(getName() + ".headerTitle");
        }
        if (headerTitle == null) {
            headerTitle = ClickUtils.toLabel(getName());
        }
        return headerTitle;
    }

    public String getTag() {
        return "th";
    }

    public void render(HtmlStringBuffer buffer) {
        // If no child controls have been added and name is not blank, create a
        // default child from the Cell name
        if (!hasControls() && StringUtils.isNotBlank(getName())) {
            setText(getHeaderTitle());
        }

        super.render(buffer);
    }
}
