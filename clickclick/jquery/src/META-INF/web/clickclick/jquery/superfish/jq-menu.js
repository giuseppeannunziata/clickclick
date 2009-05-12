jQuery(document).ready(function(){
    jQuery('ul.sf-menu')
    .superfish({$options})
    .find('>li:has(ul)')
    .mouseover(function(){
        jQuery('ul', this).bgIframe({opacity:false});
    })
    .find('a')
    .focus(function(){
        jQuery('ul', jQuery('.nav>li:has(ul)')).bgIframe({opacity:false});
    });
});
