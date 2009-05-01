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
package net.sf.clickclick.control.paginator;

import java.util.List;
import org.apache.click.MockContext;
import org.apache.click.control.AbstractControl;
import org.apache.click.control.AbstractLink;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Renderable;
import org.apache.click.control.Table;
import org.apache.click.element.CssImport;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Paginator implementation based on the article:
 * http://woork.blogspot.com/2008/03/perfect-pagination-style-using-css.html
 *
 * @author Bob Schellink
 */
public class SimplePaginator extends AbstractControl implements Renderable {

    private static final long serialVersionUID = 1L;

    // --------------------------------------------------------- Public Methods

    private int pageTotal;

    private int currentPage;

    protected int lowerBound;

    protected int upperBound;

    protected String styleClass = "pagination-digg";

    protected AbstractLink pageLink;

    // ----------------------------------------------------------- Constructors

    public SimplePaginator(String name) {
        setName(name);
    }

    public String getFirstTitleMessage() {
        return getMessage("paginator-first-title");
    }

    public String getFirstLabelMessage() {
        return getMessage("paginator-first-label");
    }

    public String getLastTitleMessage() {
        return getMessage("paginator-last-title");
    }

    public String getLastLabelMessage() {
        return getMessage("paginator-last-label");
    }
    
    public String getNextTitleMessage() {
        return getMessage("paginator-next-title");
    }

    public String getNextLabelMessage() {
        return getMessage("paginator-next-label");
    }

    public String getPreviousTitleMessage() {
        return getMessage("paginator-previous-title");
    }

    public String getPreviousLabelMessage() {
        return getMessage("paginator-previous-label");
    }

    public String getGotoPageTitleMessage() {
        return getMessage("paginator-goto-title");
    }

    public void setPageLink(AbstractLink pageLink) {
        this.pageLink = pageLink;
    }

