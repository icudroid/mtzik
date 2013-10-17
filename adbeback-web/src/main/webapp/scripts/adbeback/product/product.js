$(function() {
	
	AdBeBack.Product = function() {
		this._init();
	};
	
	AdBeBack.Product.ListenMusic = function(idMedia){
		AdBeBack.ListenMusic(idMedia);
	};

	AdBeBack.Product.prototype = {
		_init : function(){
			AdBeBack.init();
			//hover states on the static widgets
			$('.my-icon').hover(
				function() { $(this).addClass('ui-state-hover'); }, 
				function() { $(this).removeClass('ui-state-hover'); }
			);
			
			AdBeBack.fillCategorie();
			AdBeBack.fillCagnottes();
		}
	};
	
    $(document).ready(new AdBeBack.Product());
	
});
