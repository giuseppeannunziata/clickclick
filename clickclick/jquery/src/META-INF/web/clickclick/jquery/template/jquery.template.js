jQuery(document).ready(function(){

  // This template uses LiveQuery to bind events, meaning you don't have to
  // rebind the event if the Control is replaced since LiveQuery does this for you.
  // Also note the method Click.debounce which merge multiple callbacks if they
  // are invoked too many times within a specified threshold
  jQuery('$selector').livequery(#if($event) "$event", #end Click.debounce(function() {
    // Extract parameters from link href

    // Retrieve the underlying d
    var target = jQuery('$selector')[0];
    var params = Click.params(target);

    // Add any parameters passed in from the Page
    var defaultParams = Click.urlPairs('$!{parameters}');
    if (Click.isNotBlank(defaultParams)) {
        params.push(defaultParams);
    }

    // Add the Control attributes 'name', 'value' and 'id' as parameters
    Click.addNameValueIdPairs(target, params);

    // Invoke the Ajax request
    jQuery.ajax({
      type: '$!{type}',
      url: '$!{url}',
      data: params,
      beforeSend: function() {
        // Show request indicator if enabled
        #if($showIndicator == "true")
          #if($indicatorTarget) jQuery('$indicatorTarget').block({ message: '$indicatorMessage' });
          #else jQuery.blockUI({ message: '$indicatorMessage' });
          #end
        #end
      },
      complete: function() {
        // Hide request indicator if enabled
        #if($showIndicator == "true")
          #if($indicatorTarget) jQuery('$indicatorTarget').unblock();
          #else jQuery.unblockUI();
          #end
        #end
      },
      error: function (xhr, textStatus, errorThrown) {
        alert('$errorMessage' #if ($productionMode != "true") + '\n\n' + xhr.responseText #end);
      }
    });
    // Return false to override default event behavior
    return false;
  }, $threshold));
  // $threshold is the number of milliseconds within which multiple function
  // callbacks will be merged into one function callback
});
