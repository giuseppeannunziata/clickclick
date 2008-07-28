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

import net.sf.click.util.HtmlStringBuffer;

/**
 *
 * @author Bob Schellink
 */
public class CssImport extends AbstractResource {

    public CssImport() {
        this(null);
    }

    public CssImport(String source) {
        this(source, false);
    }

    public CssImport(String source, boolean prefixWithContextPath) {
        if (source != null) {
            if (prefixWithContextPath) {
                HtmlStringBuffer sourceBuffer = new HtmlStringBuffer(source.length());
                // Append the context path
                sourceBuffer.append(getContext().getRequest().getContextPath());
                sourceBuffer.append(source);
                setHref(sourceBuffer.toString());
            } else {
                setHref(source);
            }
        }
        setAttribute("type", "text/css");
        setAttribute("rel", "stylesheet");
    }

    public String getTag() {
        return "link";
    }

    public void setHref(String href) {
        setAttribute("href", href);
    }

    public String getHref() {
        return getAttribute("href");
    }

    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart(getTag());
        appendAttributes(buffer);
        buffer.elementEnd();
    }
}
