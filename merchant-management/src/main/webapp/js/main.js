$(document).ready(function() {
	$('#dl-menu').dlmenu();
	$('.button').button();
	$('label.required').each(function() {
		var label = $(this);
		label.html(label.text() + '&nbsp;<span style="color: red;">*</span>');
	});
});