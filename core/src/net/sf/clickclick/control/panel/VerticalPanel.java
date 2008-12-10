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
import net.sf.click.MockContext;
import net.sf.click.Page;
import net.sf.click.control.TextField;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.Row;

/**
 *
 * @author Bob Schellink
 */
public class VerticalPanel extends AbstractTablePanel {

    public VerticalPanel() {
    }

    public VerticalPanel(String name) {
       super(name);
    }

    public Control add(Control control) {
        Row row = new Row();
        table.add(row);
        Cell cell = new Cell();
        cell.setStyle("text-align", getHorizontalAlignment());
        cell.setStyle("vertical-align", getVerticalAlignment());
        row.add(cell);
        cell.add(control);
        return control;
    }

    public boolean remove(Control control) {
        Row row = getRow(control);
        return table.remove(row);
    }

    public static void main(String[] args) {
        MockContext.initContext();
        Page page = new Page();
        VerticalPanel panel = new VerticalPanel("panel");
        page.addControl(panel);
        Control field = (Control) panel.add(new TextField("text"));
        field = (Control) panel.add(new TextField("text"));
        Cell cell = panel.getCell(field);
        cell.setAttribute("class", "cell");
        System.out.println(panel);
    }
}
