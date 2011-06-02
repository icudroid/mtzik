<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="my.music.dl.title"/></title>
	<script type="text/javascript" src="<c:url value='/scripts/adbeback/my-musics/my-musics.js'/>"></script>
</head>

<body>
	<h2>
	   		Mes Musiques de moins de 7 jours
	</h2>
	
	
	
	<table cellspacing="0" cellpadding="0" border="0" width="740" class="my-music">
		<colgroup>
			<col align="center" class="date">
			<col align="left" class="muscis">
			<col align="left" class="unlockCode">
			<col align="center" class="download">
		</colgroup>
		<thead>
			<tr>
				<th scope="col">Date</th>
				<th scope="col">Mes musiques</th>
				<th scope="col">Code de l'archive</th>
				<th scope="col">Télécharger</th>
			</tr>
		</thead>
		<tbody>		
			<c:forEach var="game" items="${adgames}" varStatus="status">
				<tr class="line<c:if test="${(status.index % 2) ==0 }">1</c:if><c:if test="${(status.index % 2) !=0 }">2</c:if>">
					<td><fmt:formatDate value="${game.generated}" type="both" dateStyle="short" timeStyle="short" /></td>
					<td>
						<ul>
							<c:forEach var="music" items="${game.medias}">
								<li>${music.title}</li>						
							</c:forEach>
						</ul>
					</td>
					<td>${game.unlockCode}</td>
					<td>
						<button class="btnDl" onclick="AdBeBack.MyMusics.download(${game.id})">Télécharger</button>
					</td>
				</tr>
			</c:forEach>
	
			</tbody>
	</table>


</body>


