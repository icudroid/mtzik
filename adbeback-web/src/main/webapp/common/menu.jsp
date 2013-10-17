<%@ include file="/common/taglibs.jsp"%>

<li>
	<a class="<decorator:getProperty property="meta.HomeMenu" default="" />" title="accueil " href="<c:url value='/home.html'/>"><fmt:message key="menu.home"/></a>
</li>
<li>
	<a class="<decorator:getProperty property="meta.NewsMenu" default="" />" title="" href="<c:url value='/new-products.html'/>"><fmt:message key="footer.news"/></a>
</li>
<li>
	<a class="<decorator:getProperty property="meta.BestDLMenu" default="" />" title="" href="<c:url value='/best-download.html'/>"><fmt:message key="footer.best.dl"/></a>
</li>
<li>
	<a class="<decorator:getProperty property="meta.SearchMenu" default="" />" title="recherche" href="<c:url value='/search.html'/>"><fmt:message key="menu.advanced.search"/></a>
</li>