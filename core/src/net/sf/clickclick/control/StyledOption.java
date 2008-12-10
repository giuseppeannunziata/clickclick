package net.sf.clickclick.control;

import java.util.List;

import net.sf.click.control.Option;
import net.sf.click.control.Select;
import net.sf.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;

/**
 * The StyledOption class adds the <code>id</code> attribute to the html option element thus allowing you to add specific css makeup to individual html instances
 * <p>
 * The id is generated as follows: <code>formName-selectName-optionName</code>
 * Please note that the possible styles that can be applied is limited and rendering differs greatly between browser
 * <p>
 * Tested elements:<br />
 * Safe on all browsers: <code>background-color</code> and <code>color</code>
 * <p>Firefox also supports background-image, font, border and padding.
 * <p>Firefox however does not apply the style of the selected element to the Select control. You can simulate this behaviour with Javascript if you so wish (see samplepage)
 * <p>See the samplepage for inspiration
 *
 * @author Christopher Highway
 */
public class StyledOption extends Option {
    private static final long serialVersionUID = 1L;

    private final String      name;
    private String            id;

    /**
     * use the constructor <code>StyledOption(value, name, label)</code> if the label has to be empty.
     * @param value will be used as value, name and label
     */
    public StyledOption(String value) {
        super(value);
        if (StringUtils.isEmpty(value)) throw new IllegalArgumentException("Value cannot be null if it is also to be used as Name.");
        name = value;
    }

    /**
     * use the constructor <code>StyledOption(value, name, label)</code> if the label has to be empty.
     * @param value
     * @param name the name is also used as label
     */
    public StyledOption(Object value, String name) {
        super(value, name);
        if (StringUtils.isEmpty(name)) throw new IllegalArgumentException("Please provide a name for this option.");
        this.name = name;
    }

    /**
     * individual properties for value, name and label
     * @param value
     * @param name
     * @param label can be empty
     */
    public StyledOption(Object value, String name, String label) {
        super(value, label);
        if (StringUtils.isEmpty(name)) throw new IllegalArgumentException("Please provide a name for this option.");
        this.name = name;
    }

    // ------------------------------------------------------------------

    /**
     * @return name of the option
     */
    public String getName() {
        return name;
    }

    /**
     * return the custom id set for this option
     * @return the HTML id attribute
     */
    public String getId() {
        return id;
    }

    /**
     * the id of this option, leave empty to let StyledOption generate the default id (formName-selectName-optionName)
     *
     * @param id the HTML id attribute
     */
    public void setId(String id) {
        this.id = id;
    }

    // ------------------------------------------------------------------

    /**
     * copied everything from Option, just adding the id attribute
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
        buffer.appendAttribute("id", getIdAttr(select));

        buffer.closeTag();
        buffer.appendEscaped(getLabel());
        buffer.elementEnd(getTag());
    }

    /**
     * option is not a field so we need to pass the parent manually
     * @param select
     * @return
     */
    String getIdAttr(Select select) {
        if (StringUtils.isEmpty(id)) {
            String result = null;
            if (select != null) result = select.getId();
            if (StringUtils.isEmpty(result))
                result = "";
            else
                result += "_";
            return result + name;
        } else {
            return id;
        }
    }
}
