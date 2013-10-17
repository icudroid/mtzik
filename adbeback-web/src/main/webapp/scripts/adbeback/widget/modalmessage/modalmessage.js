(function( $ ) {
	
	
	$.modal = function ( options) {
		new modal(options);
	};
	
	modal = function ( options) {
		this._init(options);
	};
	
	var thisObject;
	
	modal.prototype = {
		overlay : null,
		message : null,
			
	
		_init : function(options){
			thisObject = this;
			thisObject.overlay = $('<div></div>').addClass('ui-widget-overlay')
			.appendTo(document.body)
			.css({
				width: this.width(),
				height: this.height()
			});
			thisObject.overlay.css('z-index',1000);
			
			thisObject.message = $('<div><p>'+options.message+'</p></div>').addClass('ui-state-highlight ui-corner-all')
			.appendTo(document.body)
			.css({
				position:'absolute',
				top:options.position.top,
				left:options.position.left,
				padding:10
			});
			thisObject.message.css('z-index',1001);
			
			setTimeout(thisObject.destroy, options.delay);
		},
		
		destroy: function() {
			thisObject.message.remove();
			thisObject.overlay.remove();
		},
		
		height: function() {
			var scrollHeight,
				offsetHeight;
			// handle IE 6
			if ($.browser.msie && $.browser.version < 7) {
				scrollHeight = Math.max(
					document.documentElement.scrollHeight,
					document.body.scrollHeight
				);
				offsetHeight = Math.max(
					document.documentElement.offsetHeight,
					document.body.offsetHeight
				);

				if (scrollHeight < offsetHeight) {
					return $(window).height() + 'px';
				} else {
					return scrollHeight + 'px';
				}
			// handle "good" browsers
			} else {
				return $(document).height() + 'px';
			}
		},

		width: function() {
			var scrollWidth,
				offsetWidth;
			// handle IE 6
			if ($.browser.msie && $.browser.version < 7) {
				scrollWidth = Math.max(
					document.documentElement.scrollWidth,
					document.body.scrollWidth
				);
				offsetWidth = Math.max(
					document.documentElement.offsetWidth,
					document.body.offsetWidth
				);

				if (scrollWidth < offsetWidth) {
					return $(window).width() + 'px';
				} else {
					return scrollWidth + 'px';
				}
			// handle "good" browsers
			} else {
				return $(document).width() + 'px';
			}
		}
	};
	


	
}(jQuery));
