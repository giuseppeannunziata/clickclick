package net.sf.clickclick.examples.page.controls;

import java.util.List;

import net.sf.click.control.Option;
import net.sf.click.control.Select;
import net.sf.click.util.HtmlStringBuffer;
import net.sf.clickclick.control.BooleanSelect;

/**
 * <p>This class still has some issues
 * <li><code>Option</code> does not have style -attribute or -class properties.</li>
 * <p>so a new extended <code>StyledOption</code> was made to support this, it's just two strings though, not the fullfledged system as you find in 
 * <code>Control</code>s. The reason is that we have to buffer the properties for the three options in the Select as the options are not immediately 
 * created, but in <code>onInit()</code>. This is also the reason it was created as inner class.<br>
 * If you want to use it with other Selects, <code>OptionGroup</code> should be overriden as well, which isn't done here as it isn't needed.
 * <h4>Use</h4>
 * The default styleclassnames of the options is "unsetOption", "trueOption", "falseOption". You can use these in your stylesheet for a uniform layout of the options.
 * <p>If you want to customize a specific instance you can use the <code>Option[Unset/true/false]StyleClass</code properties.
 * <p>&nbsp;
 * @author Christopher Highway
 */
public class StyledBooleanSelect extends BooleanSelect {
    private static final long serialVersionUID = 1L;

    private String            optionUnsetStyleClass = "unsetOption";
    private String            optionTrueStyleClass = "trueOption";
    private String            optionFalseStyleClass = "falseOption";
    private String            optionUnsetStyleAttributes;
    private String            optionTrueStyleAttributes;
    private String            optionFalseStyleAttributes;

    public StyledBooleanSelect() {
        super();
    }

    public StyledBooleanSelect(String name, boolean required) {
        super(name, required);
    }

    public StyledBooleanSelect(String name, String label, boolean required) {
        super(name, label, required);
    }

    public StyledBooleanSelect(String name, String label, String notation, boolean required) {
        super(name, label, notation, required);
    }

    public StyledBooleanSelect(String name, String label, String notation) {
        super(name, label, notation);
    }

    public StyledBooleanSelect(String name, String label) {
        super(name, label);
    }

    public StyledBooleanSelect(String name) {
        super(name);
    }

    // ------------------------------------------------------------------

    public void onInit() {
        if (getOptionList().size() > 0) getOptionList().clear(); // being called more than once? We could keep the existing options, but they might have changed
        if (isTristate()) add(new StyledOption("", getOptionLabelUnset(), getOptionUnsetStyleClass(), getOptionUnsetStyleAttributes()));
        add(new StyledOption(Boolean.TRUE.toString(), getOptionLabelTrue(), getOptionTrueStyleClass(), getOptionTrueStyleAttributes()));
        add(new StyledOption(Boolean.FALSE.toString(), getOptionLabelFalse(), getOptionFalseStyleClass(), getOptionFalseStyleAttributes()));
    }

    /**
     * very ugly hack this, we could inherit from Control, but an option is not really a control as it is only part of one, but it can get the style attribute...
     * I started out with copying everything from AbstractControl, but this is really overkill...
     * 
     * might/should include "class" attribute so you can style using css file
     */
    public static class StyledOption extends Option {
        private static final long serialVersionUID = 1L;
        private String            styleAttributes;
        private String            styleClass;

        public StyledOption(String value) {
            super(value);
        }

        public StyledOption(Object value, String label) {
            super(value, label);
        }

        public StyledOption(Object value, String label, String styleClass, String styleAttributes) {
            super(value, label);
            this.styleClass = styleClass;
            this.styleAttributes = styleAttributes;
        }

        // ------------------------------------------------------------------

        /**
         * copied everything from Option, just adding the style attributes / class if necessary
         */
        public void render(Select select, HtmlStringBuffer buffer) {
            buffer.elementStart(getTag());

            if (select.isMultiple()) {

                if (!select.getSelectedValues().isEmpty()) {

                    // Search through selection list for matching value
                    List values = select.getSelectedValues();
                    for (int i = 0, size = values.size(); i < size; i++) {
                        String value = values.get(i).toString();
                        if (getValue().equals(value)) {
                            buffer.appendAttribute("selected", "selected");
                            break;
                        }
                    }
                }

            } else {
                if (getValue().equals(select.getValue())) {
                    buffer.appendAttribute("selected", "selected");
                }
            }

            buffer.appendAttribute("value", getValue());

            // -- begin addition
            if (styleClass != null && !styleClass.isEmpty()) buffer.appendAttribute("class", styleClass);
            if (styleAttributes != null && !styleAttributes.isEmpty()) buffer.appendAttribute("style", styleAttributes);
            // -- end 

            buffer.closeTag();
            buffer.appendEscaped(getLabel());
            buffer.elementEnd(getTag());
        }

        // ------------------------------------------------------------------
        
        public String getStyleAttributes() {
            return styleAttributes;
        }

        public void setStyleAttributes(String styleAttributes) {
            this.styleAttributes = styleAttributes;
        }

        public String getStyleClass() {
            return styleClass;
        }

        public void setStyleClass(String styleClass) {
            this.styleClass = styleClass;
        }
    }

    public String getOptionUnsetStyleClass() {
        return optionUnsetStyleClass;
    }

    public void setOptionUnsetStyleClass(String className) {
        this.optionUnsetStyleClass = className;
    }

    public String getOptionTrueStyleClass() {
        return optionTrueStyleClass;
    }

    public void setOptionTrueStyleClass(String className) {
        this.optionTrueStyleClass = className;
    }

    public String getOptionFalseStyleClass() {
        return optionFalseStyleClass;
    }

    public void setOptionFalseStyleClass(String className) {
        this.optionFalseStyleClass = className;
    }

    public String getOptionUnsetStyleAttributes() {
        return optionUnsetStyleAttributes;
    }

    public void setOptionUnsetStyleAttributes(String attributes) {
        this.optionUnsetStyleAttributes = attributes;
    }

    public String getOptionTrueStyleAttributes() {
        return optionTrueStyleAttributes;
    }

    public void setOptionTrueStyleAttributes(String attributes) {
        this.optionTrueStyleAttributes = attributes;
    }

    public String getOptionFalseStyleAttributes() {
        return optionFalseStyleAttributes;
    }

    public void setOptionFalseStyleAttributes(String attributes) {
        this.optionFalseStyleAttributes = attributes;
    }
}
