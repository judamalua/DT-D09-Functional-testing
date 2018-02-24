$(document).ready(function() {

	$('select').material_select();

	$('input[type=checkbox]').each(function() {

		var name = $(this).attr('name');
		$('[name="' + '_' + name + '"]').remove();

	});

	$('select').each(function() {

		var name = $(this).attr('name');
		$('[name="' + '_' + name + '"]').remove();

	});

	$('table').each(function() {

		$(this).attr('class', 'striped');

	});

	$('.slider').slider();

	$('.parallax').parallax();

});
