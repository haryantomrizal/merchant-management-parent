$('#content').ready(function() {
	$('#permissionsCheckboxContainer input[type=checkbox]').change(function() {
		var checked = $(this).prop('checked');
		if (checked) {
			$(this).prop('checked',true);
			$(this).parents().children('input[type=checkbox]').prop('checked',true);
			$(this).parent().find('ul input[type=checkbox]').prop('checked',true);
		} else {
			$(this).parent().find('ul input[type=checkbox]:checked').prop('checked',false);
		}
	});
});