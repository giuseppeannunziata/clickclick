package net.sf.clickclick.examples.page.controls;

import java.util.Locale;

import net.sf.click.control.FieldSet;
import net.sf.click.control.Form;
import net.sf.click.control.Label;
import net.sf.click.control.Select;
import net.sf.click.control.Submit;
import net.sf.click.extras.control.PageSubmit;
import net.sf.clickclick.control.BooleanSelect;
import net.sf.clickclick.examples.page.BorderPage;

public class BooleanSelectPage extends BorderPage {

    public Form           locForm    = new Form();
    public Form           form       = new Form();
    private FieldSet      unstyled   = new FieldSet("BooleanSelect");
    private Select        locale     = new Select("Select Locale");
    private Submit        sbOk       = new Submit("ok", "  OK  ", this, "onSubmitClick");

    private BooleanSelect twostate   = new BooleanSelect("Twostate");
    private BooleanSelect tristate   = new BooleanSelect("Tristate", true);
    private BooleanSelect custom     = new BooleanSelect("Custom labels");

    private BooleanSelect notationTF = new BooleanSelect("True/False");
    private BooleanSelect notationYN = new BooleanSelect("Yes/No");
    private BooleanSelect notationOO = new BooleanSelect("On/Off");
    private BooleanSelect notationAI = new BooleanSelect("Active/Inactive");
    private BooleanSelect notationOC = new BooleanSelect("Open/Closed");

    public String         result     = "";

    public void onInit() {
        // -- personalizing BooleanSelect fields
        custom.setOptionLabels("Okidoki", "No way!");

        notationTF.setNotation(BooleanSelect.TRUEFALSE); // default so not really necessary
        notationYN.setNotation(BooleanSelect.YESNO); // you can also set this option in the constructor
        notationOO.setNotation(BooleanSelect.ONOFF);
        notationAI.setNotation(BooleanSelect.ACTIVEINACTIVE);
        notationOC.setNotation(BooleanSelect.OPENCLOSED);

        // -- adding fields to form & page --
        unstyled.add(twostate);
        unstyled.add(tristate);
        unstyled.add(custom);
        unstyled.add(new Label("<i>Notations backed by resource bundle.</i>"));
        unstyled.add(notationTF);
        unstyled.add(notationYN);
        unstyled.add(notationOO);
        unstyled.add(notationAI);
        unstyled.add(notationOC);

        locale.addAll(new String[] { "[set by browser]", "EN", "NL" });
        locForm.add(locale);
        locForm.add(new Label("<small><i>this will reset the form</i></small>"));
        locForm.add(new Submit("update", "update", this, "onUpdateClick"));

        form.add(unstyled);
        form.add(sbOk);
        form.add(new PageSubmit("cancel", this.getClass()));
    }

    public void onRender() {
        String l = getContext().getLocale().getLanguage();
        locale.setValue(l.toUpperCase());
        locale.setLabel("Change Locale (current: " + l + ")");
    }

    public boolean onSubmitClick() {
        if (form.isValid()) {
            result = "Tristate value is: " + tristate.getBoolean().toString().toUpperCase();
        }
        return true;
    }

    public boolean onUpdateClick() {
        if ("EN".equals(locale.getValue()))
            getContext().setLocale(new Locale("en"));
        else if ("NL".equals(locale.getValue()))
            getContext().setLocale(new Locale("nl"));
        else
            getContext().setLocale(Locale.getDefault());

        // must do a reload of the page to see the changes
        setRedirect(this.getClass());
        return false;
    }
}
