$(function() {
	AdBeBack.Contact = function() {
		this._init();
	};

	AdBeBack.Contact.prototype = {
		_init : function(){
			AdBeBack.init();
			$( "#submitMessage" ).button();
		}
	};
	
    $(document).ready(new AdBeBack.Contact());
});