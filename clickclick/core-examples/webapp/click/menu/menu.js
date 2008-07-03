//This scripts is for Internet Explorer which does not support css :hover. This script injects iehover

function initMenu() {
    var isIE6 = false /*@cc_on || @_jscript_version < 5.7 @*/;
    //var isIE = window.ActiveXObject ? true : false; // ActiveX is only used in Internet Explorer
    if (!isIE6)
        return;

    //Get all LI elements, having 'verticalMenu' as one of their parent elements id        
    writeMenu ("verticalMenu");
  
    //Get all LI elements, having 'horizontalMenu' as one of their parent elements id        
    writeMenu ("horizontalMenu");
}

function writeMenu (idArg) {
    var elements = document.getElementById(idArg);
    if (elements != null) {
        var list = elements.getElementsByTagName("LI");
        if (list != null) {            
            enableHover(list);
        }
    }
}

//enable hover for the list
function enableHover(list) {
    for (var i=0; i<list.length; i++) {
        list[i].onmouseover=function() {
            this.className+=" iehover";
        }
        list[i].onmouseout=function() {
            this.className=this.className.replace(new RegExp(" iehover\\b"), "");
        }
    }
}

if (window.attachEvent) window.attachEvent("onload");
