/*
 * Copyright 2008 Bob Schellink
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.clickclick.util;

import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Provides a HEAD entry for importing <tt>inline</tt> Cascading Stylesheets
 * through the &lt;style&gt; element.
 * <p/>
 * Example usage:
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *     public void onInit() {
 *         Css css = new Css("body { font: 12px arial; }");
 *         getHeadEntries().add(css);
 *     }
 * }
 * </pre>
 *
 * The <tt>css</tt> element will render the output:
 *
 * <pre class="prettyprint">
 * &lt;style type="text/css" rel="stylesheet"&gt;
 * body { font: 12px arial; }
 * &lt;/style&gt;
 * </pre>
 *
 * Below is an example showing how to create an inline CSS from a Velocity
 * template:
 * <p/>
 * <h3>webapp/css/style-template.css</h3>
 * <pre class="prettyprint">
 * .blue {
 *     background: #00ff00 url('$context/css/blue.png') no-repeat fixed center;
 * }
 * </pre>
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *     public void onInit() {
 *         // Create a default template model to pass to the template
 *         Map model = ClickUtils.createTemplateModel(this, getContext());
 *
 *         // Specify the path to CSS Velocity template
 *         String cssTemplate = "/css/style-template.css";
 *
 *         // Render the template providing it with the model
 *         String template = getContext().renderTemplate(cssTemplate, model);
 *
 *         // Create the inline Css for the given template
 *         Css css = new Css(template);
 *         getHeadEntries().add(css);
 *     }
 * }
 * </pre>
 *
 * The <tt>css</tt> above will render as follows (assuming the context path is
 * <tt>myApp</tt>):
 *
 * <pre class="prettyprint">
 * &lt;style type="text/css" rel="stylesheet"&gt;
 * .blue {
 *     background: #00ff00 url('/myApp/css/blue.png') no-repeat fixed center;
 * }
 * &lt;/style&gt;
 * </pre>
 *
 * @author Bob Schellink
 */
public class Css extends Head {

    // -------------------------------------------------------------- Variables

    /** A buffer holding the inline CSS content. */
    private HtmlStringBuffer content = new HtmlStringBuffer();

    // ------------------------------------------------------------ Constructor

    /**
     * Construct a new inline CSS element.
     */
    public Css() {
        this(null);
    }

    /**
     * Construct a new inline CSS element with the given content.
     *
     * @param content the CSS content
     */
    public Css(String content) {
        if (content != null) {
            this.content.append(content);
        }
        setAttribute("type", "text/css");
        setAttribute("rel", "stylesheet");
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Returns the Css HTML tag: &lt;style&gt;.
     *
     * @return the Css HTML tag: &lt;style&gt;
     */
    public String getTag() {
        return "style";
    }

    /**
     * Return the CSS internal content buffer.
     *
     * @return the CSS internal content buffer
     */
    public HtmlStringBuffer getContent() {
        return content;
    }

    /**
     * Set the CSS internal content buffer.
     *
     * @param content the new content buffer
     */
    public void setInclude(HtmlStringBuffer content) {
        this.content = content;
    }

    /**
     * Append the given CSS string to the internal content buffer.
     *
     * @param content the CSS string to append to the internal content buffer
     */
    public void append(String content) {
        this.content.append(content);
    }

    /**
     * Render the HTML representation of the CSS to the specified buffer.
     *
     * @param buffer the buffer to render output to
     */
    public void render(HtmlStringBuffer buffer) {

        // Render IE conditional comment if conditional comment was set
        renderConditionalCommentPrefix(buffer);

        buffer.elementStart(getTag());
        appendAttributes(buffer);
        buffer.closeTag();

        // Render CDATA tag if necessary
        renderCharacterDataPrefix(buffer);

        buffer.append(getContent());

        renderCharacterDataSuffix(buffer);

        buffer.elementEnd(getTag());

        renderConditionalCommentSuffix(buffer);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!isUnique()) {
            return super.equals(o);
        }

        //1. Use the == operator to check if the argument is a reference to this object.
        if (o == this) {
            return true;
        }

        //2. Use the instanceof operator to check if the argument is of the correct type.
        if (!(o instanceof Css)) {
            return false;
        }

        //3. Cast the argument to the correct type.
        Css that = (Css) o;

        String id = getId();
        String thatId = that.getId();
        return id == null ? thatId == null : id.equals(thatId);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashcode() {
        if (!isUnique()) {
            return super.hashCode();
        }
        return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * @see Head#setUnique(boolean)
     *
     * @deprecated use {@link #setId(java.lang.String)} instead
     *
     * @param unique sets whether the Head import should be unique or not
     */
    void setUnique(boolean unique) {
        super.setUnique(unique);

        // If CSS is unique and ID is not defined, derive the ID from the content
        if (unique && StringUtils.isBlank(getId()) && getContent().length() > 0) {
            int hash = getContent().toString().hashCode();
            setId(Integer.toString(hash));
        }
    }
}
