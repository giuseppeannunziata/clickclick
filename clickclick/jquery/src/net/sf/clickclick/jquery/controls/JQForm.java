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

import net.sf.clickclick.control.JavascriptImport;
import net.sf.clickclick.control.ajax.AjaxForm;
import net.sf.click.util.AdvancedPageImports;

/**
 *
 * @author Bob Schellink
 */
public class JQForm extends AjaxForm {

    public JQForm() {
    }

    public JQForm(String name) {
        super(name);
    }

    public String getHtmlImports() {
        AdvancedPageImports pageImports = (AdvancedPageImports) getPage().getPageImports();
        String contextPath = getContext().getRequest().getContextPath();
        pageImports.add(new JavascriptImport(contextPath + "/clickclick/jquery/jquery-1.2.6.js"));
        pageImports.add(new JavascriptImport(contextPath + "/clickclick/jquery/form/jquery.form.js"));
        return null;
    }
}
