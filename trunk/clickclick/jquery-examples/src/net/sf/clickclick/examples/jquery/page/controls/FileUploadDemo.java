package net.sf.clickclick.examples.jquery.page.controls;

import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.control.ajax.JQForm;
import net.sf.clickclick.jquery.Taconite;
import net.sf.clickclick.util.AjaxAdapter;
import net.sf.clickclick.util.Partial;
import org.apache.click.Control;
import org.apache.click.control.FileField;
import org.apache.click.control.Form;
import org.apache.click.control.Submit;
import org.apache.commons.fileupload.FileItem;

public class FileUploadDemo extends BorderPage {

    private Form form;
    private FileField fileField = new FileField("field", true);

    private Form ajaxForm;
    private FileField ajaxFileField = new FileField("field", true);

    public void onInit() {
        form = new Form("form");
        form.add(fileField);

        Submit submit = new Submit("upload", this, "onUploadClick");
        form.add(submit);

        addControl(form);

        ajaxForm = new JQForm("ajaxForm");
        ajaxForm.setJavaScriptValidation(true);
        ajaxForm.add(ajaxFileField);

        submit = new Submit("upload");
        submit.setActionListener(new OnSubmitHandler());
        ajaxForm.add(submit);

        addControl(ajaxForm);
    }

    public boolean onUploadClick() {
        if (form.isValid()) {
            addModel("fileItem", fileField.getFileItem());
        }

        return true;
    }

    class OnSubmitHandler extends AjaxAdapter {
        public Partial onAjaxAction(Control source) {
            Taconite partial = new Taconite();
            if (ajaxForm.isValid()) {

                if (ajaxFileField.getFileItem() == null) {
                    return partial;
                }

                FileItem fileItem = ajaxFileField.getFileItem();

                // Remove any form errors
                partial.remove("#" + ajaxForm.getId() + "-errorsTr");

                // Update FileItem data
                partial.replaceContent("#ajax-fileitem-name", fileItem.getName());
                partial.replaceContent("#ajax-fileitem-size", Long.toString(
                    fileItem.getSize()));
                partial.replaceContent("#ajax-fileitem-content-type",
                    fileItem.getContentType());
            }
            return partial;
        }
    }
}