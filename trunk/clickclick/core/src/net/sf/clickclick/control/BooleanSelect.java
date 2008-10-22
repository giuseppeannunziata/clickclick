package net.sf.clickclick.control;

import net.sf.click.control.Select;
import net.sf.click.extras.control.DateField;
import net.sf.click.extras.control.IntegerField;
import org.apache.commons.lang.StringUtils;

/**
 * Extends the {@link Select} control.
 *
 * <table class='htmlHeader' cellspacing='6'>
 * <tr>
 * <td>BooleanSelect</td>
 * <td>
 * <select title='BooleanSelect Control'>
 * <option value=''></option>
 * <option value='true'>yes</option>
 * <option value='false'>no</option>
 * </select>
 * </td>
 * </tr>
 * </table>
 * <h4>Use</h4>
 * <p>BooleanSelect can be used as simple two-option select (TRUE / FALSE) or as tristate select (UNSET / TRUE / FALSE).
 * <p>Tristate is handy when you want to confirm that the user made a concious descision and not just accepted the default value.
 * <p>Please note that tristate can best be used in combination with <i>required</i> and that the <i>required</i> property is useless when used in combination with the two-state (as there will always be an option selected anyway).
 * <p>The default value when creating a BooleanSelect field is: <i>Required</i> == <i>Tristate</i>.<br />
 * Changing one of these properties after creation will not influence the other.
 * 
 * <h4>Values</h4>
 * <p>In holding with {@link IntegerField}, {@link DateField} and others the <code>BooleanSelect</code> field provides <code>getBoolean()</code> and <code>setBoolean()</code> methods.
 * <p>The <code>setValue()</code> and <code>setObjectValue()</code> only allow Boolean objects or Strings(containing "true" or "false").
 * <p><code>null</code> or empty strings are allowed in all these method and means: value not set (unset) -- in keeping with the other fields <code>null</code> values are stored and returned as "" (empty string). 
 * The <code>getValueObject()</code> method does not return an empty string but <code>null</code>.
 * 
 * <h4>Makeup</h4>
 * <p>The labels used for the true and false options can be customized in two ways: manually or with one of the built-in notations<br/>
 * <p>Use the <code>setOptionLabels()</code> conveniencemethods or the individual properties to manually set.
 * <p>Or use the <code>setNotation()</code> method to use one of the built-in notations.
 * <p>Following notations are provided and backed by the resource-bundle (i18n).
 * <li> <code>TRUE / FALSE</code> -- <i>default</i>
 * <li> <code>YES / NO</code>
 * <li> <code>ON / OFF</code>
 * <li> <code>ACTIVE / INACTIVE</code>
 * <li> <code>OPEN / CLOSED</code>
 * <p>&nbsp;
 * <p>Remark: Options are created in <code>control.onInit()</code> all makeup needs to be done in <code>page.onInit()</code> or earlier, changes made in <code>onRender()</code> or <code>onPost()</code> (actionlisteners) will not be reflected in the final result.
 * 
 * <p>&nbsp;
 * @see Select
 * @author Christopher Highway
 */
public class BooleanSelect extends Select {
    private static final long  serialVersionUID = 1L;

    public static final String CUSTOM           = "_custom_";
    public static final String TRUEFALSE        = "default";
    public static final String YESNO            = "yesno";
    public static final String ONOFF            = "onoff";
    public static final String ACTIVEINACTIVE   = "activeinactive";
    public static final String OPENCLOSED       = "openclosed";

    private boolean            tristate         = false;
    private String             notation         = TRUEFALSE;
    private String             customTrue       = null;
    private String             customFalse      = null;
    private String             customUnset      = null;

    /**
     * Create a Select field with no name defined.
     * <p/>
     * <b>Please note</b> the control's name must be defined before it is valid.
     */
    public BooleanSelect() {
        super();
    }

    /**
     * Create a Select field with the given name.
     *
     * @param name the name of the field
     */
    public BooleanSelect(String name) {
        super(name);
    }

