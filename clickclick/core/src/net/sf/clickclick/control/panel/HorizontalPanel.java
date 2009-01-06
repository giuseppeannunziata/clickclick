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

import org.apache.click.Control;
import org.apache.click.MockContext;
import org.apache.click.Page;
import org.apache.click.control.TextField;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.Row;

/**
 *
 * @author Bob Schellink
 */
public class HorizontalPanel extends AbstractTablePanel {

    public HorizontalPanel() {
    }
    
    public HorizontalPanel(String name) {
        super(name);
    }

    public Control add(Control control) {
        Row row = null;
        if (!table.hasControls()) {
            row = new Row();
            table.add(row);
        } else {
            row = (Row) table.getControls().get(0);
        }
        
        Cell cell = new Cell();
        cell.setStyle("text-align", getHorizontalAlignment());
        cell.setStyle("vertical-align", getVerticalAlignment());
        row.add(cell);
        cell.add(control);
        return control;
    }

    public boolean remove(Control control) {
        Cell cell = getCell(control);
        return table.remove(cell);
    }

    public static void main(String[] args) {
        MockContext.initContext();
        Page page = new Page();
        HorizontalPanel panel = new HorizontalPanel("panel");
        page.addControl(panel);
        panel.setSpacing(5);
        Control field = (Control) panel.add(new TextField("text"));
        field = (Control) panel.add(new TextField("text"));
        Cell cell = panel.getCell(field);
        cell.setAttribute("class", "cell");
        System.out.println(panel);
    }
}
