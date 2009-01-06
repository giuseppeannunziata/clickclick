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
package net.sf.clickclick.control.html.table;

import org.apache.click.MockContext;
import org.apache.click.control.AbstractContainer;
import org.apache.click.control.Container;
import org.apache.click.control.HiddenField;
import net.sf.clickclick.control.Html;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author Bob Schellink
 */
public class HtmlTable extends AbstractContainer {

    public HtmlTable() {
    }

    public HtmlTable(String name) {
        super(name);
    }

    public String getTag() {
        return "table";
    }
    
    public int getCellspacing() {
        String cellspacing = getAttribute("cellspacing");
        if (NumberUtils.isNumber(cellspacing)) {
            return NumberUtils.toInt(cellspacing);
        }
        return 0;
    }

    public void setCellspacing(int cellspacing) {
        setAttribute("cellspacing", Integer.toString(cellspacing));
    }

    public static void main(String[] args) {

        MockContext.initContext();

        HtmlTable table = new HtmlTable("table");
        Row row = new Row("Row1");
        table.add(row);
        row = new Row("Row2");
        table.add(row);
        row = new Row("Row3");
        table.add(row);
        row = new Row("Row4");
        table.add(row);
        System.out.println("===================================START");
        System.out.println(table);
        System.out.println("===================================END");
        table.remove(row);
        System.out.println(table);
        System.out.println("===================================REMOVED");
        
        Container caption = new AbstractContainer() {

            public String getTag() {
                return "caption";
            }
        };
        table.add(caption);

        Html html = new Html("caption 1\n");
        caption.add(html);
        Container thead = new AbstractContainer() {

            public String getTag() {
                return "thread";
            }
        };
        row = new Row();
        thead.add(row);
        Cell cell = new Cell();
        row.add(cell);
        cell = new Cell();
        row.add(cell);
        table.add(thead);

        row = new Row();

        cell = new Cell();
        //cell.setValue("hello");
        html = new Html("hello");
        cell.add(html);
        HiddenField field = new HiddenField("myfield", String.class);
        field.setValue("my hidden value");
        cell.add(field);
        cell.add(new Html("value 2"));
        row.add(cell);
        table.add(row);
        table.add(row);
//         
        System.out.println(table);
    }
}
