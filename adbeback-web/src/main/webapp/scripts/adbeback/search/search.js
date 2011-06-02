$(function() {
	var btnSearch;
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
						 	"<a href='product/__urltitle_____id__.html'>__title__</a>"+
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
	var html_cat_line = "<li><a title='' href='javascript:' onclick='AdBeBack.Search.searchCategory(this,__id__)' style='__style__'>__title__</a></li>";
	
	AdBeBack.Search = function() {
		this._init();
	};

	AdBeBack.Search.ListenMusic = function(idMedia){
		AdBeBack.ListenMusic(idMedia);
	};
	
	AdBeBack.Search.searchCategory = function(event,cat){
		selectedCategory = cat;
		
		$("#categories_block_left div ul li a").removeAttr("style");
		$(event).attr({
				  style: 'font-size:15px;'
				});
		thisObject._search({
			search: $('#fieldSearch').val(),
			genreId: selectedCategory
		});
	},

	AdBeBack.Search.prototype = {
		_init : function(){
			AdBeBack.init();
			thisObject = this;
			var initSearch = false;
			if (typeof(initCat) != 'undefined'){
				selectedCategory = initCat;	
				initSearch = true;
				thisObject._drawCategories(initCat);
			}else{
				thisObject._drawCategories();
			}
			
			if (typeof(initStr) != 'undefined'){
				$('#fieldSearch').val(initStr);
				initSearch = true;
			}
			
			if(initSearch){
				thisObject._search({
					search: $('#fieldSearch').val(),
					genreId: selectedCategory
				});
			}
			
			
			
			btnSearch = $('#btnSearch').button();
			btnSearch.bind('click',  function(event) {
				thisObject._search({
					search: $('#fieldSearch').val(),
					genreId: selectedCategory
				});
			});

			
			$( "#fieldSearch" ).autocomplete({
				source: function( request, response ) {
					$.postJSON("json/search/searchMusicAutoComplete.json",{
						search: $('#fieldSearch').val(),
						genreId: selectedCategory
					}, function(data){
						response( $.map( data, function( item ) {
							return {
								label: item.fullName+((item.title!=null)?' | '+item.title:''),
								value: ((item.title!=null)?item.title:item.fullName)
							}
						}));
					});
				},
				minLength: 2,
				select: function( event, ui ) {
					thisObject._search({
						search: ui.item.value,
						genreId: selectedCategory
					});
				},
				open: function() {
					$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
				},
				close: function() {
					$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
				}
			});
			
			
		},
		
		_search : function(obj){
			$.postJSON("json/search/searchMusic.json",obj, function(datas) {
				thisObject._drawResult(datas);
			});
		},
		
		_drawResult : function(datas){
			var global ="";
			for(var i=0;i<datas.length;i++){
				var hmusic = html_line.split("__n__").join((i%2)+1);
				hmusic = hmusic.split("__id__").join(datas[i].id);
				hmusic = hmusic.split("__title__").join(datas[i].title);
				hmusic = hmusic.split("__urltitle__").join(AdBeBack.urlEncode(datas[i].title));
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
	
    $(document).ready(new AdBeBack.Search());
	
});
