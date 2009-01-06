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
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;

/**
 *
 * @author Bob Schellink
 */
public class AjaxForm extends Form {

    public AjaxForm() {
    }

    public AjaxForm(String name) {
        super(name);
    }

    public void onInit() {
        super.onInit();
        add(new HiddenField(name, getId()));
        AjaxControlRegistry.registerAjaxControl(this);
    }
}
