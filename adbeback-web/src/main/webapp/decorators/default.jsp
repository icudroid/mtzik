<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <%@ include file="/common/meta.jsp" %>
        <title><decorator:title/> | <fmt:message key="webapp.name"/></title>

        <script type="text/javascript">
        	var AdBeBack = {};
        	AdBeBack.locale = "<%=request.getLocale()%>";
        	AdBeBack.sessionId = "<%=request.getSession().getId()%>";
        	AdBeBack.context="${ctx}";
        </script>


        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/${appConfig["csstheme"]}/print.css'/>" />
		
		
		<script type="text/javascript" src="<c:url value='/scripts/swfobject.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/jquery-1.5.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/jquery-ui-1.8.10.custom.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/json.min.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/adbeback/widget/modalmessage/modalmessage.js'/>"></script>
		<script type="text/javascript">
			<%@ include file="/scripts/global.js"%>
			$(document).ready(AdBeBack.init);
		</script>
        
        <decorator:head/>
    </head>
	<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
		<div id="wrapper1">
			<div id="wrapper2">
				<div id="wrapper3">
					<div id="header">
						<h1 id="logo">
							<a title="adbeback" href="<c:url value='/home.html'/>"><img alt="adbeback" src="<c:url value='/images/logo.png'/>"></a>
							<fmt:message key="default.slogan"/>
						</h1>
						<div id="header_right">

							<div id="languages_block_top">
								<jsp:include page="/common/languages.jsp"/>
							</div>
							
							<ul id="header_links">
								<%@ include file="/common/menu.jsp"%>
							</ul>

							<div id="search_block_top">
								<form id="searchbox" action="<c:url value='/search.html'/>" method="get">
									<label for="search_query"><!-- image on background --></label>
									<input type="text" value="" name="str" id="search_query" autocomplete="off" class="ac_input">
									<a href="javascript:document.getElementById('searchbox').submit();"><fmt:message key="btn.find"/></a>
								</form>
							</div>

							<div id="header_user">
								<ul>
									<jsp:include page="/common/account.jsp"/>
									<li id="shopping_cart">
										<a title="Votre panier d'achat" href="javascript:AdBeBack.ShowCart()"><fmt:message key="default.cart"/>:</a>
										<span class="ajax_cart_no_product" id="shopping_cart_nb_product">
											<c:if test="${cart == null || cart.nbProduct==0}">
												( vide )
											</c:if>
											<c:if test="${cart != null && cart.nbProduct>0}">
												${cart.nbProduct}&nbsp;<fmt:message key="default.cart.musics"/>
											</c:if>
											
										</span>
									</li>
								</ul>
							</div>
							
							<div id="sliderholder" class="hidden">
								<div id="explain">
									<div class="ui-widget">
										<div class="ui-state-highlight ui-corner-all"> 
											<strong>1.</strong><h3><fmt:message key="default.cart.add"/></h3></p>
										</div>
									</div>
									<div class="ui-widget">
										<div class="ui-state-highlight ui-corner-all"> 
											<strong>2.</strong><h3><fmt:message key="default.play"/></h3></p>
										</div>
									</div>								
									<div class="ui-widget">
										<div class="ui-state-highlight ui-corner-all"> 
											<strong>3.</strong><h3><fmt:message key="default.dl.free"/></h3></p>
										</div>
									</div>								
								</div>
								<div id="slider">
									<div class="lof-slidecontent">
										<div class="preload"><div></div></div>
										<div class="lof-main-outer" style="width:720px; height:430px;">
										  	<ul class="lof-main-wapper">
									        </ul>
										</div>
									</div>
									<div class="lof-navigator-wapper">
								        <div class="lof-next" href="" onclick="return false"><fmt:message key="default.next"/></div>
										<div class="lof-navigator-outer">
										      <ul class="lof-navigator">
										      </ul>
										</div>
								        <div class="lof-previous" href="" onclick="return false"><fmt:message key="default.previous"/></div>
 									</div>
								</div>
							</div>
						</div>
						
						<div id="columns">
							<div class="column" id="left_column"></div>
							<div class="center_column" id="center_column">
								<decorator:body />
							</div>
						</div>
					
					</div>
					
				</div>
				<div id="footer_wrapper">
					<jsp:include page="/common/footer.jsp"/>
				</div>
			</div>
		</div>
		<div id="mp3PlayerContainer" ></div>
	</body>
</html>
