<%@ include file="/common/taglibs.jsp"%>

	<head>
	    <title><fmt:message key="login.title"/></title>
	</head>
	<body id="authentication">
	<form method="get" action="signup.html" id="create-account_form" class="std">
		<fieldset>
			<h3><fmt:message key="label.account.create"/></h3>
			<h4><fmt:message key="label.account.fillemail"/></h4>
			<p class="text">
				<label for="email_create"><fmt:message key="label.address.email"/></label>
				<input type="text" class="text" value="" name="email_create" id="email_create">
			</p>
			<p class="submit">
				<input type="submit" value="<fmt:message key="btn.create.account"/>" id="SubmitCreate">
			</p>
		</fieldset>
	</form>

	<form method="post" id="login_form" action="<c:url value='/j_security_check'/>"
	    onsubmit="saveUsername(this);return validateForm(this);">
	    <fieldset>
			<h3><fmt:message key="label.already.record"/></h3>
			<c:if test="${param.error != null}">
			    <p class="error">
			        <img src="${ctx}/images/iconWarning.gif" alt="<fmt:message key='icon.warning'/>" class="icon"/>
			        <fmt:message key="errors.password.mismatch"/>
			    </p>
			</c:if>
			<p class="text">
				<label for="j_username"><fmt:message key="label.username"/></label>
				<input type="text" class="text medium" name="j_username" id="j_username" tabindex="1" />
			</p>
			<p class="text">
				<label for="j_password">  <fmt:message key="label.password"/></label>
				<input type="password" class="text medium" name="j_password" id="j_password" tabindex="2" />
			</p>
			<p class="submit">
			 	<input type="submit" name="login" id="login" value="<fmt:message key='button.login'/>" tabindex="3" />
			</p>
			<p class="lost_password"><a id="lostpwd" href="javascript:" onclick="passwordHint();"><fmt:message key="label.forget.password"/></a></p>
			
		</fieldset>
	    
	<%@ include file="/scripts/login.js"%>

	</body>
	
	