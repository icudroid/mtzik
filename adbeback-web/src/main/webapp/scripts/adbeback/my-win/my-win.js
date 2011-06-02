$(function() {
	var thisObject;
	
	AdBeBack.MyWin = function() {
		this._init();
	};
	
	AdBeBack.MyWin.transfert = function (id){
		
		
	};

	AdBeBack.MyWin.prototype = {
			
			_init : function(){
				
				$('.btnTransfert').button(
						{icons:{
							 primary: "ui-icon-extlink"
						}}
				);
				
				this._initControlsLeft();
			},
			
			_initControlsLeft : function(){
				
				var html = 	"<div  id='acount-menu' class='block'>"+
								"<h4>Mon compte</h4>"+
								"<div class='block_content'>"+
									"<ul class='tree dynamized' style='display: block;'>"+
									"<li><a title='Mes informations' href='account.html'>Mes informations</a></li>"+
									"<li><a title='Mes musiques' href='my-musics.html'>Mes musiques</a></li>"+
									"<li><a title='Mon jeu MTzik' href='my-goose-game.html'>Mon jeu MTzik</a></li>"+
									"<li><a title='Mon gains' href='my-win-game.html'><b>Mes gains</b></a></li>"+
									"<li><a title='Supprimer mon compte' href='javascript:' onclick='AdBeBack.unregister();'>Supprimer mon compte</a></li>"+
									"</ul>"+
								"</div>"+
							"</div>";
				$("#left_column").append(html);
				
			}
	};
	
    $(document).ready(new AdBeBack.MyWin());
	
});
