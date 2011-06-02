<%@ include file="/common/taglibs.jsp"%>


<c:if test="${pageContext.request.locale.language == 'en'}">
	<ul id="first-languages">
		<li class="selected_language">
			<img width="16" height="11" alt="en" src="<c:url value='/images/en.jpg'/>">
		</li>
		<li style="opacity: 0.3;">
			<a title="Français (French)" href="<c:url value='/?locale=fr'/>">
				<img width="16" height="11" alt="fr" src="<c:url value='/images/fr.jpg'/>">
			</a>
		</li>
		<li style="opacity: 0.3;">
			<a title="Español (Spanish)" href="<c:url value='/?locale=es'/>">
				<img width="16" height="11" alt="es" src="<c:url value='/images/es.jpg'/>">
			</a>
		</li>
	</ul>
</c:if>

<c:if test="${pageContext.request.locale.language == 'fr'}">
	<ul id="first-languages">
		<li style="opacity: 0.3;">
			<a title="English (English)" href="<c:url value='/?locale=en'/>">
				<img width="16" height="11" alt="en" src="<c:url value='/images/en.jpg'/>">
			</a>
		</li>
		<li class="selected_language">
			<img width="16" height="11" alt="fr" src="<c:url value='/images/fr.jpg'/>">
		</li>
		<li style="opacity: 0.3;">
			<a title="Español (Spanish)" href="<c:url value='/?locale=es'/>">
				<img width="16" height="11" alt="es" src="<c:url value='/images/es.jpg'/>">
			</a>
		</li>
	</ul>
</c:if>
<c:if test="${pageContext.request.locale.language == 'es'}">
	<ul id="first-languages">
		<li style="opacity: 0.3;">
			<a title="English (English)" href="<c:url value='/?locale=en'/>">
				<img width="16" height="11" alt="en" src="<c:url value='/images/en.jpg'/>">
			</a>
		</li>
		<li style="opacity: 0.3;">
			<a title="Français (French)" href="<c:url value='/?locale=fr'/>">
				<img width="16" height="11" alt="fr" src="<c:url value='/images/fr.jpg'/>">
			</a>
		</li>
		<li  class="selected_language">
			<img width="16" height="11" alt="es" src="<c:url value='/images/es.jpg'/>">
		</li>
	</ul>
</c:if>
<label><fmt:message key="label.language"/></label>