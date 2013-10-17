AdBeBack.fillCategorie = function(){
	//categories
		var container = $("<div class='block' id='categories_block_left'></div>");
		$("#left_column").append(container);
		var html_categories = 	"<h4>Catégories</h4>"+
				"<div class='block_content'>"+
					"<ul class='tree dynamized' style='display: block;'>"+
						"__inner__"+
					"</ul>"+
				"</div>";
		var html_cat_line = "<li><a title='' href='<c:url value='/search.html?cat=__id__'/>'>__title__</a></li>";
		
		
		$.postJSON("<c:url value='/json/home/getCategories.json'/>",null, function(datas) {
			var global ="";
			for(var i=0;i<datas.length;i++){

				var hcat = html_cat_line.split("__id__").join(datas[i].id);
				hcat = hcat.split("__title__").join(datas[i].title);
				global+=hcat;
			}
			
			var allCat = html_cat_line.split("__id__").join(-1);
			allCat = allCat.split("__title__").join("<fmt:message key='global.js.all.categories'/>");
			global+=allCat;
			
			container.append($(html_categories.split("__inner__").join(global)));
		});
};

AdBeBack.fillCagnottes = function(){
	//cagnottes
	var container = $("<div id='special_block_right' class='block products_block exclusive blockspecials'></div>");
	$("#left_column").append(container);
	$.postJSON("<c:url value='/json/home/getCagnottes.json'/>",null, function(datas) {
		var htmlCats = "";
		//htmlCats +="<div id='special_block_right' class='block products_block exclusive blockspecials'>";
		htmlCats +="<h4>Jeu MTzik</h4>";
		
		for(var i=0;i<datas.length;i++){
			htmlCats +="<div class='block_content'>";
			htmlCats +="<div>";
			htmlCats +="<h5>Niveau "+datas[i].level+" : <span class='price'>"+datas[i].value+" &euro;</span></h5>";
			htmlCats +="</div>";
			htmlCats +="</div>";
		}
		htmlCats +="</div>";
		//htmlCats +="</div>";
		
		container.append($(htmlCats));
	});
};

AdBeBack.playing =false;
AdBeBack.readyToPlay = false;

AdBeBack.Flash = {};
AdBeBack.Flash.swfVersionStr = "10.0.0";
AdBeBack.Flash.xiSwfUrlStr = "playerProductInstall.swf";


AdBeBack.SoundAppEndPlay = function(){
	$(".ui-icon-pause").addClass('ui-icon-play').removeClass('ui-icon-pause');
};

AdBeBack.SoundAppReady = function(){
	AdBeBack.readyToPlay = true;
};

AdBeBack.ListenMusic = function(idMedia,elt){
	
	if(AdBeBack.readyToPlay ==true){
    	if(elt.children('span:first-child').hasClass('ui-icon-pause')){
    		elt.children('span:first-child').removeClass('ui-icon-pause').addClass('ui-icon-play');
    		document.getElementById("mp3Player").stop();
    	}else{
    		$(".ui-icon-pause").addClass('ui-icon-play').removeClass('ui-icon-pause');
    		elt.children('span:first-child').addClass('ui-icon-pause');
    		document.getElementById("mp3Player").play(idMedia);
    	}
	}
};



AdBeBack.htmlRedrawCart = 	"<div id='basketProducts' style='display: block;'>"+
								"Votre panier contient <strong>__nbMedia__ <fmt:message key='global.js.items'/></strong><br><br>"+
								"<table cellspacing='0' cellpadding='10' border='0' class='tracks'>"+
									"<tbody>__inner__</tbody>"+
								"</table>"+
							"</div>"+
							"<div id='basketPager'></div>"+
							"<div id='basketMSummary'>"+
								"<div class='total'>"+
									"<div class='inner'>"+
										"<div class='imaged tobepaid'><fmt:message key='global.js.total.ads'/>:</div>"+
										"<div class='amount'>__total__&nbsp;<fmt:message key='global.js.ads'/></div>"+
									"</div>"+
								"</div>"+
							"</div>";

AdBeBack.htmlCart = "<div id='basketContentList'>"+AdBeBack.htmlRedrawCart+"</div>";


AdBeBack.htmlCartLine  = "<tr class='line__n__'>"+
							"<td class='expand'><strong>__title__</strong></td>"+
							"<td><a title='<fmt:message key='global.js.cart.del'/>' href='javascript:' onclick='AdBeBack.RemoveFromCart(__id__)'  class='my-icon ui-state-default ui-corner-all'><span class='ui-icon  ui-icon-trash'><fmt:message key='global.js.cart.del'/></span></a></td>"+
							"<td class='devise'>__ads__&nbsp;<fmt:message key='global.js.ads'/></td>"+
						"</tr>";

