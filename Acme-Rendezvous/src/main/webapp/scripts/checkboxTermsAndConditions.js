$(document).ready(function() {

	if ($("#check").is(":checked")) {
		$("#submit").attr("disabled", false);
	} else {
		$("#submit").attr("disabled", true);
	}
	$("#check").click(function() {
		if ($("#check").is(":checked")) {
			$("#submit").attr("disabled", false);
		} else {
			$("#submit").attr("disabled", true);
		}
	});
});
