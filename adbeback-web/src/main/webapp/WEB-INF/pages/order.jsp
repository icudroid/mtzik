<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="order.title" /></title>
</head>

<div id='basketContentList'>
	<div id='basketProducts' style='display: block;'>
		Votre panier contient <strong>__nbMedia__ article(s)</strong><br><br>
		<table cellspacing='0' cellpadding='10' border='0' class='tracks'>
			<tbody>
				<tr class='line1'>
					<td class='expand'><strong>__title__</strong></td>
					<td><input type='button'
						class='imaged delete basketdeleteitem' title='Supprimer'
						value='Supprimer' name='del'></td>
					<td class='devise'>__ads__ publicités</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id='basketPager'></div>
	<div id='basketMSummary'>
		<div class='total'>
			<div class='inner'>
				<div class='imaged tobepaid'>Total de publicités :</div>
				<div class='amount'>__total__ €</div>
			</div>
		</div>
	</div>
</div>
