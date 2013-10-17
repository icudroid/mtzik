<%@page import="fr.k2i.adbeback.webapp.webenum.WebEnum"%>
<%@page import="fr.k2i.adbeback.core.business.player.Sex"%>
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="account.title"/></title>
    <script type="text/javascript" src="<c:url value='/scripts/i18n/jquery-ui-i18n.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/adbeback/account/account.js'/>"></script>
</head>

<body id="authentication">

<spring:bind path="player.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">    
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<div class="separator"></div>

<form:form commandName="player" method="post" action="account.html" id="signupForm" cssClass="std" >

	<fieldset class="account_creation">
		<h3><fmt:message key="label.own_id"/></h3>
		
		<p class="radio required">
			<span><fmt:message key="user.civility"/></span>
			<form:radiobutton path="sex" value="Mr" id="sexMr" disabled="true"/>
			<label class="top" for="sexM"><%=WebEnum.getText(Sex.Mr,request.getLocale()) %></label>
			<form:radiobutton path="sex" value="Mme" id="sexMme" disabled="true"/>
			<label class="top" for="sexMme"><%=WebEnum.getText(Sex.Mme,request.getLocale()) %></label>
			<form:radiobutton path="sex" value="Mlle" id="sexMlle" disabled="true"/>
			<label class="top" for="sexMlle"><%=WebEnum.getText(Sex.Mlle,request.getLocale()) %></label>
		</p>
		
		<p class="required text">
			<label for="username"><fmt:message key="user.username"/></label>
	        <form:input path="username" id="username" cssClass="text" cssErrorClass="text error" readonly="true" disabled="true"/>
		</p>
				
		<p class="required text">
			<label for="firstName"><fmt:message key="user.firstName"/></label>
			<form:errors path="firstName" cssClass="fieldError"/>
            <form:input path="firstName" id="firstName" cssClass="text" cssErrorClass="text error" maxlength="50" readonly="true" disabled="true"/>
		</p>
		
		
		<p class="required text">
			<label for="lastName"><fmt:message key="user.lastName"/></label>
			<form:errors path="lastName" cssClass="fieldError"/>
            <form:input path="lastName" id="lastName" cssClass="text" cssErrorClass="text error" maxlength="50" readonly="true" disabled="true"/>
		</p>
		
		<p class="required text">
			<label for="email"><fmt:message key="user.email"/></label>
			<form:errors path="email" cssClass="fieldError"/>
            <form:input path="email" id="email" cssClass="text" cssErrorClass="text error"/>
            <sup>*</sup>
		</p>
		
		<p class="required text">
			<label for="password"><fmt:message key="user.password"/></label>
             <form:errors path="password" cssClass="fieldError"/>
             <form:password path="password" id="password" cssClass="text" cssErrorClass="text error" showPassword="true"/>
             <sup>*</sup>
		</p>

		<p class="required text">
			<label for="confirmPassword"><fmt:message key="user.confirmPassword"/></label>
            <form:errors path="confirmPassword" cssClass="fieldError"/>
            <form:password path="confirmPassword" id="confirmPassword" cssClass="text" cssErrorClass="text error" showPassword="true"/>
            <sup>*</sup>
		</p>		
		
	
		<p class="required text">
			<label for="birthday"><fmt:message key="user.birthday"/></label>
            <form:errors path="birthday" cssClass="fieldError"/>
            <form:input path="birthday" id="birthday" cssClass="text" cssErrorClass="text error" readonly="true" disabled="true"/>
		</p>	
		
		<p class="checkbox">
			<form:checkbox path="newsletter" value="true" id="newsletter"/>
			<label for="newsletter"><fmt:message key="user.newsletter"/></label>
		</p>		
		
		</fieldset>
		
		<fieldset class="account_creation">
			<h3><fmt:message key="label.address"/></h3>
			
			<p class="required text">
				<label for="address.address"><fmt:message key="user.address.address"/></label>
	            <form:errors path="address.address" cssClass="fieldError"/>
	            <form:input path="address.address" id="address.address" cssClass="text" cssErrorClass="text error"/>
			</p>
			
			<p class="required text">
				<label for="address.compAddress"><fmt:message key="user.address.compAddress"/></label>
	            <form:errors path="address.compAddress" cssClass="fieldError"/>
	            <form:input path="address.compAddress" id="address.compAddress" cssClass="text" cssErrorClass="text error"/>
			</p>
			
			<p class="required text">
				<label for="address.city"><fmt:message key="user.address.city"/></label>
	            <form:errors path="address.city" cssClass="fieldError"/>
	            <form:input path="address.city" id="address.city" cssClass="text" cssErrorClass="text error"/>
			</p>	
			
			<p class="required text">
				<label for="address.province"><fmt:message key="user.address.province"/></label>
	            <form:errors path="address.province" cssClass="fieldError"/>
	            <form:input path="address.province" id="address.province" cssClass="text" cssErrorClass="text error"/>
			</p>
			
			<p class="required text">
				<label for="address.postalCode"><fmt:message key="user.address.postalCode"/></label>
	            <form:errors path="address.postalCode" cssClass="fieldError"/>
	            <form:input path="address.postalCode" id="address.postalCode" cssClass="text" cssErrorClass="text error"/>
			</p>
			
			<p class="required text">
			 	<appfuse:country name="address.country.code" prompt="" default="${player.address.country.code}"/>
				<label for="address.country.code"><fmt:message key="user.address.country"/></label>
			</p>
			
		</fieldset>
		
		<p class="required required_desc">
			<span><sup>*</sup><fmt:message key="required"/></span>
		</p>
		
		<p class="submit">
			<input id="btnValidate" type="submit" name="save" onclick="bCancel=false" value="<fmt:message key="button.modify"/>"/>
		</p>
		

</form:form>

<v:javascript formName="player" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>


</body>
