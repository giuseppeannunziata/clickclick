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
 * Provides a HEAD entry for importing <tt>inline</tt> Javascript through the
 * &lt;script&gt; element.
 * <p/>
 * Example usage:
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *     public void onInit() {
 *         Javascript javascript = new Javascript("alert('Hello World!);");
 *         getHeadEntries().add(javascript);
 *     }
 * }
 * </pre>
 *
 * The <tt>javascript</tt> element will render the output:
 *
 * <pre class="prettyprint">
 * &lt;script type="text/javascript"&gt;
 * alert('Hello World');
 * &lt;/script&gt;
 * </pre>
 *
 * Below is an example showing how to create an inline Javascript from a
 * Velocity template:
 * <p/>
 * <h3>webapp/js/mycorp-template.js</h3>
 * <pre class="prettyprint">
 * hide = function() {
 *     var div = document.getElementById('${divId}');
 *     div.style.display = "none";
 * }
 * </pre>
 *
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *     public void onInit() {
 *         // Create a default template model to pass to the template
 *         Map model = ClickUtils.createTemplateModel(this, getContext());
 *
 *         // Add the id of the div to hide
 *         model.put("divId", "myDiv");
 *
 *         // Specify the path to JAvascript Velocity template
 *         String jsTemplate = "/js/mycorp-template.js";
 *
 *         // Render the template providing it with the model
 *         String template = getContext().renderTemplate(jsTemplate, model);
 *
 *         // Create the inline Javascript for the given template
 *         Javascript javascript = new Javascript(template);
 *         getHeadEntries().add(javascript);
 *     }
 * }
 * </pre>
 *
 * The <tt>javascript</tt> above will render as follows (assuming the context path is
 * <tt>myApp</tt>):
 *
 * <pre class="prettyprint">
 * &lt;script type="text/javascript"&gt;
 *     hide = function() {
 *         var div = document.getElementById('myDiv');
 *         div.style.display = "none";
 *     }
 * &lt;/style&gt;
 * </pre>
 *
 * @author Bob Schellink
 */
public class Javascript extends Head {

    // -------------------------------------------------------------- Constants

    /**
     * Indicator for Javascript to be placed in the HEAD section of an HTML
     * page.
     */
    public static final int HEAD = 0;

    /**
     * Indicator for Javascript to be placed at the bottom of the BODY section
     * of an HTML page.
     */
    public static final int BODY = 1;

    // -------------------------------------------------------------- Variables

    /** The current position of the Javascript in the HTML page. */
    private int position = BODY;

    /** A buffer holding the inline CSS content. */
    private HtmlStringBuffer content = new HtmlStringBuffer();

    // ----------------------------------------------------------- Constructors

    /**
     * Construct a new inline Javascript element.
     */
    public Javascript() {
        this(null, BODY);
    }

    /**
     * Construct a new inline Javascript element with the given content.
     *
     * @param content the Javascript content
     */
    public Javascript(String content) {
        this(content, BODY);
    }

    /**
     * Construct a new inline Javascript element with the given content at the
     * specified position.
     *
     * @param content the Javascript content
     * @param position the position of the Javascript in the HTML page
     */
    public Javascript(String content, int position) {
        if (content != null) {
            this.content.append(content);
        }
        setAttribute("type", "text/javascript");
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Returns the Javascript HTML tag: &lt;script&gt;.
     *
     * @return the Javascript HTML tag: &lt;script&gt;
     */
    public String getTag() {
        return "script";
    }

    /**
     * Return the Javascript internal content buffer.
     *
     * @return the Javascript internal content buffer
     */
    public HtmlStringBuffer getContent() {
        return content;
    }

    /**
     * Set the Javascript internal content buffer.
     *
     * @param content the new content buffer
     */
    public void setContent(HtmlStringBuffer content) {
        this.content = content;
    }

    /**
     * Set the <tt>position</tt> property, valid values are {@link #HEAD} and
     * {@link #BODY}.
     *
     * @see #setPosition(int)
     *
     * @param position the new position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Return the <tt>position</tt> of the JavaScript import in the HTML page.
     * Valid values are {@link #HEAD} and {@link #BODY}.
     * <p/>
     * The position property is a hint where the JavaScript import should be
     * placed in the HTML page.
     * <p/>
     * Please see {@link net.sf.click.util.PageImports} on options where imports
     * can be placed on the page.
     *
     * @return the position of the Javascript import in the HTML page
     */
    public int getPosition() {
        return position;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Append the given Javascript string to the internal content buffer.
     *
     * @param content the Javascript string to append to the internal content
     * buffer
     */
    public void append(String content) {
        this.content.append(content);
    }

    /**
     * Render the HTML representation of the JavaScript to the specified
     * buffer.
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
        if (!(o instanceof Javascript)) {
            return false;
        }

        //3. Cast the argument to the correct type.
        Javascript that = (Javascript) o;

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
