<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="my.win.title"/></title>
	<script type="text/javascript" src="<c:url value='/scripts/adbeback/my-win/my-win.js'/>"></script>
</head>

<body>
	<h2>
	   		Mes gains
	</h2>
	
	
	
	<table cellspacing="0" cellpadding="0" border="0" width="740" class="my-music">
		<colgroup>
			<col align="center" class="date">
			<col align="left" class="value">
			<col align="left" class="status">
			<col align="center" class="transfert">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">Date</th>
				<th scope="col">Valeur</th>
				<th scope="col">Statut</th>
				<th scope="col">Transférer</th>
			</tr>
		</thead>
		<tbody>		
			<c:forEach var="win" items="${player.wins}" varStatus="status">
				<tr class="line<c:if test="${(status.index % 2) ==0 }">1</c:if><c:if test="${(status.index % 2) !=0 }">2</c:if>">
					<td><fmt:formatDate value="${win.windate}" type="both" dateStyle="short" timeStyle="short" /></td>
					<td>${win.value}&nbsp;&euro;</td>
					<td>${win.status.label}</td>
					<td>
						<c:if test="${'NotTranfered' eq  win.status}">
							<button class="btnTransfert" onclick="AdBeBack.MyWin.transfert(${win.id})">Transférer</button>
						</c:if>
					</td>
				</tr>
			</c:forEach>
	
			</tbody>
	</table>


</body>


