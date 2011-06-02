$(function() {
	AdBeBack.Account = function() {
		this._init();
	};

	AdBeBack.Account.prototype = {
		_init : function(){
			AdBeBack.init();
			this._initControlsLeft();
			$( "#btnValidate" ).button();

		},
		
		_initControlsLeft : function(){
			
			var html = 	"<div  id='acount-menu' class='block'>"+
							"<h4>Mon compte</h4>"+
							"<div class='block_content'>"+
								"<ul class='tree dynamized' style='display: block;'>"+
								"<li><a title='Mes informations' href='account.html'><b>Mes informations</b></a></li>"+
								"<li><a title='Mes musiques' href='my-musics.html'>Mes musiques</a></li>"+
								"<li><a title='Mon jeu MTzik' href='my-goose-game.html'>Mon jeu MTzik</a></li>"+
								"<li><a title='Mon gains' href='my-win-game.html'>Mes gains</a></li>"+
								"<li><a title='Supprimer mon compte' href='javascript:' onclick='AdBeBack.unregister();'>Supprimer mon compte</a></li>"+
								"</ul>"+
							"</div>"+
						"</div>";
			$("#left_column").append(html);
			
		}
	};
	
    $(document).ready(new AdBeBack.Account());
});