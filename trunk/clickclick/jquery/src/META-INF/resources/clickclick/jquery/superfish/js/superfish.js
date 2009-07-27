
/*
 * Superfish v1.4.5 - jQuery menu widget
 * Copyright (c) 2008 Joel Birch
 *
 * Dual licensed under the MIT and GPL licenses:
 * 	http://www.opensource.org/licenses/mit-license.php
 * 	http://www.gnu.org/licenses/gpl.html
 *
 * CHANGELOG: http://users.tpg.com.au/j_birch/plugins/superfish/changelog.txt
 */

;(function($){
	$.fn.superfish = function(op){

		var	bcClass    = 'sf-breadcrumb',
			menuClass  = 'sf-js-enabled',
			anchorClass= 'sf-with-ul',
			arrowClass = 'sf-sub-indicator',
			shadowClass= 'sf-shadow',
			$arrow     = $(['<span class="',arrowClass,'"> &#187;</span>'].join('')),

			over = function(){
				var $$ = $(this), menu = getMenu($$);
				clearTimeout(menu.sfTimer);
				$$.showSuperfishUl().siblings().hideSuperfishUl();
			},
			out = function(){
				var $$ = $(this), menu = getMenu($$), o = $.fn.superfish.op;
				clearTimeout(menu.sfTimer);
				menu.sfTimer=setTimeout(function(){
					o.retainPath=($.inArray($$[0],o.$path)>-1);
					$$.hideSuperfishUl();
					if (o.$path.length && $$.parents(['li.',o.hoverClass].join('')).length<1){over.call(o.$path);}
				},o.delay);	
			},
			getMenu = function($menu){
				var menu = $menu.parents(['ul.',menuClass,':first'].join(''))[0];
				$.fn.superfish.op = $.fn.superfish.o[menu.serial];
				return menu;
			},
			addArrow = function($a){ $a.addClass(anchorClass).append($arrow.clone()); };

		return this.each(function() {
			var s = this.serial = $.fn.superfish.o.length;
			var o = $.extend({},$.fn.superfish.defaults,op);
			o.$path = $('li.'+o.pathClass,this).slice(0,o.pathLevels).each(function(){
				$(this).addClass([o.hoverClass,bcClass].join(' '))
					.filter('li:has(ul)').removeClass(o.pathClass);
			});
			$.fn.superfish.o[s] = $.fn.superfish.op = o;
			
			$('li:has(ul)',this)[($.fn.hoverIntent && !o.disableHI) ? 'hoverIntent' : 'hover'](over,out).each(function() {
				if (o.autoArrows) addArrow( $('>a:first-child',this) );
			})
			.not('.'+bcClass)
				.hideSuperfishUl();
			
			var $a = $('a',this);
			$a.each(function(i){
				var $li = $a.eq(i).parents('li');
				$a.eq(i).focus(function(){over.call($li);}).blur(function(){out.call($li);});
			});
			if (!$.browser.msie && o.dropShadows) $(this).addClass(shadowClass);
			o.onInit.call(this);
			
		}).addClass(menuClass);
	};

	$.fn.extend({
		hideSuperfishUl : function(){
			var o = $.fn.superfish.op;
			var not = (o.retainPath===true) ? o.$path : '';
			o.retainPath = false;
			var $ul = $(['li.',o.hoverClass].join(''),this).add(this).not(not).removeClass(o.hoverClass)
					.find('>ul').hide();
			o.onHide.call($ul);
			return this;
		},
		showSuperfishUl : function(){
			var o = $.fn.superfish.op,
				$ul = this.addClass(o.hoverClass)
					.find('>ul:hidden');
			o.onBeforeShow.call($ul);
			$ul.animate(o.animation,o.speed,function(){ o.onShow.call(this); });
			return this;
		}
	});

	$.fn.superfish.o = [];
	$.fn.superfish.op = {};
	$.fn.superfish.defaults = {
		hoverClass	: 'sfHover',
		pathClass	: 'overideThisToUse',
		pathLevels	: 1,
		delay		: 800,
		animation	: {opacity:'show'},
		speed		: 'normal',
		autoArrows	: true,
		dropShadows : true,
		disableHI	: false,		// true disables hoverIntent detection
		onInit		: function(){}, // callback functions
		onBeforeShow: function(){},
		onShow		: function(){},
		onHide		: function(){}
	};

})(jQuery);
