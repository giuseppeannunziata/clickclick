jQuery.noConflict();

jQuery(document).ready(function(){
    jQuery('#$source').click(function() {
       
        var params = jQuery.query(this.href);
        var path = this.href.substring(0, this.href.indexOf('?'));

        jQuery.ajax({
            type: "GET",
            url: path,
            dataType: "$dataType",
            data: params,
            success: function (data, textStatus) {
                if ('$dataType' != 'script') {
                    if (data) {
                        jQuery('#$target').html(data);
                    } else {
                        alert('Server returned empty response!');
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert('$errorMessage');
            },
            beforeSend: function() {
                #if($showBusyIndicator)
                  #if($busyIndicatorMessage)
                    jQuery.blockUI({ message: $busyIndicatorMessage });
                  #else
                     jQuery.blockUI();
                  #end
                #end
            },
            complete: function() {
                #if($showBusyIndicator)
                  jQuery.unblockUI();
                #end
            }
        });
        return false;
    })
    jQuery.query = function(s) {
        var r = {};
        if (s) {
            var q = s.substring(s.indexOf('?') + 1);
            jQuery.each(q.split('&'), function() {
                var splitted = this.split('=');
                var key = splitted[0];
                var val = splitted[1];
                // ignore empty values
                if (typeof val == 'number' || typeof val == 'boolean' || val.length > 0) {
                    r[key] = val;
                }
            });
        }
        return r;
    };
})
