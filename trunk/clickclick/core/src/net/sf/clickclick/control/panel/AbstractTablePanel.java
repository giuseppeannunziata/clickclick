/*
 * Copyright 2008 Bob Schellink
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.control.panel;

import net.sf.click.Control;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;

/**
 *
 * @author Bob Schellink
 */
public abstract class AbstractTablePanel extends SimplePanel {

    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_CENTER = "center";
    public static final String ALIGN_RIGHT = "right";

    public static final String ALIGN_TOP = "top";
    public static final String ALIGN_MIDDLE = "middle";
    public static final String ALIGN_BOTTOM = "bottom";

    protected String verticalAlignment = ALIGN_TOP;

    protected String horizontalAlignment = ALIGN_LEFT;

    protected HtmlTable table = new HtmlTable();

    public AbstractTablePanel() {
        init();
    }

    public AbstractTablePanel(String name) {
        super(name);
        init();
    }

    public abstract Control add(Control control);

    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by this Panel");
    }

    public abstract boolean remove(Control control);

    public void setHorizontalAlignment(String horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public String getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setVerticalAlignment(String verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getVerticalAlignment() {
        return verticalAlignment;
    }

    public int getSpacing() {
        return table.getCellspacing();
    }

    public void setSpacing(int spacing) {
        table.setCellspacing(spacing);
    }

    public void setSize(Control control, String width, String height) {
        Cell cell = getCell(control);
        if (cell == null) {
            return;
        }
        cell.setAttribute("width", width);
        cell.setAttribute("height", height);
    }
    
    public Row getRow(Control control) {
        Cell cell = getCell(control);
        if (cell == null) {
            return null;
        }
        return (Row) cell.getParent();
    }

    public Cell getCell(Control control) {
        return (Cell) control.getParent();
    }

    // ------------------------------------------------------ Protected Methods

    protected void init() {
        super.insert(table, 0);
        table.setAttribute("cellpadding", "0");
    }
}
