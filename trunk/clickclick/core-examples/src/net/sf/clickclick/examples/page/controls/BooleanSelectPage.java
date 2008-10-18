package net.sf.clickclick.examples.page.controls;

import java.util.Locale;

import net.sf.click.control.FieldSet;
import net.sf.click.control.Form;
import net.sf.click.control.Label;
import net.sf.click.control.Select;
import net.sf.click.control.Submit;
import net.sf.click.extras.control.PageSubmit;
import net.sf.clickclick.control.BooleanSelect;
import net.sf.clickclick.control.panel.HorizontalPanel;
import net.sf.clickclick.examples.page.BorderPage;

public class BooleanSelectPage extends BorderPage {

    public Form                 locForm        = new Form();
    public Form                 form           = new Form();
    private FieldSet            unstyled       = new FieldSet("BooleanSelect");
    private FieldSet            styled         = new FieldSet("StyledBooleanSelect");
    private Select              locale         = new Select("Select Locale");
    private HorizontalPanel     hp             = new HorizontalPanel("panel");
    private Submit              sbOk           = new Submit("ok", "  OK  ", this, "onSubmitClick");

    private BooleanSelect       twostate       = new BooleanSelect("Twostate");
    private BooleanSelect       tristate       = new BooleanSelect("Tristate", true);
    private BooleanSelect       custom         = new BooleanSelect("Custom labels");
    private BooleanSelect       notationTF     = new BooleanSelect("True/False");
    private BooleanSelect       notationYN     = new BooleanSelect("Yes/No");
    private BooleanSelect       notationOO     = new BooleanSelect("On/Off");
    private BooleanSelect       notationAI     = new BooleanSelect("Active/Inactive");
    private BooleanSelect       notationOC     = new BooleanSelect("Open/Closed");

    private StyledBooleanSelect styledTwostate = new StyledBooleanSelect("s2", "Styled Twostate");
    private StyledBooleanSelect styledTristate = new StyledBooleanSelect("s3", "Styled Tristate", BooleanSelect.ACTIVEINACTIVE, true);
    private StyledBooleanSelect styledCustom   = new StyledBooleanSelect("sCust", "Custom Style");

    public String               result         = "";

    public void onInit() {
        // -- personalizing BooleanSelect fields
        custom.setOptionLabels("Okidoki", "No way!");

        notationTF.setNotation(BooleanSelect.TRUEFALSE); // default so not really necessary
        notationYN.setNotation(BooleanSelect.YESNO); // you can also set this option in the constructor
        notationOO.setNotation(BooleanSelect.ONOFF);
        notationAI.setNotation(BooleanSelect.ACTIVEINACTIVE);
        notationOC.setNotation(BooleanSelect.OPENCLOSED);

        styledTwostate.setOptionFalseStyleClass("falseOption");

        styledCustom.setNotation(BooleanSelect.OPENCLOSED);
        styledCustom.setTristate(true);
        styledCustom.addStyleClass("custom");
        styledCustom.setOptionUnsetStyleClass("custom");
        styledCustom.setOptionTrueStyleClass("custom");
        styledCustom.setOptionFalseStyleClass("custom");
        styledCustom.setOptionUnsetStyleAttributes("height: 26px; padding-left: 25px;");
        styledCustom.setOptionTrueStyleAttributes("height: 26px; padding-left: 25px; background-image: url(/clickclick-core-examples/assets/images/lockopen.png);");
        styledCustom.setOptionFalseStyleAttributes("height: 26px; padding-left: 25px; background-image: url(/clickclick-core-examples/assets/images/lockclosed.png);");
        styledCustom.setAttribute("onChange", "changeStyle(this);");

        // -- adding fields to form & page --
        unstyled.add(twostate);
        unstyled.add(tristate);
        unstyled.add(custom);
        unstyled.add(notationTF);
        unstyled.add(notationYN);
        unstyled.add(notationOO);
        unstyled.add(notationAI);
        unstyled.add(notationOC);

        locale.addAll(new String[] { "[set by browser]", "EN", "NL" });
        locForm.add(locale);
        locForm.add(new Label("<small><i>this will reset the form</i></small>"));
        locForm.add(new Submit("update", "update", this, "onUpdateClick"));
        hp.add(unstyled);

        styled.add(styledTwostate);
        styled.add(styledTristate);
        styled.add(new Label("<i>Images only work with Firefox.</i>"));
        styled.add(styledCustom);
        hp.add(styled);
        
        form.add(hp);
        form.add(sbOk);
        form.add(new PageSubmit("cancel", this.getClass()));
    }

    public void onRender() {
        String l = getContext().getLocale().getLanguage();
        locale.setValue(l.toUpperCase());
        locale.setLabel("Change Locale (current: " + l + ")");
        // hey! where is autoboxing gone? oh wait 1.4... ;-)
        if (!sbOk.isClicked()) styledCustom.setBoolean(Boolean.FALSE);
    }

    public boolean onSubmitClick() {
        if (form.isValid()) {
            result = "Tristate Select-values are: " + tristate.getBoolean().toString().toUpperCase() + " and " + styledTristate.getBoolean().toString().toUpperCase();
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
