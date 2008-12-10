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
package net.sf.clickclick.control.ajax;

import java.util.Iterator;
import net.sf.click.AjaxControlRegistry;
import net.sf.click.AjaxListener;
import net.sf.click.control.ActionButton;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;

/**
 *
 * @author Bob Schellink
 */
public class AjaxActionButton extends ActionButton {

    // ----------------------------------------------------------- Constructors

    /**
     * Create a Submit button with the given name.
     *
     * @param name the button name
     */
    public AjaxActionButton(String name) {
        super(name);
    }

    /**
     * Create a Submit button with the given name and label.
     *
     * @param name the button name
     * @param label the button display label
     */
    public AjaxActionButton(String name, String label) {
        super(name, label);
    }

    /**
     * Create an ActionButton for the given listener object and listener method.
     * 
     * @throws IllegalArgumentException if the name, listener or method is null
     * or if the method is blank
     */
    public AjaxActionButton(AjaxListener ajaxListener) {
        setActionListener(ajaxListener);
    }

    /**
     * Create a Submit button with the given name, listener object and
     * listener method.
     *
     * @param name the button name
     * @param ajaxListener the Ajax Listener target object
     */
    public AjaxActionButton(String name, AjaxListener ajaxListener) {
        super(name);
        setActionListener(ajaxListener);
    }

    /**
     * Create a Submit button with the given name, label, listener object and
     * listener method.
     *
     * @param name the button name
     * @param label the button display label
     * @param ajaxListener the Ajax Listener target object
     * @throws IllegalArgumentException if listener is null or if the method
     * is blank
     */
    public AjaxActionButton(String name, String label, AjaxListener ajaxListener) {
        super(name, label);
        setActionListener(ajaxListener);
    }

    /**
     * Create an Submit button with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public AjaxActionButton() {
        super();
    }

    // ------------------------------------------------------ Public Attributes

    public String getId() {
        String id = super.getId();
        if (id == null) {
            return getName() + "_id";
        } else {
            return id;
        }
    }

    public void onInit() {
        super.onInit();
        AjaxControlRegistry.registerAjaxControl(this);
    }

    public String getHref(Object value) {
        String uri = ClickUtils.getRequestURI(getContext().getRequest());

        HtmlStringBuffer buffer =
            new HtmlStringBuffer(uri.length() + getName().length() + 40);

        buffer.append(uri);
        buffer.append("?");
        buffer.append(ACTION_BUTTON);
        buffer.append("=");
        buffer.append(getName());

        if (value != null) {
            buffer.append("&amp;");
            buffer.append(VALUE);
            buffer.append("=");
            buffer.append(ClickUtils.encodeUrl(value, getContext()));
        }

        if (hasParameters()) {
            Iterator i = getParameters().keySet().iterator();
            while (i.hasNext()) {
                String name = i.next().toString();
                if (!name.equals(ACTION_BUTTON) && !name.equals(VALUE)) {
                    Object paramValue = getParameters().get(name);
                    String encodedValue
                        = ClickUtils.encodeUrl(paramValue, getContext());
                    buffer.append("&amp;");
                    buffer.append(name);
                    buffer.append("=");
                    buffer.append(encodedValue);
                }
            }
        }

        return getContext().getResponse().encodeURL(buffer.toString());
    }

    public String getHref() {
        return getHref(getValueObject());
    }

    public void render(HtmlStringBuffer buffer) {
        setParameter(getId(), "1");
        buffer.elementStart(getTag());

        buffer.appendAttribute("type", getType());
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("value", getLabel());
        buffer.appendAttribute("title", getTitle());
        if (getTabIndex() > 0) {
            buffer.appendAttribute("tabindex", getTabIndex());
        }

        String onClickAction = " href=\"" + getHref() + "\"";
        buffer.append(onClickAction);

        appendAttributes(buffer);

        if (isDisabled()) {
            buffer.appendAttributeDisabled();
        }

        buffer.elementEnd();
    }
}
