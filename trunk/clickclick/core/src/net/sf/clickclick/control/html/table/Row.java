package net.sf.clickclick.control.html.table;

import org.apache.click.Control;
import org.apache.click.control.AbstractContainer;

/**
 *
 * @author Bob Schellink
 */
public class Row extends AbstractContainer {

    public Row() {
    }

    public Row(String name) {
        super(name);
    }
    
    public Control insert(Control control, int column) {
        if (!(control instanceof Cell)) {
            throw new IllegalArgumentException("Only cells can be inserted.");
        }
        return insert((Cell) control, column);
    }

    public Cell insert(Cell cell, int column) {
        if (column > getColumnCount()) {
            expandCells(column);
        }
        super.insert(cell, column);
        return cell;
    }

    public Cell insertCell(int column) {
        if (column < 0) {
            throw new IndexOutOfBoundsException("column must be >= 0");
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

    public Cell removeCell(int column) {
        Cell cell = getCell(column);
        if (cell == null) {
            return null;
        }

        remove(cell);
        return cell;
    }

    public int getColumnCount() {
        return hasControls() ? getControls().size() : 0;
    }

    public Cell getCell(final int column) {
        if (hasControls()) {
            if (getControls().size() >= column) {
                return (Cell) getControls().get(column);
            }
        }
        return null;
    }

    public String getTag() {
        return "tr";
    }

    /**
     * Adds cells to the row up to and including the specified column.
     *
     * @param column specifies number of cells the row should have
     */
    public void expandCells(int column) {
        //Try and find the cell. If it exists already, return
        Cell tableCell = getCell(column);
        if (tableCell != null) {
            return;
        }

        //It does not exist, create it and all previous cells
        int columnCount = getColumnCount();
        Cell newCell = null;
        while(columnCount < column) {
            newCell = new Cell();
            add(newCell);
            columnCount++;
        }
    }

    public static void main(String[] args) {
        Row row = new Row();
        row.insert(new Cell("one"),3);
        row.insert(new Cell("three"), 5);
        System.out.println(row);
    }
}
