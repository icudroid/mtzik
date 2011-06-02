<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="home.title"/></title>
    <meta name="heading" content="<fmt:message key='home.heading'/>"/>
    <meta name="HomeMenu" content="active"/>
    
    <script type="text/javascript" src="<c:url value='/scripts/jquery.easing.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery.lofslidernews.js'/>"></script>
    <script type="text/javascript" >
    	<%@ include file="/scripts/adbeback/home/home.js"%>
    </script>
    
</head>


<body>
<div class="block products_block" id="featured-products_block_center">
	<h4><fmt:message key="home.best.dl"/></h4>
			<div class="block_content">
			<ul style="">
				<c:forEach var="entry" items="${bestDownload}" >
					<li class="ajax_block_product">
						<a title="${entry.title}" href="<c:url value='/product/${entry.title}_${entry.id}.html'/>" class="product_image"><img width="170" height="170" alt="${entry.title}" src="<c:url value='/img/${entry.jacket}'/>"></a>
						<div class="product_info">
							<h5><a title="${entry.title}" href="<c:url value='/product/${entry.title}_${entry.id}.html'/>">${entry.title}</a></h5>
						</div>
						<div class="buttons">
							<a title="<fmt:message key="default.cart.add"/>" href="javascript:" onclick="AdBeBack.AddToCart(${entry.id},$(this));" class="add2cart ajax_add_to_cart_button"><fmt:message key="default.cart.add"/></a>
							<a title="<fmt:message key="home.more.info"/>" href="<c:url value='/product/${entry.title}_${entry.id}.html'/>" class="viewlink"><fmt:message key="home.more.info"/></a>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</body>
		

