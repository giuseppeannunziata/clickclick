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

/**
 *
 * @author Bob Schellink
 */
public class JavascriptImport extends AbstractResource {

    public static final int HEAD = 0;
    public static final int BODY = 1;

    private int position = BODY;

    public JavascriptImport() {
        this(null, BODY);
    }

    public JavascriptImport(String source) {
        this(source, BODY);
    }

    public JavascriptImport(String source, int position) {
        this(source, position, false);
    }

    public JavascriptImport(String source, boolean prefixWithContextPath) {
        this(source, BODY, false);
    }

    public JavascriptImport(String source, int position, boolean prefixWithContextPath) {
        if (source != null) {
            if (prefixWithContextPath) {
                HtmlStringBuffer sourceBuffer = new HtmlStringBuffer(source.length());
                // Append the context path
                sourceBuffer.append(getContext().getRequest().getContextPath());
                sourceBuffer.append(source);
                setSource(sourceBuffer.toString());
            } else {
                setSource(source);
            }
        }
        this.position = position;
        setAttribute("type", "text/javascript");
    }

    public String getTag() {
        return "script";
    }

    public void setSource(String source) {
        setAttribute("src", source);
    }

    public String getSource() {
        return getAttribute("src");
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void render(HtmlStringBuffer buffer) {
        buffer.elementStart(getTag());
        appendAttributes(buffer);
        buffer.closeTag();
        
        buffer.elementEnd(getTag());
    }
}
