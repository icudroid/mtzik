$(function() {
	
	AdBeBack.LastWinners = function() {
		this._init();
	};


	AdBeBack.LastWinners.prototype = {
		_init : function(){
			AdBeBack.fillCategorie();
			AdBeBack.fillCagnottes();
		}
		
	};
	
    $(document).ready(new AdBeBack.LastWinners());
	
});
