$(function() {
	AdBeBack.Signup = function() {
		this._init();
	};

	AdBeBack.Signup.prototype = {
		_init : function(){
			AdBeBack.init();
			$( "#btnValidate" ).button();
			
			$.datepicker.setDefaults( $.datepicker.regional[ "" ] );
			var options = $.datepicker.regional[  AdBeBack.locale ];
			options.changeMonth = true;
			options.changeYear = true;
			options.yearRange = 'c-100:c';
			$( "#birthday" ).datepicker( options );

		}
	};
	
    $(document).ready(new AdBeBack.Signup());
});
