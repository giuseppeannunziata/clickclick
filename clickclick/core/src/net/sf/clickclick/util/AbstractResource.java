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

import java.util.HashMap;
import java.util.Map;
import org.apache.click.Context;
import org.apache.click.util.HtmlStringBuffer;

/**
 *
 * @author Bob Schellink
 */
public abstract class AbstractResource {

    /** The Resource attributes Map. */
    protected Map attributes;

    /**
     * Returns the resource html tag.
     * <p/>
     * Subclasses should override this method and return the correct tag.
     * <p/>
     * This method returns <tt>null</tt> by default.
     * <p/>
     * Example tags include <tt>table</tt>, <tt>form</tt>, <tt>a</tt> and
     * <tt>input</tt>.
     *
     * @return this resource html tag
     */
    public String getTag() {
        return null;
    }

    /**
     * Return the resource HTML attribute with the given name, or null if the
     * attribute does not exist.
     *
     * @param name the name of link HTML attribute
     * @return the link HTML attribute
     */
     public String getAttribute(String name) {
        if (hasAttributes()) {
            return (String) getAttributes().get(name);
        }
        return null;
    }

    /**
     * Set the resource attribute with the given attribute name and value.
     *
     * @param name the attribute name
     * @param value the attribute value
     * @throws IllegalArgumentException if name parameter is null
     */
    public void setAttribute(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Null name parameter");
        }

        if (value != null) {
            getAttributes().put(name, value);
        } else {
            getAttributes().remove(name);
        }
    }

    /**
     * Return the resource attributes Map.
     *
     * @return the resource attributes Map.
     */
    public Map getAttributes() {
        if (attributes == null) {
            attributes = new HashMap();
        }
        return attributes;
    }

    /**
     * Return true if the resource has attributes or false otherwise.
     *
     * @return true if the resource has attributes on false otherwise
     */
    public boolean hasAttributes() {
        if (attributes != null) {
            return !attributes.isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Returns true if specified attribute is defined, false otherwise.
     *
     * @param name the specified attribute to check
     * @return true if name is a defined attribute
     */
    public boolean hasAttribute(String name) {
        return hasAttributes() && getAttributes().containsKey(name);
    }

    /**
     * Render the resource output to the specified buffer.
     * <p/>
     * If {@link #getTag()} returns null, this method will return an empty
     * string.
     *
     * @param buffer the specified buffer to render the resource output to
     */
    public void render(HtmlStringBuffer buffer) {
        if (getTag() == null) {
            return;
        }
        renderTagBegin(getTag(), buffer);
        renderTagEnd(getTag(), buffer);
    }

    /**
     * Return the thread local Context.
     *
     * @return the thread local Context
     */
    public Context getContext() {
        return Context.getThreadLocalContext();
    }

    public String toString() {
        if (getTag() == null) {
            return "";
        }
        HtmlStringBuffer buffer = new HtmlStringBuffer(getResourceSizeEst());
        render(buffer);
        return buffer.toString();
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Render the tag and common attributes.
     * <p/>
     * <b>Please note:</b> the tag will not be closed by this method. This
     * enables callers of this method to append extra attributes as needed.
     * <p/>
     * For example the result of calling:
     * <pre class="prettyprint">
     * Field field = new TextField("mytext");
     * HtmlStringBuffer buffer = new HtmlStringBuffer();
     * field.renderTagBegin("div", buffer);
     * </pre>
     * will be:
     * <pre class="prettyprint">
     * &lt;div name="mytext" id="mytext"
     * </pre>
     * Note that the tag is not closed.
     *
     * @param tagName the name of the tag to render
     * @param buffer the buffer to append the output to
     */
    protected void renderTagBegin(String tagName,
      HtmlStringBuffer buffer) {
        if (tagName == null) {
            throw new IllegalStateException("Tag cannot be null");
        }

        buffer.elementStart(tagName);

        appendAttributes(buffer);
    }

    /**
     * Closes the specifies tag.
     *
     * @param tagName the name of the tag to close
     * @param buffer the buffer to append the output to
     */
    protected void renderTagEnd(String tagName, HtmlStringBuffer buffer) {
        buffer.elementEnd();
    }

    /**
     * Return the estimated rendered resource size in characters.
     *
     * @return the estimated rendered resource size in characters
     */
    protected int getResourceSizeEst() {
        int size = 0;
        if (getTag() != null && hasAttributes()) {
            //length of the markup -> </> == 3
            //1 * tag.length()
            size += 3 + getTag().length();
            //using 20 as an estimate
            size += 20 * getAttributes().size();
        }
        return size;
    }

    /**
     * Append all the resource attributes to the specified buffer.
     *
     * @param buffer the specified buffer to append all the attributes
     */
    protected void appendAttributes(HtmlStringBuffer buffer) {
        if (hasAttributes()) {
            buffer.appendAttributes(attributes);
        }
    }
}