AdBeBack.ShowCart = function(){
	jQuery.ajax({
        'type': 'POST',
        'url': "<c:url value='/json/cart/getCart.json'/>",
        'success': function(data){
			if ($("#dialog")) {
				$("#dialog").remove();
			}
			$("body").append("<div id='dialog' />");
			$( "#dialog" ).dialog({
				autoOpen: false,
				show: "blind",
				hide: "blind",
				title: "<fmt:message key='global.js.cart'/>",
				position:['center',100],
				minWidth:600,
				minHeight:300,
				modal: true,
				resizable: false,
				draggable:false,
				closeText: 'fermer',
				buttons: { 
					"<fmt:message key='global.js.cart.close'/>": function() { $(this).dialog("close"); },
							"Valider": function() { 
									if(data.nbProduct>0){
										$(this).dialog("close");
										window.location="<c:url value='/adgame.html'/>";
									}
								}
			
				}
			});
			
			var global ="";
			for(var i=0;i<data.lines.length;i++){
				var hcart = AdBeBack.htmlCartLine.split("__n__").join((i%2)+1);
				hcart = hcart.split("__id__").join(data.lines[i].idMedia);
				hcart = hcart.split("__title__").join(data.lines[i].title);
				hcart = hcart.split("__ads__").join(data.lines[i].adNeeded);
				global+=hcart;
			}
			
			var innerDlg = AdBeBack.htmlCart.split("__inner__").join(global);
			innerDlg = innerDlg.split("__nbMedia__").join(data.nbProduct);
			innerDlg = innerDlg.split("__total__").join(data.minScore);
			
			
			$("#dialog").html(innerDlg);
			$('.my-icon').hover(
					function() { $(this).addClass('ui-state-hover'); }, 
					function() { $(this).removeClass('ui-state-hover'); }
			);
			
			$( "#dialog" ).dialog( "open" );
        }
    });
};

AdBeBack.AddToCartOk = function(data,elt){
	if(data.nbProduct==0){
		$("#shopping_cart_nb_product").html('(<fmt:message key="global.js.cart.empty"/>)');
	}else{
		$("#shopping_cart_nb_product").html(data.nbProduct+"&nbsp;<fmt:message key='default.cart.musics'/>");
		if(elt){
			$.modal({
				message:data.error,
				position:{left:elt.offset().left,top:elt.offset().top-elt.height()-20},
				delay:2000
			});
		}
	}
};

AdBeBack.DrawCart = function(data){
	var global ="";
	for(var i=0;i<data.lines.length;i++){
		var hcart = AdBeBack.htmlCartLine.split("__n__").join((i%2)+1);
		hcart = hcart.split("__id__").join(data.lines[i].idMedia);
		hcart = hcart.split("__title__").join(data.lines[i].title);
		hcart = hcart.split("__ads__").join(data.lines[i].adNeeded);
		global+=hcart;
	}
	
	var innerDlg =AdBeBack.htmlRedrawCart.split("__inner__").join(global);
	innerDlg = innerDlg.split("__nbMedia__").join(data.nbProduct);
	innerDlg = innerDlg.split("__total__").join(data.minScore);
	
	$("#dialog").html(innerDlg);
	$('.my-icon').hover(
			function() { $(this).addClass('ui-state-hover'); }, 
			function() { $(this).removeClass('ui-state-hover'); }
	);
	AdBeBack.AddToCartOk(data);
};

AdBeBack.RemoveFromCart = function(idMedia){
	jQuery.ajax({
        'type': 'POST',
        'url': "<c:url value='/json/cart/remove.json'/>",
        'data': "idMedia="+idMedia,
        'success': AdBeBack.DrawCart
    });
};

AdBeBack.AddToCart = function(idMedia,elt){
	jQuery.ajax({
        'type': 'POST',
        'url': "<c:url value='/json/cart/add.json'/>",
        'data': "idMedia="+idMedia,
        'success': function(data){
        	AdBeBack.AddToCartOk(data,elt);
        }
    });
	
};

AdBeBack.InitMp3Player = function(){
    var flashvars = {idSession : AdBeBack.sessionId};
    var params = {};
    params.allowscriptaccess = "sameDomain";
    var attributes = {};
    attributes.id = "mp3Player";
    attributes.name = "mp3Player";
    swfobject.embedSWF(
        "<c:url value='/swf/Mp3Player.swf'/>", "mp3PlayerContainer", 
        "0", "0", 
        AdBeBack.Flash.swfVersionStr, AdBeBack.Flash.xiSwfUrlStr, 
        flashvars, params, attributes);
};