    public AbstractLink getPageLink() {
        if (pageLink == null) {
            String name = getName();
            if (getName() == null) {
                throw new RuntimeException("Paginator name is not defined. " +
                    "Please set the Paginator name through #setName(String).");
            }
            pageLink = new ActionLink(getName());
        }
        return pageLink;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();
            CssImport cssImport = new CssImport("/clickclick/core/paginator/SimplePaginator.css");
            headElements.add(cssImport);
        }
        return headElements;
    }

    public void calcPageTotal(int pageSize, int rows) {
        // If pageTotal has value, exit early
        if (getPageTotal() > 0) {
            return;
        }

        if (pageSize == 0 || rows == 0) {
            setPageTotal(1);
            return;
        }

        double value = (double) rows / (double) pageSize;

        setPageTotal((int) Math.ceil(value));
    }

    public void render(HtmlStringBuffer buffer) {

        // If there are no pages to render, exit early
        if (getPageTotal() <= 0) {
            return;
        }

        calcLowerAndUpperBound();

        buffer.elementStart("ul");
        String styleClass = getStyleClass();
        if (styleClass != null) {
            buffer.appendAttribute("class", styleClass);
        }
        buffer.closeTag();
        buffer.append("\n");

        renderFirst(buffer);
        buffer.append("\n");
        renderPrevious(buffer);
        buffer.append("\n");

        for (int i = lowerBound; i < upperBound; i++) {
            int pageNumber = i + 1;
            renderItem(buffer, pageNumber);

            if (i < upperBound - 1) {
                renderSeparator(buffer);
            }
        }

        renderNext(buffer);
        buffer.append("\n");
        renderLast(buffer);
        buffer.append("\n");
        buffer.elementEnd("ul");
    }

    
    public String toString() {
        HtmlStringBuffer buffer =
            new HtmlStringBuffer(getPageTotal() * 70);
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    protected void renderItem(HtmlStringBuffer buffer, int pageNumber) {
        if (pageNumber == getCurrentPage()) {
            buffer.append("<li class=\"active\">");
        } else {
            buffer.append("<li>");
        }
        renderValue(buffer, pageNumber);
        buffer.append("</li>");
        buffer.append("\n");
    }

    protected void renderValue(HtmlStringBuffer buffer, int pageNumber) {
        if (pageNumber == getCurrentPage()) {
            buffer.append(pageNumber);
        } else {
            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(String.valueOf(pageNumber));

            // Cater for zero based indexing and subtract 1 from pageNumber
            pageLink.setParameter(Table.PAGE, String.valueOf(pageNumber - 1));
            pageLink.setTitle(getGotoPageTitleMessage() + " " +
                pageNumber);
            pageLink.render(buffer);
        }
    }

    protected void renderSeparator(HtmlStringBuffer buffer) {
    }

    protected void renderFirst(HtmlStringBuffer buffer) {
        buffer.elementStart("li");
        String pageValue = String.valueOf(0);
        if (getCurrentPage() > 0) {
            buffer.appendAttribute("class", "first");
            buffer.closeTag();

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getFirstLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getFirstTitleMessage());
            pageLink.render(buffer);
        } else {
            buffer.appendAttribute("class", "first-off");
            buffer.closeTag();

            buffer.append(getFirstLabelMessage());
        }

        buffer.elementEnd("li");
    }

    protected void renderPrevious(HtmlStringBuffer buffer) {
        buffer.elementStart("li");

        String pageValue = String.valueOf(getCurrentPage() - 1);
        if (getCurrentPage() > 0) {
            buffer.appendAttribute("class", "previous");
            buffer.closeTag();

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getPreviousLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getPreviousTitleMessage());
            pageLink.render(buffer);

        } else {
            buffer.appendAttribute("class", "previous-off");
            buffer.closeTag();

            buffer.append(getPreviousLabelMessage());
        }

        buffer.elementEnd("li");
    }

    protected void renderLast(HtmlStringBuffer buffer) {
        buffer.elementStart("li");
        String pageValue = String.valueOf(getPageTotal() - 1);
        if (getCurrentPage() < getPageTotal() - 1) {
            buffer.appendAttribute("class", "last");
            buffer.closeTag();

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getLastLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getLastTitleMessage());
            pageLink.render(buffer);
        } else {
            buffer.appendAttribute("class", "last-off");
            buffer.closeTag();

            buffer.append(getLastLabelMessage());
        }

        buffer.elementEnd("li");
    }

    protected void renderNext(HtmlStringBuffer buffer) {
        buffer.elementStart("li");
        String pageValue = String.valueOf(getCurrentPage() + 1);
        if (getCurrentPage() < getPageTotal() - 1) {
            buffer.appendAttribute("class", "next");
            buffer.closeTag();

            AbstractLink pageLink = getPageLink();
            pageLink.setLabel(getNextLabelMessage());
            pageLink.setParameter(Table.PAGE, pageValue);
            pageLink.setTitle(getNextTitleMessage());
            pageLink.render(buffer);
        } else {
            buffer.appendAttribute("class", "next-off");
            buffer.closeTag();

            buffer.append(getNextLabelMessage());
        }

        buffer.elementEnd("li");
    }

    protected void calcLowerAndUpperBound() {
        // Create sliding window of paging links
        lowerBound = Math.max(0, getCurrentPage() - 5);
        upperBound = Math.min(lowerBound + 10, getPageTotal());
        if (upperBound - lowerBound < 10) {
            lowerBound = Math.max(upperBound - 10, 0);
        }
    }

    public static void main(String[] args) {
        final int currentPage = 2;
        final int pageSize = 10;
        final int rowCount = 3000;
        MockContext.initContext();
        SimplePaginator paginator = new SimplePaginator("my-paginator");
        paginator.setCurrentPage(currentPage);
        paginator.calcPageTotal(pageSize, rowCount);
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        paginator.render(buffer);
        System.out.println(buffer);
    }
}
