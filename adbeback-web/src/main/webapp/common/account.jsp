<%@ include file="/common/taglibs.jsp"%>

<li id="header_user_info">
	<c:if test="${empty pageContext.request.remoteUser}">
		<a href="<c:url value='/account.html'/>""><fmt:message key="label.login"/></a>
	</c:if>
	<c:if test="${!empty pageContext.request.remoteUser}">
		<a href="<c:url value='/logout.jsp'/>""><fmt:message key="label.logout"/></a>
	</c:if>
	<a title="<fmt:message key="label.your_account"/>" href="<c:url value='/account.html'/>" id="your_account"><fmt:message key="label.your_account"/></a>
</li>