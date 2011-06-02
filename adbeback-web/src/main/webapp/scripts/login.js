<script type="text/javascript">

	$("#SubmitCreate").button();
	$("#login").button();


    if (AdBeBack.getCookie("username") != null) {
        $("j_username").val(AdBeBack.getCookie("username"));
        $("j_password").focus();
    } else {
        $("j_username").focus();
    }
    
    function saveUsername(theForm) {
        var expires = new Date();
        expires.setTime(expires.getTime() + 24 * 30 * 60 * 60 * 1000); // sets it for approx 30 days.
        AdBeBack.setCookie("username",theForm.j_username.value,expires,"<c:url value="/"/>");
    }
    
    function validateForm(form) {                                                               
        return AdBeBack.validateRequired(form); 
    } 
    
    function passwordHint() {
        if ($("#j_username").val().length == 0) {
            alert("<fmt:message key="errors.required"><fmt:param><fmt:message key="label.username"/></fmt:param></fmt:message>");
            $("j_username").focus();
        } else {
        	
        	jQuery.ajax({
                'type': 'GET',
                'url': '<c:url value="/password.html"/>',
                'data': "username="+ $("#j_username").val(),
                'success': function(data) {
                	var elt = $('#lostpwd');
        			$.modal({
        				message:data.msg,
        				position:{left:elt.offset().left,top:elt.offset().top-elt.height()-20},
        				delay:2500
        			});
				}
            });
                 
        }
    }
    
    function required () { 
        this.aa = new Array("j_username", "<fmt:message key="errors.required"><fmt:param><fmt:message key="label.username"/></fmt:param></fmt:message>", new Function ("varName", " return this[varName];"));
        this.ab = new Array("j_password", "<fmt:message key="errors.required"><fmt:param><fmt:message key="label.password"/></fmt:param></fmt:message>", new Function ("varName", " return this[varName];"));
    } 
</script>