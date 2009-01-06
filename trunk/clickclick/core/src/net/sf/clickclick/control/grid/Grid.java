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
package net.sf.clickclick.control.grid;

import java.util.List;
import org.apache.click.Control;
import org.apache.click.control.Container;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.control.html.table.Cell;
import net.sf.clickclick.control.html.table.HtmlTable;
import net.sf.clickclick.control.html.table.Row;

/**
 *
 * @author Bob Schellink
 */
public class Grid extends HtmlTable {

    private int numRows = 0;
    private int numColumns = 0;

    public Grid() {
    }

    public Grid(String name) {
        super(name);
    }

    public Grid(int numRows, int numCols) {
        this(null, numRows, numCols);
    }

    public Grid(String name, int numRows, int numColumns) {
        super(name);
        this.numRows = numRows;
        this.numColumns = numColumns;
        ensureCapacity(numRows, numColumns);
    }

    public Control insert(Control control, int index) {
        throw new IllegalArgumentException("Method not supported by Grid. Use "
            + "insert(Control, int, int) instead");
    }

    public Control insert(Control control, int row, int column) {
        if (row <= 0) {
            throw new IndexOutOfBoundsException("row must be > 0");
        }
        if (column <= 0) {
            throw new IndexOutOfBoundsException("column must be > 0");
        }
        if (control instanceof Cell || control instanceof Row) {
            throw new IllegalArgumentException("Rows and Cells cannot be added" +
                " to Grid.");
        }
        Cell cell = insertCell(row, column);
        cell.add(control);
        return control;
    }

    public int getRowCount() {
        return hasControls() ? getControls().size() : 0;
    }

    public boolean remove(int row, int column) {
        Cell td = getCell(row, column);
        if (td == null) {
            return false;
        }

        return removeAll(td);
    }

    // -------------------------------------------------------- Package Private Methods

    /**
     * Adds rows to the table up to and including the specified row index.
     *
     * @param row specifies number of rows the table should have
     */
    void expandRows(int row) {
        //Try and find the row. If it exists already, return
        Row tableRow = getRow(row);
        if (tableRow != null) {
            return;
        }

        //It does not exist, create it and all previous rows
        int rowCount = getRowCount();
        Row newRow = null;
        while (rowCount < row) {
            newRow = new OneBasedRow();
            super.insert(newRow, getControls().size());
            rowCount++;
            newRow.expandCells(numColumns);
        }
    }

    // ---------------------------------------------------------- Inner classes

    /**
     * This row ensures its index is one based instead of zero based.
     */
    class OneBasedRow extends Row {
        public Control insert(Control control, int column) {
            return super.insert(control, column + 1);
        }
        
        public Cell insert(Cell cell, int column) {
            if (column <= 0) {
                throw new IndexOutOfBoundsException("For Grids the column must be > 0");
            }
            super.insert(cell, column - 1);
            return cell;
        }
        
        public Cell getCell(final int column) {
            if (hasControls()) {
                if (getControls().size() >= column) {
                    int realColumn = column - 1;
                    return (Cell) getControls().get(realColumn);
                }
            }
            return null;
        }

        public Cell insertCell(int column) {
            if (column <= 0) {
                throw new IndexOutOfBoundsException("For Grids the column must be > 0");
            }
            if (column > getColumnCount()) {
                expandCells(column);
                return getCell(column);
            } else {
                Cell newCell = new Cell();
                insert(newCell, column);
                return newCell;
            }
        }
    }

    // -------------------------------------------------------- Private Methods

    private void ensureCapacity(int newRowCount, int newColumnCount) {
        if (newRowCount > numRows) {
            this.numRows = newRowCount;
            expandRows(numRows);
        }
        if (newColumnCount > numColumns) {
            this.numColumns = newColumnCount;

            List rows = (List) getControls();
            for (int i = 0; i < rows.size(); i++) {
                Row row = (Row) rows.get(i);
                row.expandCells(numColumns);
            }
        }
    }

    private Cell insertCell(int row, int column) {
        if (row <= 0) {
            throw new IndexOutOfBoundsException("row must be > 0");
        }
        if (column <= 0) {
            throw new IndexOutOfBoundsException("column must be > 0");
        }
        ensureCapacity(row, column);
        return getRow(row).getCell(column);
    }

    private boolean removeAll(Container container) {
        boolean hasChanged = false;
        if (container.hasControls()) {
            List controls = container.getControls();
            for (int i = controls.size() - 1; i >= 0; i--) {
                Control control = (Control) controls.get(i);
                if (container.remove(control)) {
                    hasChanged = true;
                }
            }
        }
        return hasChanged;
    }

    private Cell getCell(int row, int column) {
        Row tableRow = getRow(row);
        if(tableRow == null) {
            return null;
        }

        return tableRow.getCell(column);
    }

    private Row getRow(int row) {
        if (hasControls()) {
            if (getControls().size() >= row) {
                int realRow = row - 1;
                return (Row) getControls().get(realRow);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Grid grid = new Grid("mygrid");
        System.out.println("Row 1 -> " + grid.getRow(1));
        System.out.println("Cell at 1,1 -> " + grid.getCell(1, 1));
        Div div = (Div) grid.insert(new Div(), 1, 1);
        div = (Div) grid.insert(new Div(), 2, 1);
        System.out.println("Removed? : " + grid.remove(1, 1));
        grid.insert(div, 1, 1);
        //cell.getRow().setName("myrow");
        
        grid.insertCell(3,3);
        System.out.println(grid);
    }
}
