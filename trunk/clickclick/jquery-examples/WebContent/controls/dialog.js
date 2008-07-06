jQuery.noConflict();
jQuery(document).ready(function(){
    jQuery('#table-id th').click(function(event) {
      var target = jQuery(event.target);
      var offset = target.offset();
      jQuery('#dialog').css({"display":"block"});
      jQuery('#dialog').dialog("close");
      jQuery('#dialog').dialog({position: [offset.left + target.width(), offset.top]});
    });
});