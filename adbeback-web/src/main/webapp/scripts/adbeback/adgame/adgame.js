$(function() {
	
	AdBeBack.AdGame = function() {
		this._init();
	};

	AdBeBack.AdGame.prototype = {
		_init : function(){
			AdBeBack.init();
            var flashvars = {idSession : AdBeBack.sessionId};
            var params = {};
            params.quality = "high";
            params.bgcolor = "#000000";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "AdGame";
            attributes.name = "AdGame";
            attributes.align = "middle";
            attributes.wmode="transparent";
            attributes.salign = "TL"; 
            menu = "false"; 
            swfobject.embedSWF(
                "swf/AdGame.swf", "flashContent", 
                "860", "600", 
                AdBeBack.Flash.swfVersionStr, AdBeBack.Flash.xiSwfUrlStr, 
                flashvars, params, attributes);
			swfobject.createCSS("#flashContent", "display:block;text-align:left;");
		}
	};
	
    $(document).ready(new AdBeBack.AdGame());
	
});
