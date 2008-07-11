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
package net.sf.clickclick.jquery.controls;

import net.sf.click.AjaxControlRegistry;
import net.sf.click.control.Form;
import net.sf.click.control.HiddenField;
import net.sf.click.control.JavascriptImport;
import net.sf.click.util.PageImports;

/**
 *
 * @author Bob Schellink
 */
public class JQForm extends Form {

    public JQForm(String name) {
        super(name);
    }

    public void onInit() {
        super.onInit();
        add(new HiddenField(name, getId()));
        AjaxControlRegistry.registerAjaxControl(this);
    }
    
    public void onHtmlImports(PageImports pageImports) {
        String contextPath = getContext().getRequest().getContextPath();
        pageImports.add(new JavascriptImport(contextPath + "/clickclick/jquery/jquery-1.2.6.js"));
        pageImports.add(new JavascriptImport(contextPath + "/clickclick/jquery/form/jquery.form.js"));
    }
}