<%@ include file="/common/taglibs.jsp"%>

<head>
 	<meta name="SearchMenu" content="active"/>
    <title><fmt:message key="search.title"/></title>
    <script type="text/javascript" src="<c:url value='/scripts/adbeback/search/search.js'/>"></script>
    
     <script type="text/javascript">
		<c:if test="${catSearch == true }">
			var initCat = '${categorie}';
		</c:if>
		<c:if test="${strSearch == true }">
			var initStr = "${str}";
		</c:if>			
     </script>
</head>
<body>

	<div class="ui-widget center">
		<input id="fieldSearch" />
		<button id="btnSearch">Rechercher</button>
	</div>
	
	<div id="result"></div>
	
</body>

