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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.click.Control;
import net.sf.click.MockContext;
import net.sf.click.Page;
import net.sf.click.control.TextField;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.Row;

/**
 *
 * @author Bob Schellink
 */
public class DockPanel extends AbstractTablePanel {

    public static final int CENTER = 0;

    public static final int NORTH = 1;

    public static final int EAST = 2;

    public static final int SOUTH = 3;

    public static final int WEST = 4;

    protected List dockedControls = new ArrayList();

    /** Default colspan value is 1 which caters for center control. */
    protected int dockColspan = 1;
    
    public DockPanel() {
    }

    public DockPanel(String name) {
        super(name);
    }

    public Control add(Control control, int direction) {
        ControlHolder holder = new ControlHolder();
        holder.control = control;
        holder.direction = direction;
        // Need to increase the colspan if controls are docked to west or east
        if (direction == WEST || direction == EAST) {
            dockColspan++;
        }
        dockedControls.add(holder);
        /*
        Row row = null;
        if (!table.hasControls()) {
        row = new Row();
        table.add(row);
        } else {
        row = (Row) table.getControls().get(0);
        }
        
        row.add(cell);
        cell.add(control);*/
        return control;
    }

    public Control add(Control control) {
        return add(control, CENTER);
    }

    public boolean remove(Control control) {
        Cell cell = getCell(control);
        return table.remove(cell);
    }

    public void render(HtmlStringBuffer buffer) {
        assemblePanel();
        super.render(buffer);
    }

    protected void init() {
        super.init();
        setHorizontalAlignment(ALIGN_CENTER);
    }
    
    protected void assemblePanel() {
        Row middleRow = new Row();
        Cell centerCell = createCell();
        table.add(middleRow);
        middleRow.add(centerCell);

        int northCount = 0;
        int westCount = 0;
        for (Iterator it = dockedControls.iterator(); it.hasNext();) {
            ControlHolder holder = (ControlHolder) it.next();
            switch (holder.direction) {
                case NORTH: {
                    Row row = new Row();
                    table.insert(row, northCount);
                    Cell cell = createCell();
                    cell.setAttribute("colspan", Integer.toString(dockColspan));
                    cell.add(holder.control);
                    row.add(cell);
                    northCount++;
                    break;
                }
                case SOUTH: {
                    Row row = new Row();
                    table.insert(row, northCount + 1);
                    Cell cell = createCell();
                    cell.setAttribute("colspan", Integer.toString(dockColspan));
                    cell.add(holder.control);
                    row.add(cell);
                    break;
                }
                case EAST: {
                    Cell cell = createCell();
                    cell.add(holder.control);
                    middleRow.insert(cell, westCount + 1);
                    break;
                }
                case WEST: {
                    Cell cell = createCell();
                    cell.add(holder.control);
                    middleRow.insert(cell, westCount);
                    westCount++;
                    break;
                }
                default: {
                    centerCell.add(holder.control);
                    break;
                }
            }
        }
    }

    protected Cell createCell() {
        Cell cell = new Cell();
        cell.setStyle("text-align", getHorizontalAlignment());
        cell.setStyle("vertical-align", getVerticalAlignment());
        return cell;
    }

    public static void main(String[] args) {
        MockContext.initContext();
        Page page = new Page();
        DockPanel panel = new DockPanel("panel");
        page.addControl(panel);
        panel.setSpacing(5);
        Control field = (Control) panel.add(new TextField("north1"), NORTH);
        field = (Control) panel.add(new TextField("north2"), NORTH);

        field = (Control) panel.add(new TextField("west1"), WEST);
        field = (Control) panel.add(new TextField("west2"), WEST);
        
        field = (Control) panel.add(new TextField("center"));
        
        field = (Control) panel.add(new TextField("east1"), EAST);
        field = (Control) panel.add(new TextField("east2"), EAST);
        
        field = (Control) panel.add(new TextField("south1"), SOUTH);
        field = (Control) panel.add(new TextField("south2"), SOUTH);
        
        //Cell cell = panel.getCell(field);
        //cell.setAttribute("class", "cell");
        System.out.println(panel);
    }

    class ControlHolder {

        public Control control;

        public int direction;
    }
}
