<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="adgame.title"/></title>
     <script type="text/javascript" src="<c:url value='/scripts/adbeback/adgame/adgame.js'/>"></script>
</head>

<body>
       <div id="flashContent">
	       	<p>
	        	To view this page ensure that Adobe Flash Player version 
				10.0.0 or greater is installed. 
			</p>
			<script type="text/javascript"> 
				var pageHost = ((document.location.protocol == "https:") ? "https://" :	"http://"); 
				document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
								+ pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
			</script> 
       </div>
</body>
