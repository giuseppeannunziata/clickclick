jQuery.noConflict();
jQuery(document).ready(function(){
    jQuery('#$imageId').click(function() {
        jQuery('#$fieldId').click();
    })
    jQuery('#$fieldId').ColorPicker({
        onSubmit: function(hsb, hex, rgb) {
            jQuery('#$fieldId').val(hex);
            jQuery('#$imageId div div').css('backgroundColor', '#' + hex);
        },
        onBeforeShow: function () {
            jQuery(this).ColorPickerSetColor(this.value);
        }
    })
    .bind('keyup', function(){
        jQuery(this).ColorPickerSetColor(this.value);
    });
});
