$(function() {
	var thisObject;
	var selectedCategory=-1;
	
	var html = 			"<div class='tracklist'>"+
							"<table cellspacing='0' cellpadding='5' border='0' width='100%' class='tracks'>"+
							  	 "<tbody>"+
							  	 "__inner__"+
								"</tbody>"+
							"</table>"+
						"</div>"	;
	var html_line = 	"<tr class='line__n__'>"+
							"<td>"+
						 		"<a title='Ecouter un extrait' onclick='AdBeBack.ListenMusic(__id__,$(this));' href='javascript:' class='my-icon ui-state-default ui-corner-all'><span class='ui-icon  ui-icon-play'>Ecouter un extrait</span></a>"+
						 	"</td>"+
						 	"<td class='expand'>"+
						 	"<a href='product/__title_____id__.html'>__title__</a>"+
						     "</td>"+
						     "<td></td>"+
						     "<td>__duration__</td>"+
						     "<td></td>"+
							 "<td>"+
							 	"<a title='Ajouter au panier' href='javascript:' onclick='javascript:AdBeBack.AddToCart(__id__,$(this));' class='my-icon ui-state-default ui-corner-all'><span class='ui-icon ui-icon-plusthick'>Ajouter au panier</span></a>"+
							 "</td>"+
						"</tr>";
	var html_categories = 	"<div class='block' id='categories_block_left'>"+
								"<h4>Catégories</h4>"+
								"<div class='block_content'>"+
									"<ul class='tree dynamized' style='display: block;'>"+
										"__inner__"+
									"</ul>"+
								"</div>"+
							"</div>";
	var html_cat_line = "<li><a title='' href='javascript:' onclick='AdBeBack.BestDownload.searchCategory(this,__id__)' style='__style__'>__title__</a></li>";
	
	AdBeBack.BestDownload = function() {
		this._init();
	};

	AdBeBack.BestDownload.ListenMusic = function(idMedia){
		AdBeBack.ListenMusic(idMedia);
	};
	
	AdBeBack.BestDownload.searchCategory = function(event,cat){
		selectedCategory = cat;
		
		$("#categories_block_left div ul li a").removeAttr("style");
		$(event).attr({
				  style: 'font-size:15px;'
				});
		thisObject._search({
			search: '',
			idGenre: selectedCategory
		});
		
		
	},

	AdBeBack.BestDownload.prototype = {
		_init : function(){
			thisObject = this;
			AdBeBack.InitMp3Player();
			thisObject._search({
				search:'',
				idGenre: selectedCategory
			});
			thisObject._drawCategories();
		},
		
		_search : function(obj){
			jQuery.ajax({
		        'type': 'POST',
		        'url': "json/best-download/searchMusic.json",
		        'data': "idGenre="+obj.idGenre,
		        'success': thisObject._drawResult
		    });
		},
		
		_drawResult : function(datas){
			var global ="";
			for(var i=0;i<datas.length;i++){
				var hmusic = html_line.split("__n__").join();
				hmusic = hmusic.split("__id__").join(datas[i].id);
				hmusic = hmusic.split("__title__").join(datas[i].title);
				hmusic = hmusic.split("__duration__").join(((datas[i].duration/60<10)?"0":"")+parseInt(datas[i].duration/60)+":"+((datas[i].duration%60<10)?"0":"")+datas[i].duration%60);
				global+=hmusic;
			}
			$("#result").html(html.split("__inner__").join(global));
			$('.my-icon').hover(
					function() { $(this).addClass('ui-state-hover'); }, 
					function() { $(this).removeClass('ui-state-hover'); }
			);
		},
		
		_drawCategories : function(cat){
			$.postJSON("json/home/getCategories.json",null, function(datas) {
				var global ="";
				for(var i=0;i<datas.length;i++){

					var hcat = html_cat_line.split("__id__").join(datas[i].id);
					hcat = hcat.split("__title__").join(datas[i].title);
					if(cat==datas[i].id){
						hcat = hcat.split("__style__").join("font-size:15px;");
					}else{
						hcat = hcat.split("__style__").join("");
					}
					global+=hcat;
				}
				
				var allCat = html_cat_line.split("__id__").join(-1);
				allCat = allCat.split("__title__").join("Toutes les catégories");
				global+=allCat;
				
				$("#left_column").append($(html_categories.split("__inner__").join(global)));
			});
		}
	};
	
    $(document).ready(new AdBeBack.BestDownload());
	
});
