package net.sf.clickclick.examples.jquery.page.ajax;

import net.sf.clickclick.control.Text;
import net.sf.clickclick.control.html.Div;
import org.apache.click.Control;
import net.sf.clickclick.util.Partial;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.controls.ajax.JQForm;
import net.sf.clickclick.jquery.util.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.click.control.TextField;
import org.apache.click.extras.control.DateField;
import org.apache.click.extras.control.EmailField;

/**
 * This example demonstrates using the ajax aware JQForm (JQuery Form).
 *
 * An AjaxListener is registered on the Form's Submit button which is fired
 * when the Form is submitted via Ajax.
 *
 * If the Form is successfully completed, a success response is returned,
 * otherwise an error response is returned.
 *
 * @author Bob Schellink
 */
public class FormDemo extends BorderPage {

    private Form form = new JQForm("form");

    private Div msgHolder = new Div("msgHolder");

    private Text textMsg = new Text();

    public void onInit() {
        super.onInit();

        addControl(form);
        addControl(msgHolder);
        msgHolder.add(textMsg);

        // Setup fields
        form.add(new TextField("firstName", true));
        form.add(new TextField("lastName", true));
        form.add(new EmailField("email", "E-Mail"));

        Submit submit = new Submit("submit");
        form.add(submit);

        // Set AjaxListener on Submit which will be invoked when form is submitted
        submit.setActionListener(new AjaxAdapter() {

            public Partial onAjaxAction(Control source) {
                if (form.isValid()) {
                    saveForm();
                    return createSuccessResponse();
                } else {
                    return createErrorResponse();
                }
            }
        });
    }

    private void saveForm() {
        System.out.println("Form saved to database");
    }

    /**
     * Return a Partial response (using a Taconite Partial object)
     * that does the following:
     *
     * 1. Replace the Form in the browser with the current Form
     * 2. Style the message holder with a green background which indicates success
     * 3. Replace the message holder with the current message holder
     */
    private Partial createSuccessResponse() {
        Taconite partial = new Taconite();

        form.add(new DateField("date"));
        // 1. Replace the Form in the browser with the current one
        partial.replace(form);

        // Set a success message
        textMsg.setText("Successfully submitted Form");

        // 2. Style the message holder with a red background
        msgHolder.setAttribute("style", "color:white; background: green; border: black 1px solid;padding: 5px; float: left");

        // 3. Replace the message holder in the browser with the current one
        partial.replace(msgHolder);

        return partial;
    }

    /**
     * Return a Partial response (using a Taconite Partial object)
     * that does the following:
     *
     * 1. Replace the Form in the browser with the current Form
     * 2. Style the message holder with a red background which indicates an error
     * 3. Replace the message holder with the current message holder
     */
    private Partial createErrorResponse() {
        Taconite partial = new Taconite();

        // 1. Replace the Form in the browser with the current one
        partial.replace(form);

        // Set an error message
        textMsg.setText("Form contained errors.");

        // 2. Style the message holder with a red background
        msgHolder.setAttribute("style", "color:white; background: red; border: black 1px solid;padding: 5px; float: left");

        // 3. Replace the message holder in the browser with the current one
        partial.replace(msgHolder);

        return partial;
    }
}
