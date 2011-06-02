<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="contact.title"/></title>
    <script type="text/javascript" src="<c:url value='/scripts/adbeback/contact/contact.js'/>"></script>
</head>

<body id="contact-form">
	<h2>Contactez-nous</h2>

	<p class="bold">Pour des questions à propos d'une commande ou des informations sur nos produits.</p>
		<form:form commandName="contactForm" method="post" action="contact-form.html" cssClass="std">
		<fieldset>
			<h3>Envoyez un message</h3>
			<p class="select">
				<label for="id_contact">Objet</label>
				<form:select path="object">
					<form:option value="-" label="-- Choisir --"/>
					<form:options items="${objects}" itemValue="value" itemLabel="label"/>
				</form:select>
			</p>
			<p class="text">
				<label for="email">Votre adresse e-mail</label>
				<form:input path="email" cssClass="text" id="email"/>
			</p>
			<p class="textarea">
				<label for="message">Message</label>
				<form:textarea path="message" id="message"  cols="35" rows="7" />
			</p>
			<p class="submit">
				<input type="submit" value="Envoyer" id="submitMessage" name="submitMessage">
			</p>
		</fieldset>
	</form:form>

</body>