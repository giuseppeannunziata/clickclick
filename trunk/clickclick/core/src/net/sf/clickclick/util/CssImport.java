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
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Provides a HEAD entry for importing <tt>external</tt> Cascading Stylesheets
 * through the &lt;link&gt; element.
 * <p/>
 * Example usage:
 * <pre class="prettyprint">
 * public class MyPage extends Page {
 *     public void onInit() {
 *         // Indicate that the web application context path should be prepended
 *         // to the CSSImport HREF attribute.
 *         boolean prefixWithContextPath = true;
 *
 *         CssImport cssImport = new CssImport("/css/style.css", prefixWithContextPath);
 *         getHeadEntries().add(cssImport);
 *     }
 * }
 * </pre>
 *
 * The <tt>cssImport</tt> above will be rendered as follows (assuming the context
 * path is <tt>myApp</tt>):
 * <pre class="prettyprint">
 * &lt;link type="text/css" rel="stylesheet" href="/myApp/css/style.css"/&gt;
 * </pre>
 *
 * @author Bob Schellink
 */
public class CssImport extends Head {

    // ----------------------------------------------------------- Constructors

    /**
     * Constructs a new CssImport link.
     * <p/>
     * The {@link net.sf.click.util.ClickUtils#getApplicationResourceVersionIndicator(net.sf.click.Context) application version indicator}
     * will be set as the default <tt>version indicator</tt> for the CssImport
     * <tt>href</tt> attribute.
     */
    public CssImport() {
        this(null);
    }

    /**
     * Construct a new CssImport link with the specified <tt>href</tt> attribute.
     * <p/>
     * The {@link net.sf.click.util.ClickUtils#getApplicationResourceVersionIndicator(net.sf.click.Context) application version indicator}
     * will be set as the default <tt>version indicator</tt> for the CssImport
     * <tt>href</tt> attribute.
     *
     * @param href the CSS link href attribute
     */
    public CssImport(String href) {
        this(href, false);
    }

    /**
     * Construct a new CssImport link with the specified <tt>href</tt> attribute
     * and indicator which specifies whether the <tt>href</tt> attribute should
     * be prefixed with the web application <tt>context path</tt>.
     * <p/>
     * The {@link net.sf.click.util.ClickUtils#getApplicationResourceVersionIndicator(net.sf.click.Context) application version indicator}
     * will be set as the default <tt>version indicator</tt> for the CssImport
     * <tt>href</tt> attribute.
     *
     * @param href the CSS link href attribute
     * @param prefixWithContextPath indicates whether the specified <tt>href</tt>
     * attribute should be prefixed with the web application <tt>context path</tt>
     */
    public CssImport(String href, boolean prefixWithContextPath) {
        this(href, prefixWithContextPath, null);
        setVersionIndicator(ClickClickUtils.getApplicationResourceVersionIndicator(getContext()));
    }

    /**
     * Construct a new CssImport link with the specified <tt>href</tt> attribute,
     * version indicator and indicator which specifies whether the <tt>href</tt>
     * attribute should be prefixed with the web application <tt>context path</tt>.
     *
     * @param href the CSS link href attribute
     * @param prefixWithContextPath indicates whether the specified <tt>href</tt>
     * attribute should be prefixed with the web application <tt>context path</tt>
     * @param versionIndicator the version indicator to add to the href path
     */
    public CssImport(String href, boolean prefixWithContextPath,
        String versionIndicator) {
        if (href != null) {
            if (prefixWithContextPath) {
                HtmlStringBuffer sourceBuffer = new HtmlStringBuffer(href.length());
                // Append the context path
                sourceBuffer.append(getContext().getRequest().getContextPath());
                sourceBuffer.append(href);
                setHref(sourceBuffer.toString());
            } else {
                setHref(href);
            }
        }
        setAttribute("type", "text/css");
        setAttribute("rel", "stylesheet");
        setVersionIndicator(versionIndicator);
    }

    // ------------------------------------------------------ Public Properties

    /**
     * Returns the Css import HTML tag: &lt;link&gt;.
     *
     * @return the Css import HTML tag: &lt;link&gt;
     */
    public String getTag() {
        return "link";
    }

    /**
     * This method always return true because CSS import must be unique based on
     * its <tt>href</tt> attribute. In other words the Page HEAD should only
     * contain a single CSS import for the specific <tt>href</tt>.
     *
     * @see Head#isUnique()
     *
     * @return true because CSS import must unique based on its <tt>href</tt>
     * attribute
     */
    public boolean isUnique() {
        return true;
    }

    /**
     * Sets the <tt>href</tt> attribute.
     *
     * @param href the new href attribute
     */
    public void setHref(String href) {
        setAttribute("href", href);
    }

    /**
     * Return the <tt>href</tt> attribute.
     *
     * @return the href attribute
     */
    public String getHref() {
        return getAttribute("href");
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Render the HTML representation of the CSS import to the specified buffer.
     *
     * @param buffer the buffer to render output to
     */
    public void render(HtmlStringBuffer buffer) {
        setHref(addVersionIndicator(getHref(), ".css"));
        
        renderConditionalCommentPrefix(buffer);

        buffer.elementStart(getTag());
        appendAttributes(buffer);
        buffer.elementEnd();

        renderConditionalCommentSuffix(buffer);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (getHref() == null) {
            throw new IllegalStateException("'href' attribute is not defined.");
        }

        //1. Use the == operator to check if the argument is a reference to this object.
        if (o == this) {
            return true;
        }

        //2. Use the instanceof operator to check if the argument is of the correct type.
        if (!(o instanceof CssImport)) {
            return false;
        }

        //3. Cast the argument to the correct type.
        CssImport that = (CssImport) o;

        return getHref() == null ? that.getHref() == null :
            getHref().equals(that.getHref());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashcode() {
        return new HashCodeBuilder(17, 37).append(getHref()).toHashCode();
    }

    // ------------------------------------------------ Package Private Methods

    /**
     * This operation is not supported because CSS imports is always unique
     * based on their <tt>href</tt> attribute.
     *
     * @see Head#setUnique(boolean)
     *
     * @param unique sets whether the Css import should be unique or not
     */
    void setUnique(boolean unique) {
        throw new UnsupportedOperationException("CssImport is always"
            + " unique based on its 'href' attribute");
    }
}
