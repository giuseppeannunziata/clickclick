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

import org.apache.click.util.ClickUtils;
import org.apache.click.util.HtmlStringBuffer;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Provides a JavaScript HEAD entry for importing <tt>external</tt> JavaScripts
 * through the &lt;script&gt; element.
 * <p/>
 * Example usage:
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *     public void onInit() {
 *         // Indicate that the web application context path should be prepended
 *         // to the JavaScript SRC attribute.
 *         boolean prefixWithContextPath = true;
 *
 *         JavascriptImport jsImport = new JavascriptImport("/js/js-library.js", prefixWithContextPath);
 *         getHeadEntries().add(jsImport);
 *     }
 * }
 * </pre>
 *
 * The <tt>jsImport</tt> above will be rendered as follows (assuming the context
 * path is <tt>myApp</tt>):
 * <pre class="prettyprint">
 * &lt;script type="text/javascript" href="/myApp/js/js-library.js"&gt;&lt;/script&gt;
 * </pre>
 *
 * @author Bob Schellink
 */
public class JavascriptImport extends Head {

    // ------------------------------------------------------------- Constatnts

    /**
     * Indicator for JavascriptImport to be placed in the HEAD section of an
     * HTML page.
     */
    public static final int HEAD = 0;

    /**
     * Indicator for JavascriptImport to be placed at the bottom of the BODY
     * section of an HTML page.
     */
    public static final int BODY = 1;

    // -------------------------------------------------------------- Variables

    /** The current position of the JavascriptImport in the HTML page. */
    private int position = BODY;

    // ----------------------------------------------------------- Constructors

    /**
     * Constructs a new JavascriptImport.
     */
    public JavascriptImport() {
        this(null, BODY);
    }

    /**
     * Construct a new JavascriptImport with the specified <tt>src</tt> attribute.
     * <p/>
     * The {@link net.sf.click.util.ClickUtils#getApplicationResourceVersionIndicator(net.sf.click.Context) application version indicator}
     * will be set as the default <tt>version indicator</tt> for the
     * JavascriptImport <tt>src</tt> attribute.
     * 
     * @param src the Javascript src attribute
     */
    public JavascriptImport(String src) {
        this(src, BODY);
    }

    /**
     * Construct a new JavascriptImport with the specified <tt>src</tt> attribute
     * and position.
     * <p/>
     * The {@link net.sf.click.util.ClickUtils#getApplicationResourceVersionIndicator(net.sf.click.Context) application version indicator}
     * will be set as the default <tt>version indicator</tt> for the
     * JavascriptImport <tt>src</tt> attribute.
     *
     * @param src the Javascript src attribute
     * @param position the position of the import in the HTML page
     */
    public JavascriptImport(String src, int position) {
        this(src, position, false);
    }

    /**
     * Construct a new JavascriptImport with the specified <tt>src</tt> attribute
     * and indicator which specifies whether the <tt>src</tt> attribute should
     * be prefixed with the web application <tt>context path</tt>.
     * <p/>
     * The {@link net.sf.click.util.ClickUtils#getApplicationResourceVersionIndicator(net.sf.click.Context) application version indicator}
     * will be set as the default <tt>version indicator</tt> for the
     * JavascriptImport <tt>src</tt> attribute.
     *
     * @param src the JavascriptImport src attribute
     * @param prefixWithContextPath indicates whether the specified <tt>src</tt>
     * attribute should be prefixed with the web application <tt>context path</tt>
     */
    public JavascriptImport(String src, boolean prefixWithContextPath) {
        this(src, BODY, false);
    }

