jQuery.noConflict();
// This document was adapted from the official JQuery Form plugin demo:
// http://www.malsup.com/jquery/form/#code-samples
// 
// prepare the form when the DOM is ready 
var formOptions;
jQuery(document).ready(function() { 
    formOptions = {
        beforeSubmit:  preSubmit,   // pre-submit callback
        dataType:      '$dataType',   // 'xml', 'script', or 'json' (expected server response type)  
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

    jQuery('#$formId').ajaxForm(formOptions);
});

// pre-submit callback
function preSubmit(formData, jqForm, options) {
    jQuery.blockUI();

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
    jQuery.unblockUI();
    if (statusText == "success") {
        var data = xhr.responseText;
        // TODO should we support xml? What to do with result?
        if ('$dataType' == "xml") {
            data = xhr.responseXML;
        }
        if ('$dataType' == "script") {
            // do nothing, JQuery will evaluate it automatically
        } else {
            onSuccess(data, statusText, xhr);
        }
    } else {
        onError(statusText, xhr);
    }
}

function onSuccess(responseData, statusText, xhr) {
    var ajaxTargetId = xhr.getResponseHeader("Click.ajaxTargetId");
    var replace = xhr.getResponseHeader("Click.replace");
    var focusId = xhr.getResponseHeader("Click.focusId");
    var redirectUrl = xhr.getResponseHeader("Click.redirectUrl");

    // Check for redirect
    if (typeof(redirectUrl) != "undefined" && redirectUrl != null && redirectUrl != "") {
        alert(redirectUrl)
        window.location = redirectUrl;
    } else {
        cleanup();
        // Otherwise a normal Ajax response
        if (!ajaxTargetId) {
            // Default ajaxTargetId to targetId if targetId was set
            if ($targetId) {
                ajaxTargetId = $targetId;
            }
        }
        
        // Execute if target was set
        if (ajaxTargetId) {
            if (replace == "true") {
                jQuery(ajaxTargetId).replaceWith(responseData);
            } else {
                jQuery("#"+ajaxTargetId).html(responseData);
            }
        }
        
        if (focusId) {
            jQuery("#"+focusId).focus();
        }
    }

    jQuery('#$formId').ajaxForm(formOptions);
}

function cleanup() {
    // Clear the response content before Ajax request
    if ($clearTarget) {
        jQuery("#$targetId").html("");
    }

    // Clear all errors
    jQuery("#${formId}-errors").remove();
    jQuery("#${formId} .error").removeClass("error");
}

function onError(statusText, xhr) {
    alert("Could not handle request: " + statusText)
}
