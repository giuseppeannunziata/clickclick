/*
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
package net.sf.clickclick.examples.jquery.page.basic;

import java.util.List;
import net.sf.clickclick.examples.jquery.page.BorderPage;
import net.sf.clickclick.jquery.helper.JQHelper;
import org.apache.click.element.CssImport;
import org.apache.click.element.JsImport;

public class EffectsPage extends BorderPage {

    public String title = "Basic";

    public List getHeadElements() {
        if (headElements == null) {
            headElements = super.getHeadElements();

            // Import jquery.js, basic.js and basic.css
            headElements.add(new JsImport(JQHelper.jqueryImport));
            headElements.add(new JsImport("/basic/effects.js"));
            headElements.add(new CssImport("/basic/effects.css"));
        }
        return headElements;
    }
}
