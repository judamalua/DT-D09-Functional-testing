function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for ( var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) { return c.substring(name.length, c.length); }
	}
	return null;
}
function acceptCookies() {
	document.getElementsByClassName("cookies")[0].innerHTML = "";
	document.cookie = "acceptCookies=true;path=/";
}
function showCookieMessage() {
	var language = getCookie("language");
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (language == "es") {
				document.getElementsByClassName("cookies")[0].innerHTML = '<div class="row"><div class="col s12 m6"><div class="card red darken-4"><div class="card-content white-text"><span class="card-title">Sobre las cookies</span><p>'
						+ this.responseText
						+ '</p></div><div class="card-action"><a href="/Acme-Rendezvous/cookie/policy.do">Política de cookies</a><a href="javascript:void(0);" onClick="acceptCookies();">Aceptar las cookies </a></div></div></div></div>';
			} else {
				document.getElementsByClassName("cookies")[0].innerHTML = '<div class="row"><div class="col s12 m6"><div class="card red darken-4"><div class="card-content white-text"><span class="card-title">About cookies</span><p>'
						+ this.responseText
						+ '</p></div><div class="card-action"><a href="/Acme-Rendezvous/cookie/policy.do">Cookie policy</a><a href="javascript:void(0);" onClick="acceptCookies();">Accept cookies </a></div></div></div></div>';
			}
		}
	};
	if (language == "es") {
		xhttp.open("GET", "/Acme-Rendezvous/cookie/ajax/es.do", true);
	} else {
		xhttp.open("GET", "/Acme-Rendezvous/cookie/ajax/en.do", true);
	}
	xhttp.send();

}
function checkCookie() {
	var acceptCookies = getCookie("acceptCookies");
	if (acceptCookies != "" && acceptCookies != null) {
		if (acceptCookies == "false") {
			showCookieMessage();
		}
	} else {
		if (acceptCookies == null) {
			document.cookie = "acceptCookies=false;path=/";
			showCookieMessage();
		}
	}
}
