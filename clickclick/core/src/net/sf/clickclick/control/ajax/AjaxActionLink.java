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

import org.apache.click.AjaxControlRegistry;
import net.sf.click.AjaxListener;
import org.apache.click.control.ActionLink;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 * @author Bob Schellink
 */
public class AjaxActionLink extends ActionLink {

    public AjaxActionLink(String name) {
        super(name);
    }

    public AjaxActionLink(String name, String label) {
        super(name, label);
    }

    public AjaxActionLink(AjaxListener ajaxListener) {
        setActionListener(ajaxListener);
    }

    public AjaxActionLink(String name, AjaxListener ajaxListener) {
        super(name);
        setActionListener(ajaxListener);
    }

    public AjaxActionLink(String name, String label, AjaxListener ajaxListener) {
        super(name, label);
        setActionListener(ajaxListener);
    }

    public AjaxActionLink() {
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
        // Add id parameter to trigger onProcess event
        AjaxControlRegistry.registerAjaxControl(this);
    }

    public void render(HtmlStringBuffer buffer) {
        setParameter(getId(), "1");
        super.render(buffer);
    }
}
