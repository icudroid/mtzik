$(function() {
	
	var slider;
	
	AdBeBack.Home = function() {
		this._init();
	};

	AdBeBack.Home.prototype = {
		_init : function(){
			$("#sliderholder").removeClass("hidden");
			$("#slider").width("720px");
			var buttons = { previous:$('#slider .lof-previous') , next:$('#slider .lof-next') };

			AdBeBack.fillCategorie();
			AdBeBack.fillCagnottes();
			
			//obtenir les nouveautes
			$.postJSON("json/home/getHomeMedias.json",null, function(datas) {
				//generation				
				var htmlNav ="";
				var html ="";
				for(var i=0;i<datas.length;i++){
					htmlNav+="<li><img src='img/thumbs/"+datas[i].thumbJacket+"'/></li>";
					html+="<li>";
						html+="<img src='img/"+datas[i].jacket+"' title='"+datas[i].title+"' >";           
						html+="<div class='lof-main-item-desc'>";
							html+="<h3>" 
								+ "<a id='slidePlay_"+datas[i].id+"' onclick='AdBeBack.ListenMusic("+datas[i].id+",$(this))'><fmt:message key='global.listen'/></a>"
								+ "&nbsp;"
								+ "<a id='slideAdd_"+datas[i].id+"' onclick='AdBeBack.AddToCart("+datas[i].id+",$(this))'><fmt:message key='default.cart.add'/></a>"
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

				
				slider  = $('#slider').lofJSidernews( { interval : 4000,
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
			
		}
	};
	
    $(document).ready(new AdBeBack.Home());
	
});
