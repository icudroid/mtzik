<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>${media.title}</title>
	<script type="text/javascript" src="<c:url value='/scripts/adbeback/product/product.js'/>"></script>
</head>

<body>
<h2>
   	<c:if test="${type eq 'music'}">
		<c:forEach var="artist" items="${media.artists}" varStatus="status">
			<c:if test="${status.index != 0}"> | </c:if>
		     	${artist.firstName} ${artist.lastName}
	    </c:forEach>
   	</c:if>
	<c:if test="${type eq 'Album'}">
   		Album
	</c:if>
</h2>
<div id="primary_block">
	<!-- right infos-->
	<div id="pb-right-column">
		<div id="image-block">
			<img width="290" height="270" id="bigpic" alt="${media.title}" title="${media.title}" src="<c:url value='/img/${media.jacket}'/>">
		</div>
	</div>
	<div id="pb-left-column">
		<div id="short_description_block">
			<div id="short_description_content"> ${media.description} </div>
			<div class="features">
				<strong>Titre :</strong>${media.title}<br>
				<strong>Durée :</strong><appfuse:formatTime duration="${media.duration}"/><br>
				<strong>Genre :</strong>
				    <c:forEach var="genre" items="${media.genres}" varStatus="status">
				   		<c:if test="${status.index != 0}"> | </c:if>
				   		${genre.genre}
				   	</c:forEach>
					<br>
			</div>
			<div class="produced">
				<strong>Producteurs :</strong>
				<c:forEach var="productor" items="${media.productors}" varStatus="status">
				    <c:if test="${status.index != 0}"> | </c:if>
				     	${productor.firstName} ${productor.lastName}
			     </c:forEach>
	            <br>
				<strong>Date de première publication :</strong> <fmt:formatDate value="${media.releaseDate}" type="date" dateStyle="short" /> <br>
			</div>
			<c:if test="${type eq 'music'}">
				<div class="authors">	 
		            Interprète(s) : 
		            	<c:forEach var="artist" items="${media.artists}" varStatus="status">
							<c:if test="${status.index != 0}"> | </c:if>
						     	${artist.firstName} ${artist.lastName}
					    </c:forEach>
		            <br>
				</div>
				<div class="album">
		            Album(s) : 
		            	<c:forEach var="album" items="${media.albums}" varStatus="status">
							<c:if test="${status.index != 0}"> | </c:if>
								<a href="<c:url value='/product/${album.title}_${album.id}.html'/>">${album.title}</a>
					    </c:forEach>
				</div>
			</c:if>						
		</div>
		<c:if test="${type eq 'music'}">
			<p class="buttons_bottom_block" id="add_to_cart"><a class="add2cart" onclick="AdBeBack.AddToCart(${media.id},$(this))">Ajouter au panier</a></p>
		</c:if>
	</div>
<br clear="all">
<br clear="all">
</div>
<div class="more_info_block">
	<c:if test="${type eq 'music'}">
		<div class="tracklist">
		    <table cellspacing="0" cellpadding="5" border="0" width="100%" class="tracks">
		       	 <tbody>
		       	 	<tr class="line1">
			            <td>
			            	<a class="my-icon ui-state-default ui-corner-all" onclick="AdBeBack.ListenMusic(${media.id},$(this))" href="javascript:" title="Ecouter un extrait"><span class="ui-icon  ui-icon-play">Ecouter un extrait</span></a>
			            </td>
			            <td class="expand">
			                ${media.title}
			            </td>
			            <td>
			          	 	<c:forEach var="album" items="${media.albums}" varStatus="status">
								<c:if test="${status.index != 0}"> | </c:if>
									<a href="<c:url value='/product/${album.title}_${album.id}.html'/>">${album.title}</a>
					    	</c:forEach>
			            </td>
		               <td><appfuse:formatTime duration="${media.duration}"/></td>
			            <td></td>
			            <td>
			            	<a class="my-icon ui-state-default ui-corner-all" onclick="AdBeBack.AddToCart(${media.id},$(this));" title="Ajouter au panier"><span class="ui-icon ui-icon-plusthick">Ajouter au panier</span></a>
			            </td>
					</tr>
		    	</tbody>
		    </table>
		</div>
	</c:if>
	<c:if test="${type eq 'Album'}">
		<div class="tracklist">
		    <table cellspacing="0" cellpadding="5" border="0" width="100%" class="tracks">
		       	 <tbody>
					<c:forEach var="music" items="${media.musics}" varStatus="status">
						<tr class="line<c:if test="${(status.index % 2) ==0 }">1</c:if><c:if test="${(status.index % 2) !=0 }">2</c:if>">
				            <td>
							   <a class="my-icon ui-state-default ui-corner-all" href="javascript:" onclick="AdBeBack.ListenMusic(${music.id},$(this));" title="Ecouter un extrait"><span class="ui-icon  ui-icon-play">Ecouter un extrait</span></a>				            
				            </td>
				            <td class="expand">
				            	<a href="<c:url value='/product/${music.title}_${music.id}.html'/>">${music.title}</a>
				            </td>
				            <td></td>
			               <td><appfuse:formatTime duration="${music.duration}"/></td>
				            <td></td>
				            <td>
				            	<a class="my-icon ui-state-default ui-corner-all" onclick="AdBeBack.AddToCart(${music.id},$(this));" title="Ajouter au panier"><span class="ui-icon ui-icon-plusthick">Ajouter au panier</span></a>
				            </td>
						</tr>
						
				    </c:forEach>
			    </tbody>
		    </table>
		</div>
	</c:if>
</div>

</body>


