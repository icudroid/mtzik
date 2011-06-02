<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="last.winners.title"/></title>
    <script type="text/javascript" src="<c:url value='/scripts/adbeback/lastwinners/lastwinners.js'/>"></script>
</head>

<body>

	<h2>
	   		Les derniers gagnants sur MTzik
	</h2>
	
	
	
	<table cellspacing="0" cellpadding="0" border="0" width="740" class="my-music">
		<colgroup>
			<col align="left" class="pseudo">
			<col align="center" class="date">
			<col align="left" class="value">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">Pseudo</th>
				<th scope="col">Date</th>
				<th scope="col">Valeur</th>
			</tr>
		</thead>
		<tbody>		
			<c:forEach var="win" items="${lastWinners}" varStatus="status">
				<tr class="line<c:if test="${(status.index % 2) ==0 }">1</c:if><c:if test="${(status.index % 2) !=0 }">2</c:if>">
					<td>${win.player.username}</td>
					<td><fmt:formatDate value="${win.windate}" type="both" dateStyle="short" timeStyle="short" /></td>
					<td>${win.value}&nbsp;&euro;</td>
				</tr>
			</c:forEach>
			</tbody>
	</table>

</body>
