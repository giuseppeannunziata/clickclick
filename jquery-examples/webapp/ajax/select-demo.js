jQuery.noConflict();
// wait till the DOM is loaded
jQuery(document).ready(function () {

    // Attach an Ajax handler to the provinceSelect change event
    jQuery("#form_provinceSelect").change(function(){

        // If no province is selected, clear the city select and exit early
        if (jQuery("#form_provinceSelect").val().length == 0) {
            jQuery("#form_citySelect").html('');
            return;
        }
        
        // Build parameters to send to server
        var params = "provinceSelect=" + jQuery("#form_provinceSelect").val() + "&form_provinceSelect=1";
        jQuery.ajax ({
            url: "$context/ajax/select-demo.htm",
            data: params,
            dataType: "html",

            // Function to execute when Ajax event starts
            beforeSend: function(){
                // Create a div with busy indicator once Ajax request starts
                jQuery('<div id="quickalert" class="quick-alert">Loading, please wait ... <img src="$context/assets/images/indicator.gif"></img></div>')
                .insertAfter(jQuery('#form'))
                .fadeIn('slow')
            },

            // Function to execute when Ajax event stops
            complete: function() {
                // Hide the busy indicator after Ajax request finish
                jQuery("#quickalert").fadeOut('slow', function() {
                    jQuery(this).remove();
                })
            },

            // If an successful populate the citySelect options
            success: function(data){
                jQuery("#form_citySelect").html( data );
            },

            // If an error occurs show alert with error message
            error: function(xhr,err,e){
                alert( "Error: " + err );
            }
        });
        return false;
    });
});
