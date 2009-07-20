package net.sf.clickclick.examples.jquery.control;

import java.util.List;
import net.sf.clickclick.control.panel.SimplePanel;
import org.apache.click.element.CssImport;
import org.apache.click.util.HtmlStringBuffer;

/**
 * Provides a Window control with a titlebar and content.
 *
 * @author Bob Schellink
 */
public class Window extends SimplePanel {

    // -------------------------------------------------------------- Variables

    private int width;

    private int height;

    private int titlebarHeight;

   // ----------------------------------------------------------- Constructors

    public Window() {
    }

    public Window(String name) {
        super(name);
    }

    // ------------------------------------------------------ Public Attributes

    /**
     * Return the width CSS style.
     *
     * @return the width CSS style
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the width CSS style.
     *
     * @param width the CSS width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the titlebarHeight
     */
    public int getTitlebarHeight() {
        return titlebarHeight;
    }

    /**
     * @param titlebarHeight the titlebarHeight to set
     */
    public void setTitlebarHeight(int titlebarHeight) {
        this.titlebarHeight = titlebarHeight;
    }

    // --------------------------------------------------------- Public Methods

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();
            headElements.add(new CssImport("/clickclick/example/window/window.css"));
        }
        return headElements;
    }

    public void render(HtmlStringBuffer buffer) {
        int titlebarHeight = getTitlebarHeight();
        if (titlebarHeight <= 0) {
            titlebarHeight = 18; // default
        }

        HtmlStringBuffer windowStyle = new HtmlStringBuffer();
        HtmlStringBuffer titlebarStyle = new HtmlStringBuffer();
        HtmlStringBuffer contentStyle = new HtmlStringBuffer();

        String styles = getAttribute("style");
        if (styles != null) {
            windowStyle.append(styles);
        }
        if (getWidth() != 0) {
            if (windowStyle.length() > 0) {
                windowStyle.append("; ");
            }
            windowStyle.append("width:").append(getWidth()).append("px");
            titlebarStyle.append("width:").append(getWidth() - 10).append("px");
            contentStyle.append("width:").append(getWidth() - 10).append("px");
        }
        if (getHeight() != 0) {
            if (windowStyle.length() > 0) {
                windowStyle.append("; ");
                titlebarStyle.append("; ");
            }
            windowStyle.append("height:").append(getHeight()).append("px");
            titlebarStyle.append("height:").append(titlebarHeight).append("px");
            int contentHeight = getHeight() - (titlebarHeight + 7);
            contentStyle.append("height:").append(contentHeight).append("px");
        }
        setAttribute("style", windowStyle.toString());

        buffer.elementStart("div");
        buffer.appendAttribute("name", getName());
        buffer.appendAttribute("id", getId());
        buffer.appendAttribute("class", "window");
        appendAttributes(buffer);
        buffer.closeTag();

        buffer.elementStart("div");
        buffer.appendAttribute("class", "titlebar");
        buffer.appendAttribute("style", titlebarStyle);
        buffer.closeTag();
        buffer.append(getLabel());
        buffer.elementEnd("div");

        buffer.elementStart("div");
        buffer.appendAttribute("class", "content");
        buffer.appendAttribute("style", contentStyle);
        buffer.closeTag();
        super.render(buffer);
        buffer.elementEnd("div");

        buffer.elementEnd("div");
    }
}
