$(function() {
	
	AdBeBack.MyMusics = function() {
		this._init();
	};
	
	AdBeBack.MyMusics.download = function(idGame){
		var input='<input type="hidden" name="idGame" value="'+ idGame +'" />';
		$('<form action="dnm/musics.zip" method="post">'+input+'</form>')
			.appendTo('body').submit().remove();
	};
	
	AdBeBack.MyMusics.prototype = {
		_init : function(){
			AdBeBack.init();
			//hover states on the static widgets
			$('.btnDl').button(
					{icons:{
						 primary: "ui-icon-extlink"
					}}
			);
			this._initControlsLeft();
		},
		
		_initControlsLeft : function(){
			
			var html = 	"<div id='acount-menu' class='block'>"+
							"<h4>Mon compte</h4>"+
							"<div class='block_content'>"+
								"<ul class='tree dynamized' style='display: block;'>"+
								"<li><a title='Mes informations' href='account.html'>Mes informations</a></li>"+
								"<li><a title='Mes musiques' href='my-musics.html'><b>Mes musiques</b></a></li>"+
								"<li><a title='Mon jeu MTzik' href='my-goose-game.html'>Mon jeu MTzik</a></li>"+
								"<li><a title='Supprimer mon compte' href='javascript:' onclick='AdBeBack.unregister();'>Supprimer mon compte</a></li>"+
								"</ul>"+
							"</div>"+
						"</div>";
			$("#left_column").append(html);
			
		}
	};
	
    $(document).ready(new AdBeBack.MyMusics());
	
});
