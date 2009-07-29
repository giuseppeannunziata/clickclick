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
import net.sf.click.control.Select;
import net.sf.clickclick.util.AdvancedPageImports;
import net.sf.clickclick.util.JavascriptImport;

/**
 *
 * @author Bob Schellink
 */
public class JQSelect extends Select {

    public JQSelect() {
    }
    
    public JQSelect(String name) {
        super(name);
    }
    
    public JQSelect(String name, String label) {
        super(name, label);
    }
    
    public JQSelect(String name, boolean required) {
        super(name, required);
    }

    public JQSelect(String name, String label, boolean required) {
        super(name, label, required);
    }

    public void onInit() {
        AjaxControlRegistry.registerAjaxControl(this);
    }

    public String getHtmlImports() {
        AdvancedPageImports pageImports = (AdvancedPageImports) getPage().getPageImports();
        String contextPath = getContext().getRequest().getContextPath();
        JavascriptImport jsImport = new JavascriptImport(contextPath + "/clickclick/jquery/jquery-1.2.6.js");
        pageImports.add(jsImport);
        return null;
    }
}