    /**
     * Create a Select field with the given name.
     * if required is true, tristate will automatically be set, as required cannot be properly tested otherwise
     *
     * @param name
     * @param required
     */
    public BooleanSelect(String name, boolean required) {
        super(name, required);
        setTristate(required);
    }

    /**
     * Create a Select field with the given name and label.
     *
     * @param name the name of the field
     * @param label the label of the field
     */
    public BooleanSelect(String name, String label) {
        super(name, label);
    }

    /**
     * Create a Select field with the given name and label.
     *
     * @param name the name of the field
     * @param label the label of the field
     * @param notation the notation to be used for the optionlabels
     */
    public BooleanSelect(String name, String label, String notation) {
        super(name, label);
        setNotation(notation);
    }

    /**
     * Create a Select field with the given name and label.
     * 
     * if required is true, tristate will automatically be set, as required cannot be properly tested otherwise
     * @param name
     * @param label the label of the field
     * @param required
     */
    public BooleanSelect(String name, String label, boolean required) {
        super(name, label, required);
        setTristate(required);
    }

    /**
     * Create a Select field with the given name and label.
     * 
     * if required is true, tristate will automatically be set, as required cannot be properly tested otherwise
     * @param name
     * @param label the label of the field
     * @param notation the notation to be used for the optionlabels
     * @param required
     */
    public BooleanSelect(String name, String label, String notation, boolean required) {
        super(name, label, required);
        setTristate(required);
        setNotation(notation);
    }

    // ------------------------------------------------------------------

    /**
     * Options are added to the Select control. Make sure you call super.onInit() if you override this method
     */
    public void onInit() {
        if (getOptionList().size() > 0) getOptionList().clear(); // being called more than once? We could keep the existing options, but they might have changed
        if (tristate) add(new StyledOption("", "unset", getOptionLabelUnset()));
        add(new StyledOption(Boolean.TRUE.toString(), "true", getOptionLabelTrue()));
        add(new StyledOption(Boolean.FALSE.toString(), "false", getOptionLabelFalse()));
        super.onInit();
    }

    /**
     * @return <code>True</code> or <code>False</code> if value was set, <code>null</code> otherwise
     */
    public Boolean getBoolean() {
        String value = getValue();
        if (StringUtils.isEmpty(value)) return null; // we need this extra check as Boolean.valueOf does not return null but false on illegal values.
        return Boolean.valueOf(getValue());
    }

    /**
     * @param value can be <code>null</code>, meaning not set to any value
     */
    public void setBoolean(Boolean value) {
        this.value = ((value == null) ? "" : value.toString());
    }

    public void setValueObject(Object object) {
        if (object == null)
            setValue("");
        else
            setValue(object.toString());
    }

    public Object getValueObject() {
        return getBoolean();
    }

    /**
     * Selecting multiple options doesn't make sense for a boolean field, use the tristate option if you want to be able to not select any option.
     * <p>
     * As we do not test for multiple options this method is disabled, setting to false does nothing, setting to true generates an exception
     * 
     * @throws IllegalArgumentException if true is passed as value
     */
    public void setMultiple(boolean value) {
        if (value) throw new IllegalArgumentException("This does not make sense for a boolean selectfield, use 'tristate' if you want to be able to not select any option.");
        // ignore as it's already false
    }

    /**
     * conveniencemethod to set all optionlabels + notation to CUSTOM at once
     * <p>
     * the unsetOptionLabel is set to ""
     * @param trueOptionLabel
     * @param falseOptionLabel
     */
    public void setOptionLabels(String trueOptionLabel, String falseOptionLabel) {
        setOptionLabels(trueOptionLabel, falseOptionLabel, "");
    }

    /**
     * conveniencemethod to set all optionlabels + notation to CUSTOM at once
     * @param trueOptionLabel
     * @param falseOptionLabel
     * @param unsetOptionLabel
     */
    public void setOptionLabels(String trueOptionLabel, String falseOptionLabel, String unsetOptionLabel) {
        setOptionLabelTrue(trueOptionLabel);
        setOptionLabelFalse(falseOptionLabel);
        setOptionLabelUnset(unsetOptionLabel);
        setNotation(CUSTOM);
    }