    /**
     * Construct a new JavascriptImport with the specified <tt>src</tt> attribute,
     * position and indicator which specifies whether the <tt>src</tt> attribute should
     * be prefixed with the web application <tt>context path</tt>.
     * <p/>
     * The {@link net.sf.click.util.ClickUtils#getApplicationResourceVersionIndicator(net.sf.click.Context) application version indicator}
     * will be set as the default <tt>version indicator</tt> for the
     * JavascriptImport <tt>src</tt> attribute.
     *
     * @param src the JavascriptImport src attribute
     * @param position the position of the import in the HTML page
     * @param prefixWithContextPath indicates whether the specified <tt>src</tt>
     * attribute should be prefixed with the web application <tt>context path</tt>
     */
    public JavascriptImport(String src, int position, boolean prefixWithContextPath) {
        this(src, position, prefixWithContextPath, null);
        setVersionIndicator(ClickClickUtils.getApplicationResourceVersionIndicator(getContext()));
    }

    /**
     * Construct a new JavascriptImport with the specified <tt>src</tt> attribute,
     * position, version indicator and indicator which specifies whether the
     * <tt>src</tt> attribute should be prefixed with the web application
     * <tt>context path</tt>.
     *
     * @param src the JavascriptImport src attribute
     * @param position the position of the import in the HTML page
     * @param prefixWithContextPath indicates whether the specified <tt>src</tt>
     * attribute should be prefixed with the web application <tt>context path</tt>
     * @param versionIndicator the version indicator to add to the src path
     */
    public JavascriptImport(String src, int position, boolean prefixWithContextPath,
        String versionIndicator) {
        if (src != null) {
            if (prefixWithContextPath) {
                HtmlStringBuffer sourceBuffer = new HtmlStringBuffer(src.length());
                // Append the context path
                sourceBuffer.append(getContext().getRequest().getContextPath());
                sourceBuffer.append(src);
                setSrc(sourceBuffer.toString());
            } else {
                setSrc(src);
            }
        }
        this.position = position;
        setAttribute("type", "text/javascript");
        setVersionIndicator(versionIndicator);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Returns the Css import HTML tag: &lt;script&gt;.
     *
     * @return the Css import HTML tag: &lt;script&gt;
     */
    public String getTag() {
        return "script";
    }

    /**
     * This method always return true because JavaScript import must be unique
     * based on its <tt>src</tt> attribute. In other words the Page HEAD should
     * only contain a single JavaScript import for the specific <tt>src</tt>.
     *
     * @see Head#isUnique()
     *
     * @return true because JavaScript import must unique based on its
     * <tt>src</tt> attribute
     */
    public boolean isUnique() {
        return true;
    }

    /**
     * Sets the <tt>src</tt> attribute.
     *
     * @param src the new src attribute
     */
    public void setSrc(String src) {
        setAttribute("src", src);
    }

    /**
     * Return the <tt>src</tt> attribute.
     *
     * @return the src attribute
     */
    public String getSrc() {
        return getAttribute("src");
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
     * Render the HTML representation of the JavaScript import to the specified
     * buffer.
     *
     * @param buffer the buffer to render output to
     */
    public void render(HtmlStringBuffer buffer) {
        setSrc(addVersionIndicator(getSrc(), ".js"));

        renderConditionalCommentPrefix(buffer);
        
        buffer.elementStart(getTag());
        appendAttributes(buffer);
        buffer.closeTag();
        
        buffer.elementEnd(getTag());
        
        renderConditionalCommentSuffix(buffer);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        //1. Use the == operator to check if the argument is a reference to this object.
        if (o == this) {
            return true;
        }

        //2. Use the instanceof operator to check if the argument is of the correct type.
        if (!(o instanceof JavascriptImport)) {
            return false;
        }

        //3. Cast the argument to the correct type.
        JavascriptImport that = (JavascriptImport) o;

        return getSrc() == null ? that.getSrc() == null :
            getSrc().equals(that.getSrc());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getSrc()).toHashCode();
    }
    
    // ------------------------------------------------ Package Private Methods

    /**
     * This operation is not supported because JavaScript imports is always
     * unique based on their <tt>src</tt> attribute.
     *
     * @see Head#setUnique(boolean)
     *
     * @param unique sets whether the JavaScript import should be unique or not
     */
    void setUnique(boolean unique) {
        throw new UnsupportedOperationException("JavascriptImport is always"
            + " unique based on its 'src' attribute");
    }
}
