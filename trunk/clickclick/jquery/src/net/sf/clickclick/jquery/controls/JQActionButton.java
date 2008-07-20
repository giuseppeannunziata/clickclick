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

import java.util.HashMap;
import java.util.Map;
import net.sf.click.AjaxListener;
import net.sf.click.Context;
import net.sf.click.Page;
import net.sf.click.util.AdvancedPageImports;
import net.sf.clickclick.control.JavascriptImport;
import net.sf.clickclick.control.JavascriptInclude;
import net.sf.clickclick.control.ajax.AjaxActionButton;

/**
 *
 * @author Bob Schellink
 */
public class JQActionButton extends AjaxActionButton {

    // -------------------------------------------------------------- Constants

    public static final String XML = "xml";
    
    public static final String HTML = "html";
    
    public static final String JSON = "json";
    
    public static final String JSONP = "jsonp";
    
    public static final String SCRIPT = "script";

    private String targetId;
    
    private String dataType = HTML;

    private String errorMessage = "Error occurred";
    
    private boolean showBusyIndicator = true;

    private String busyMessage = null;

    public JQActionButton(String name) {
        super(name);
    }

    public JQActionButton(String name, String label) {
        super(name, label);
    }

    public JQActionButton(AjaxListener ajaxListener) {
        super(ajaxListener);
    }

    public JQActionButton(String name, AjaxListener ajaxListener) {
        super(name, ajaxListener);
    }

    public JQActionButton(String name, String label, AjaxListener ajaxListener) {
        super(name, label, ajaxListener);
    }

    public JQActionButton() {
    }

    public String getHtmlImports() {
        AdvancedPageImports pageImports = (AdvancedPageImports) getPage().getPageImports();
        Context context = getContext();
        String contextPath = context.getRequest().getContextPath();

        JavascriptImport jsImport = new JavascriptImport(contextPath + "/clickclick/jquery/jquery-1.2.6.js");
        pageImports.add(jsImport);

        Page page = getPage();

        Map model = new HashMap();
        model.put("source", getId());
        model.put("context", contextPath);
        model.put("path", page.getPath());
        model.put("target", getTargetId());
        model.put("dataType", getDataType());
        model.put("errorMessage", getErrorMessage());
        model.put("showBusyIndicator", Boolean.valueOf(isShowBusyIndicator()));
        model.put("busyIndicatorMessage", getBusyMessage());
        if (isShowBusyIndicator()) {
            jsImport = new JavascriptImport(contextPath + "/clickclick/jquery/blockui/jquery.blockUI.js");
            pageImports.add(jsImport);
        }
 
        String include = getContext().renderTemplate("/clickclick/jquery/action/jq-action-button.js", model);
        JavascriptInclude jsInclude = new JavascriptInclude(include);
        pageImports.add(jsInclude);
        return null;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String responseDataType) {
        this.dataType = responseDataType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isShowBusyIndicator() {
        return showBusyIndicator;
    }

    public void setShowBusyIndicator(boolean showBusyIndicator) {
        this.showBusyIndicator = showBusyIndicator;
    }

    public String getBusyMessage() {
        return busyMessage;
    }

    public void setBusyMessage(String busyMessage) {
        this.busyMessage = busyMessage;
    }
}