AdBeBack.initAutoComplete = function(){
	$( "#search_query" ).autocomplete({
		source: function( request, response ) {
			$.postJSON("<c:url value='/json/search/searchMusicAutoComplete.json'/>",{
				search: $('#search_query').val(),
				genreId: -1
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
			 $('#search_query').val(ui.item.value);
			document.getElementById('searchbox').submit();
		},
		open: function() {
			$( this ).removeClass( "ui-corner-all" ).addClass( "ui-corner-top" );
		},
		close: function() {
			$( this ).removeClass( "ui-corner-top" ).addClass( "ui-corner-all" );
		}
	});
};

AdBeBack.init = function()  {
	AdBeBack.InitMp3Player();
	AdBeBack.initAutoComplete();
};


/* This function is used to set cookies */
AdBeBack.setCookie = function(name,value,expires,path,domain,secure) {
  document.cookie = name + "=" + escape (value) +
    ((expires) ? "; expires=" + expires.toGMTString() : "") +
    ((path) ? "; path=" + path : "") +
    ((domain) ? "; domain=" + domain : "") + ((secure) ? "; secure" : "");
};

/* This function is used to get cookies */
AdBeBack.getCookie= function (name) {
    var prefix = name + "=" ;
    var start = document.cookie.indexOf(prefix) ;

    if (start==-1) {
        return null;
    }
    
    var end = document.cookie.indexOf(";", start+prefix.length) ;
    if (end==-1) {
        end=document.cookie.length;
    }

    var value=document.cookie.substring(start+prefix.length, end) ;
    return unescape(value);
};

/* This function is used to delete cookies */
AdBeBack.deleteCookie =  function(name,path,domain) {
  if (getCookie(name)) {
    document.cookie = name + "=" +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      "; expires=Thu, 01-Jan-70 00:00:01 GMT";
  }
};


//This function is used by the login screen to validate user/pass
//are entered. 
AdBeBack.validateRequired = function (form) {                                    
 var bValid = true;
 var focusField = null;
 var i = 0;                                                                                          
 var fields = new Array();                                                                           
 oRequired = new required();                                                                         
                                                                                                     
 for (x in oRequired) {                                                                              
     if ((form[oRequired[x][0]].type == 'text' || form[oRequired[x][0]].type == 'textarea' || form[oRequired[x][0]].type == 'select-one' || form[oRequired[x][0]].type == 'radio' || form[oRequired[x][0]].type == 'password') && form[oRequired[x][0]].value == '') {
        if (i == 0)
           focusField = form[oRequired[x][0]]; 
           
        fields[i++] = oRequired[x][1];
         
        bValid = false;                                                                             
     }                                                                                               
 }                                                                                                   
                                                                                                    
 if (fields.length > 0) {
    focusField.focus();
    alert(fields.join('\n'));                                                                      
 }                                                                                                   
                                                                                                    
 return bValid;                                                                                      
};

AdBeBack.isUndefined = function(value) {   
    var undef;   
    return value == undef; 
}

AdBeBack.unregister = function(){
	
	var elt = $("<div><fmt:message key='global.js.del.account'/></div>");
	$("body").append(elt);
	elt.dialog({
		resizable: false,
		height:140,
		modal: true,
		title:'Suppression de votre compte',
		close: function(event, ui) { 
			$( this ).remove();
		},
		buttons: {
			"Oui": function() {
				jQuery.ajax({
			        'type': 'GET',
			        'url': "<c:url value='/json/account/delete.json'/>",
			        'success': function(){
			        	alert("<fmt:message key='global.js.deleted.account'/>");
			        	window.location = "<c:url value='/home.html'/>";
			        }
			    });
			},
			"Non": function() {
				$( this ).dialog( "close" ).remove();
			}
		}
	});

};



AdBeBack.urlEncodeCharacter = function(c){
	return '%' + c.charCodeAt(0).toString(16);
};

AdBeBack.urlDecodeCharacter = function(str, c){
	return String.fromCharCode(parseInt(c, 16));
};

AdBeBack.urlEncode = function( s ){
      return encodeURIComponent( s ).replace( /\%20/g, '+' ).replace( /[!'()*~]/g, AdBeBack.urlEncodeCharacter );
};

AdBeBack.urlDecode = function( s ){
      return decodeURIComponent(s.replace( /\+/g, '%20' )).replace( /\%([0-9a-f]{2})/g, AdBeBack.urlDecodeCharacter);
};
