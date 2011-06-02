<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="my.goose.game.title"/></title>
    <script type="text/javascript" src="<c:url value='/scripts/adbeback/mygoosegame/mygoosegame.js'/>"></script>
</head>

<body>
<h2>
	Jeu de MTzik
</h2>
<c:if test="level"></c:if>
<h3>Niveau : ${level.level}</h3>
<h3>Valeur de la cagnotte du niveau :  ${level.value}&nbsp;&euro;<br></h3>
<div style="height: 25px"></div>
<table style="border-collapse: separate;">
	<tr>
		<c:forEach var="gooseCase" items="${cases}" varStatus="status">
		    <c:if test="${status.index % 11 == 0}"> </tr><tr> </c:if>
		     	<td style="width:68px;height:68px;border: 1px solid #FFFFFF;">
		     	<span style="position: relative;">
			     	<div style="float: left;">
						<span></span>
						<c:choose>
							<c:when test="${gooseCase.class.name == 'fr.k2i.adbeback.core.business.goosegame.AddPotGooseCase'}"><img style="position: absolute;" alt="" src='<c:url value="/images/addCagnotte.png"/>'></c:when>
							<c:when test="${gooseCase.class.name == 'fr.k2i.adbeback.core.business.goosegame.DeadGooseCase'}"><img style="position: absolute;" alt="" src='<c:url value="/images/dead.png"/>'></c:when>
							<c:when test="${gooseCase.class.name == 'fr.k2i.adbeback.core.business.goosegame.EndLevelGooseCase'}"><img style="position: absolute;" alt="" src='<c:url value="/images/win.png"/>'></c:when>
							<c:when test="${gooseCase.class.name == 'fr.k2i.adbeback.core.business.goosegame.JailGooseCase'}"><img style="position: absolute;" alt="" src='<c:url value="/images/jail.png"/>'></c:when>
							<c:when test="${gooseCase.class.name == 'fr.k2i.adbeback.core.business.goosegame.JumpGooseCase'}"><img style="position: absolute;" alt="" src='<c:url value="/images/jump.png"/>'></c:when>
							<c:when test="${gooseCase.class.name == 'fr.k2i.adbeback.core.business.goosegame.ReductionGooseCase'}"><img style="position: absolute;" alt="" src='<c:url value="/images/reduc.png"/>'></c:when>
							<c:when test="${gooseCase.class.name == 'fr.k2i.adbeback.core.business.goosegame.StartLevelGooseCase'}"><img style="position: absolute;" alt="" src='<c:url value="/images/start.png"/>'></c:when>
						</c:choose>
						<c:if test="${gooseCase eq token.gooseCase}"> <img style="position: absolute;" alt="" src='<c:url value="/images/token.png"/>'> </c:if>
			     	</div>
			     	<div style="float:right;">
			     		<span>${gooseCase.number}</span>
			     	</div>
		     	</span>
		     		
		     	</td>
	     </c:forEach>
	</tr>
</table>
	



</body>
