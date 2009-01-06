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
import org.apache.click.control.AbstractControl;
import net.sf.clickclick.control.html.Div;

/**
 *
 * @author Bob Schellink
 */
public class AbsolutePanel extends SimplePanel {

    private Div div;

    public AbsolutePanel() {
        init();
    }

    public AbsolutePanel(String name) {
        super(name);
        init();
    }

    public Control add(Control control) {
        return add(control, 0, 0);
    }

    public Control add(Control control, int left, int top) {
        
        AbstractControl abstractControl = null;

        if (control instanceof AbstractControl) {
            // If control is AbstractControl, modify its attributes directly
            abstractControl = (AbstractControl) control;
        } else {
            // If the control is not an AbstractControl, wrap it in an inlined Div
            Div div = new Div();
            div.setStyle("display", "inline");
            abstractControl = div;
        }
        abstractControl.setStyle("position", "absolute");
        abstractControl.setStyle("left", left + "px");
        abstractControl.setStyle("top", top + "px");
        div.add(abstractControl);
        return control;
    }

    public Control insert(Control control, int index) {
        throw new UnsupportedOperationException("insert is not supported by this Panel");
    }

    protected void init() {
        div = new Div();
        super.insert(div, 0);
        div.setStyle("position", "relative");
        div.setStyle("overflow", "hidden");
    }
}
