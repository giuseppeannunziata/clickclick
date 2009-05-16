package net.sf.clickclick.examples.jquery.page.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.click.Control;
import net.sf.clickclick.AjaxListener;
import org.apache.click.control.Checkbox;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.EmailField;
import org.apache.click.util.HtmlStringBuffer;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.JQForm;
import net.sf.clickclick.control.html.Div;
import net.sf.clickclick.examples.jquery.page.HomePage;
import net.sf.clickclick.jquery.util.JQAjaxPartial;

public class FormDemo extends BorderPage {

    public String title = "Form Demo";

    private JQForm form = new JQForm("form");

    private Div target = new Div("target");

    /**
     * Build form and fields
     */
    public void onInit() {
        super.onInit();

        // Setup Ajax
        form.setDataType(JQForm.HTML);
        form.setClearTarget(true);
        form.setTargetId(target.getId());

        // Setup fields
        TextField textField = new TextField("firstName");
        textField.setValue("Steve");
        textField.setRequired(true);
        form.add(textField);
        
        textField = new TextField("lastName");
        textField.setValue("Masters");
        textField.setRequired(true);
        form.add(textField);

        EmailField emailField = new EmailField("email", "E-Mail");
        emailField.setSize(20);
        emailField.setValue("steve@test.com");
        form.add(emailField);

        final Checkbox redirect = new Checkbox("redirect", "Redirect to Home after submit?");
        form.add(redirect);

        Submit submit = new Submit("submit");        

        // Set AjaxListener on Submit that will be invoked when submit is performed
        submit.setActionListener(new AjaxListener() {
 
            public boolean onAction(Control source) {
                actionPerformed(source);
                return true;
            }

            public Partial onAjaxAction(Control source) {
                if (form.isValid()) {
                    actionPerformed(source);
                    // Return a Partial response
                    boolean shouldRedirect = redirect.isChecked();
                    return createSuccessPartial(shouldRedirect);
                } else {
                    return createErrorPartial();
                }
            }

            private void actionPerformed(Control source) {
                // Perform submit action e.g. saving the Form to database
                System.out.println("Form saved to database");
            }
        });
        form.add(submit);

        addControl(form);

        // Add ajax target
        addControl(target);
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
    private Partial createSuccessPartial(boolean shouldRedirect) {
        JQAjaxPartial partial = new JQAjaxPartial();
        // For redirect, set redirectUrl and return the partial
        if (shouldRedirect) {
            partial.setRedirect(HomePage.class);
            return partial;
        }

        HtmlStringBuffer buffer = new HtmlStringBuffer();
        buffer.append("<pre>Map {\n");
        buffer.append("    [firstName] => ").append(form.getFieldValue("firstName")).append("\n");
        buffer.append("    [lastName] => ").append(form.getFieldValue("lastName")).append("\n");
        buffer.append("    [email] => ").append(form.getFieldValue("email")).append("\n");
        buffer.append("}</pre>");

        // Set the partial content
        partial.setContent(buffer.toString());

        // Set the target Control that will have its html replaced by the
        // Partial content
        partial.setTargetId(target.getId());

        return partial;
    }

    /**
     * Create a Partial response
     */
    private Partial createErrorPartial() {
        JQAjaxPartial partial = new JQAjaxPartial();

        // Set the partial content, in this case the form because it is invalid
        // and we need to display its error messages
        partial.setContent(form);

        // Set partial to replace the target itself, not just its content
        partial.setReplaceTarget(true);

        // Set the form as the target Control that will have its html replaced by the
        // Partial content
        partial.setTargetId(form.getId());

        partial.setFocusId(form.getFocusField().getId());

        return partial;
    }
}
