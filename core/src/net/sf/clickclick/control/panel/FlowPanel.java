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
import net.sf.click.control.AbstractContainer;
import net.sf.click.control.TextField;
import net.sf.clickclick.control.html.Div;

/**
 *
 * @author Bob Schellink
 */
public class FlowPanel extends SimplePanel {

    private Div div = new Div();

    public FlowPanel() {
        init();
    }
    
    public FlowPanel(String name) {
        super(name);
        init();
    }

    public Control add(Control control) {
        // Wrap the control in a Span element
        Div div = new Div();
        div.setAttribute("class", "c-flowpanel-section");
        div.setStyle("display", "inline");
        div.add(control);
        div.add(div);
        return control;
    }

    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by this Panel");
    }

    public boolean remove(Control control) {
        AbstractContainer container = getContainer(control);
        return div.remove(container);
    }
    
    public AbstractContainer getContainer(Control control) {
        return (AbstractContainer) control.getParent();
    }
    
    public void setSize(String width, String height) {
        setWidth(width);
        setHeight(height);
    }
    
    public void setHeight(String height) {
        div.setHeight(height);
    }
    
    public void setWidth(String width) {
        div.setWidth(width);
    }

    public static void main(String[] args) {        
        MockContext.initContext();
        Page page = new Page();
        FlowPanel panel = new FlowPanel("panel");
        page.addControl(panel);
        panel.setSize("100px", "100px");
        panel.add(new TextField("field"));
        System.out.println(panel);
    }

    // ------------------------------------------------------ Protected Methods

    protected void init() {
        super.insert(div, 0);
        div.setAttribute("class", "c-flowpanel");
    }
}
