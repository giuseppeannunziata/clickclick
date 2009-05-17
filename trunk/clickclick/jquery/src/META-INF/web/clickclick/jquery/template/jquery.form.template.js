/**
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

// prepare the form when the DOM is ready
var formOptions;
jQuery(document).ready(function() {
    formOptions = {
        beforeSubmit:  preSubmit,   // pre-submit callback
        dataType:      'xml',   // must be 'xml' to work with JQuery Taconite support
        complete:      postSubmit   // post-submit callback

        // other available options:
        //success:       showResponse,  // post-submit callback
        //target:    null,       // target element(s) to be updated with server response
        //url:       url         // override for form's 'action' attribute
        //type:      type        // 'get' or 'post', override for form's 'method' attribute
        //clearForm: true        // clear all form fields after successful submit
        //resetForm: true        // reset the form after successful submit

        // jQuery.ajax options can be used here too, for example:
        //timeout:   3000
    };

    // bind the form submit event
    jQuery('$selector').ajaxForm(formOptions);
});

// pre-submit callback
function preSubmit(formData, jqForm, options) {
     #if($showIndicator == "true")
       #if($indicatorTarget) jQuery('$indicatorTarget').block({ message: '$indicatorMessage' });
       #else jQuery.blockUI({ message: '$indicatorMessage' });
       #end
     #end

    // Below is a short explanation of the arguments passed to the showRequest method.

    // formData is an array; here we use jQuery.param to convert it to a string to display it
    // but the form plugin does this for you automatically when it submits the data
    var queryString = jQuery.param(formData);

    // jqForm is a jQuery object encapsulating the form element.  To access the
    // DOM element for the form do this:
    // var formElement = jqForm[0];

    // here we could return false to prevent the form from being submitted;
    // returning anything other than false will allow the form submit to continue
    return true;
}

function postSubmit(xhr, statusText) {
    #if($showIndicator == "true")
      #if($indicatorTarget) jQuery('$indicatorTarget').unblock();
      #else jQuery.unblockUI();
      #end
    #end
    if (statusText == "success") {
        var data = xhr.responseText;
        onSuccess(data, statusText, xhr);
    } else {
        onError(statusText, xhr);
    }
}

function onSuccess(responseData, statusText, xhr) {
    // bind the form submit event
    jQuery('$selector').ajaxForm(formOptions);
}

function onError(statusText, xhr) {
    alert('$errorMessage' #if ($productionMode != "true") + '\n\n' + xhr.responseText #end);
}
