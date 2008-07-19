jQuery.noConflict();
// This document was adapted from the official JQuery Form plugin demo:
// http://www.malsup.com/jquery/form/#code-samples
// 
// prepare the form when the DOM is ready 
jQuery(document).ready(function() { 
    var options = {
        target:        '#response',   // target element(s) to be updated with server response 
        beforeSubmit:  showRequest,  // pre-submit callback 
        success:       showResponse  // post-submit callback 
 
        // other available options: 
        //url:       url         // override for form's 'action' attribute 
        //type:      type        // 'get' or 'post', override for form's 'method' attribute 
        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
        //clearForm: true        // clear all form fields after successful submit 
        //resetForm: true        // reset the form after successful submit 
 
        // jQuery.ajax options can be used here too, for example: 
        //timeout:   3000 
    };

    jQuery('#form').ajaxForm(options); 
});

// pre-submit callback 
function showRequest(formData, jqForm, options) {
    // Clear the response content before Ajax request
    jQuery("#response").html("");

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
 
// post-submit callback 
function showResponse(responseText, statusText)  { 
    // for normal html responses, the first argument to the success callback 
    // is the XMLHttpRequest object's responseText property 
 
    // if the ajaxSubmit method was passed an Options Object with the dataType 
    // property set to 'xml' then the first argument to the success callback 
    // is the XMLHttpRequest object's responseXML property 
 
    // if the ajaxSubmit method was passed an Options Object with the dataType 
    // property set to 'json' then the first argument to the success callback 
    // is the json data object returned by the server 
} 