    /**
     * returns the notation used for the optionlabels
     * @return
     */
    public String getNotation() {
        return notation;
    }

    /**
     * Change the labels used for the options with one of the built-in notations.<br />
     * Following notations are provided and backed by the resource-bundle (i18n).<br/>
     * true/false, yes/no, on/off, active/inactive, open/closed
     * <p>The prefered way of setting the notation is by using the static properties of this class:<br />
     * <code>    setNotation(BooleanSelect.YESNO);</code>
     * <p>Notation names:
     * <li> <code>TRUEFALSE</code> -- <i>default</i> 
     * <li> <code>YESNO</code>
     * <li> <code>ONOFF</code>
     * <li> <code>ACTIVEINACTIVE</code>
     * <li> <code>OPENCLOSED</code>
     * <li> <code>CUSTOM</code> -- <i>this option is automatically set when you provide custom labels with: <code>setOptionLabels()</code></i> 
     * @param notation
     */
    public void setNotation(String notation) {
        // TODO caveat: this method is not very robust as you can add an illegal argument, it does allow you to add custom values to the i18n resource bundle without recompiling though
        if (StringUtils.isEmpty(notation)) throw new IllegalArgumentException("" + notation + " is not a valid option for notation. (Where are those enums when you need them?!");
        this.notation = notation;
    }

    public boolean isTristate() {
        return tristate;
    }

    /**
     * Tristate decides if the the select field contains two or three options.
     *
     * @param tristate false = 2 options, true = 3 options
     */
    public void setTristate(boolean tristate) {
        this.tristate = tristate;
    }

    /**
     * get the label to use for the true option
     * @return custom value or message from languagebundle according to the chosen notation
     */
    public String getOptionLabelTrue() {
        String res;
        if (CUSTOM.equals(notation)) {
            if (customTrue == null) throw new IllegalStateException("You must set custom option labels when you choose CUSTOM notation.");
            res = customTrue;
        } else {
            res = getMessage(notation + "-true");
            if (res == null) throw new RuntimeException("Could not find resource with name: " + notation + "-true");
        }
        return res;
    }

    /**
     * set the label of the true option
     * @param optionLabel
     */
    public void setOptionLabelTrue(String optionLabel) {
        if (StringUtils.isEmpty(optionLabel)) throw new IllegalArgumentException("You must provide a value for the TrueOptionLabel");
        this.customTrue = optionLabel;
    }

    /**
     * get the label to use for the false option
     * @return custom value or message from languagebundle according to the chosen notation
     */
    public String getOptionLabelFalse() {
        String res;
        if (CUSTOM.equals(notation)) {
            if (customFalse == null) throw new IllegalStateException("You must set custom option labels when you choose CUSTOM notation.");
            res = customFalse;
        } else {
            res = getMessage(notation + "-false");
            if (res == null) throw new RuntimeException("Could not find resource with name: " + notation + "-false");
        }
        return res;
    }

    /**
     * set the label of the false option
     * @param optionLabel
     */
    public void setOptionLabelFalse(String optionLabel) {
        if (StringUtils.isEmpty(optionLabel)) throw new IllegalArgumentException("You must provide a value for the FalseOptionLabel");
        this.customFalse = optionLabel;
    }

    /**
     * get the label to use for the unset option
     * @return custom value or "" (empty string)
     */
    public String getOptionLabelUnset() {
        String res;
        if (CUSTOM.equals(notation)) {
            if (customUnset == null) throw new IllegalStateException("You must set custom option labels when you choose CUSTOM notation.");
            res = customUnset;
        } else {
            res = ""; // there are no individual messages for the unset option, needed?
        }
        return res;
    }

    /**
     * set the label of the unset option
     * @param optionLabel
     */
    public void setOptionLabelUnset(String optionLabel) {
        if (optionLabel == null) throw new IllegalArgumentException("You must provide a value for the UnsetOptionLabel"); // empty is allowed
        this.customUnset = optionLabel;
    }
}
