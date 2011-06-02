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
							 	"<a title='Ajouter au panier' href='javascript:' onclick='AdBeBack.AddToCart(__id__,$(this));' class='my-icon ui-state-default ui-corner-all'><span class='ui-icon ui-icon-plusthick'>Ajouter au panier</span></a>"+
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
	var html_cat_line = "<li><a title='' href='javascript:' onclick='AdBeBack.NewProducts.searchCategory(this,__id__)' style='__style__'>__title__</a></li>";
	
	AdBeBack.NewProducts = function() {
		this._init();
	};

	AdBeBack.NewProducts.ListenMusic = function(idMedia){
		AdBeBack.ListenMusic(idMedia);
	};
	
	AdBeBack.NewProducts.searchCategory = function(event,cat){
		selectedCategory = cat;
		
		$("#categories_block_left div ul li a").removeAttr("style");
		$(event).attr({
				  style: 'font-size:15px;'
				});
		thisObject._search({
			search: '',
			genreId: selectedCategory
		});
		
		
	},

	AdBeBack.NewProducts.prototype = {
		_init : function(){
			AdBeBack.init();
			thisObject = this;
			thisObject._search({
				search:'',
				genreId: selectedCategory
			});
			thisObject._drawCategories();
			$("#sliderholder").removeClass("hidden");
			$("#slider").width("720px");
			var buttons = { previous:$('#slider .lof-previous') , next:$('#slider .lof-next') };
			
			//obtenir les nouveautes
			$.postJSON("json/new-products/getNews.json",null, function(datas) {
				//generation				
				var htmlNav ="";
				var html ="";
				for(var i=0;i<datas.length;i++){
					htmlNav+="<li><img src='img/thumbs/"+datas[i].thumbJacket+"'/></li>";
					html+="<li>";
						html+="<img src='img/"+datas[i].jacket+"' title='"+datas[i].title+"' >";           
						html+="<div class='lof-main-item-desc'>";
							html+="<h3>" 
								+ "<a id='slidePlay_"+datas[i].id+"' onclick='AdBeBack.ListenMusic("+datas[i].id+",$(this))'>Ecouter</a>"
								+ "&nbsp;"
								+ "<a id='slideAdd_"+datas[i].id+"'  onclick='AdBeBack.AddToCart("+datas[i].id+",$(this));'>Ajouter au panier</a>"
								+"</h3>";
							html+="<h2>"+datas[i].title+"</h2>";
							
							html+="<p>";
							html+="<div>Artistes : ";
							for(var ia = 0;ia<datas[i].artists.length;ia++){
								if(ia!=0)html+=", ";
								html+=datas[i].artists[ia].firstName+" "+datas[i].artists[ia].lastName;
							}
							html+="</div>";
							html+="<div>Genres : ";
							for(var ig = 0;ig<datas[i].genres.length;ig++){
								if(ig!=0)html+=", ";
								html+=datas[i].genres[ig].genre;
							}
							html+="</div>";
							html+="<div>Durée : "+((datas[i].duration/60<10)?"0":"")+parseInt(datas[i].duration/60)+":"+((datas[i].duration%60<10)?"0":"")+datas[i].duration%60;
							html+="</div>";
							html+="</p>";
						html+="</div>";
			        html+="</li>"; 
				}
				$(".lof-navigator").html(htmlNav);
				$(".lof-main-wapper").html(html);
				for(var i=0;i<datas.length;i++){
					$("#slidePlay_"+datas[i].id).button({
			            icons: {
			                primary: "ui-icon-play"
			            }});
					$("#slideAdd_"+datas[i].id).button({
			            icons: {
			                primary: "ui-icon-plusthick"
			            }});
				}

				
				var slider  = $('#slider').lofJSidernews( { interval : 4000,
					direction		: 'opacitys',	
				 	easing			: 'easeInOutExpo',
					duration		: 1200,
					auto		 	: false,
					maxItemDisplay  : 4,
					navPosition     : 'horizontal', // horizontal
					navigatorHeight : 86,
					navigatorWidth  : 86,
					mainWidth		:720,
					buttons			: buttons} );	
			});
			
			
		},
		
		_search : function(obj){
			$.postJSON("json/new-products/searchMusic.json",obj, function(datas) {
				thisObject._drawResult(datas);
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
	
    $(document).ready(new AdBeBack.NewProducts());
	
});
