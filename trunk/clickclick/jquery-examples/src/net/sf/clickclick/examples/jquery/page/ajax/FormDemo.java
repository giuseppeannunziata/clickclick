package net.sf.clickclick.examples.jquery.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.click.Control;
import net.sf.click.AjaxListener;
import net.sf.click.Context;
import net.sf.click.control.Form;
import net.sf.click.control.Submit;
import net.sf.click.control.TextField;
import net.sf.click.extras.control.EmailField;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.click.util.PageImports;
import net.sf.click.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.JQForm;

public class FormDemo extends BorderPage {

    public String title = "Form Demo";

    private Form form = new JQForm("form");

    /**
     * Build form and fields
     */
    public void onInit() {
        super.onInit();
        TextField textField = new TextField("firstName");
        textField.setValue("Steve");
        form.add(textField);
        
        textField = new TextField("lastName");
        textField.setValue("Masters");
        form.add(textField);

        EmailField emailField = new EmailField("email", "E-Mail");
        emailField.setSize(20);
        emailField.setValue("steve@test.com");
        form.add(emailField);

        Submit submit = new Submit("submit");        

        // Set AjaxListener on Submit that will be invoked when submit is performed
        submit.setActionListener(new AjaxListener() {
 
            public boolean onAction(Control source) {
                actionPerformed(source);
                return true;
            }

            public Partial onAjaxAction(Control source) {
                actionPerformed(source);
                // Return a Partial response
                return createPartial();
            }

            private void actionPerformed(Control source) {
                // Perform submit action e.g. saving the Form to database
                System.out.println("Form saved to database");
            }
        });
        form.add(submit);

        addControl(form);
    }

    /**
     * Add JavaScript and CSS imports.
     */
    public void onHtmlImports(PageImports pageImports) {
        Context context = getContext();
        String contextPath = context.getRequest().getContextPath();
        Map model = new HashMap();

        String jsTemplate = "/ajax/form-demo.js";
        pageImports.appendToGlobalScript(context.renderTemplate(jsTemplate, model));
    }

    /**
     * Returns a formatted date String.
     */
    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:S");
        return format.format(new Date());
    }
    
    /**
     * Create a Partial response
     */
    private Partial createPartial() {
        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("<pre>Map {\n");
        buffer.append("    [firstName] => ").append(form.getFieldValue("firstName")).append("\n");
        buffer.append("    [lastName] => ").append(form.getFieldValue("lastName")).append("\n");
        buffer.append("    [email] => ").append(form.getFieldValue("email")).append("\n");
        buffer.append("}</pre>");
        return new Partial(buffer.toString());
    }
}
