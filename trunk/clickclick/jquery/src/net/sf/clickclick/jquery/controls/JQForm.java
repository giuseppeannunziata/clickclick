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
import java.util.List;
import java.util.Map;
import net.sf.click.Context;
import net.sf.click.control.Field;
import net.sf.clickclick.control.ajax.AjaxForm;
import net.sf.click.util.ClickUtils;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.clickclick.util.AdvancedPageImports;
import net.sf.clickclick.util.JavascriptImport;
import net.sf.clickclick.util.JavascriptInclude;

/**
 *
 * @author Bob Schellink
 */
public class JQForm extends AjaxForm {

    public static final String XML = "xml";

    public static final String HTML = "html";
    
    public static final String JSON = "json";
    
    public static final String JSONP = "jsonp";
    
    public static final String SCRIPT = "script";

    private String dataType = HTML;

    private boolean clearTarget = true;

    private String targetId;

    public JQForm() {
    }

    public JQForm(String name) {
        super(name);
    }

    public String getHtmlImports() {
        AdvancedPageImports pageImports = (AdvancedPageImports) getPage().getPageImports();
        Context context = getContext();
        String contextPath = context.getRequest().getContextPath();
        Map model = new HashMap();
        model.put("targetId", getTargetId());
        model.put("clearTarget", Boolean.valueOf(isClearTarget()));
        model.put("dataType", getDataType());
        model.put("formId", getId());

        pageImports.add(new JavascriptInclude(context.renderTemplate("/clickclick/jquery/form/jq-form.js", model)));

        pageImports.add(new JavascriptImport(contextPath + "/clickclick/jquery/jquery-1.2.6.js"));
        pageImports.add(new JavascriptImport(contextPath + "/clickclick/jquery/form/jquery.form.js"));

        pageImports.add(new JavascriptImport(contextPath + "/clickclick/jquery/blockui/jquery.blockUI.js"));
        return super.getHtmlImports();
    }

    public Field getFocusField() {
        List formFields = ClickUtils.getFormFields(this);

        Field focusField = null;

        boolean errorFieldFound = false;
        for (int i = 0, size = formFields.size(); i < size; i++) {
            Field field = (Field) formFields.get(i);

            if (field.getError() != null
                && !field.isHidden()
                && !field.isDisabled()) {
                focusField = field;
                break;
            }
        }

        if (!errorFieldFound) {
            for (int i = 0, size = formFields.size(); i < size; i++) {
                Field field = (Field) formFields.get(i);

                if (field.getFocus()
                    && !field.isHidden()
                    && !field.isDisabled()) {
                    focusField = field;
                    break;
                }
            }
        }

        return focusField;
    }

    /**
     * Close the form tag and render any additional content after the Form.
     * <p/>
     * Additional content includes <tt>javascript validation</tt> and
     * <tt>javascript focus</tt> scripts.
     *
     * @param formFields all fields contained within the form
     * @param buffer the buffer to render to
     */
    protected void renderTagEnd(List formFields, HtmlStringBuffer buffer) {

        // Method was overridden in order to remove script that sets focus
        // to fields
        buffer.elementEnd(getTag());
        buffer.append("\n");

        renderValidationJavaScript(buffer, formFields);
    }

    public boolean isClearTarget() {
        return clearTarget;
    }

    public void setClearTarget(boolean clearTarget) {
        this.clearTarget = clearTarget;
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
}
