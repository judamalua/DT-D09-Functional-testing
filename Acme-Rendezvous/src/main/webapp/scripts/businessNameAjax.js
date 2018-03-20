function getBusinessName() {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (this.responseText != "null") {
				document.getElementsByClassName("brand-logo")[0].innerHTML = '&#160;&#160;' + this.responseText.split(",")[0]
						+ '&#160;<img width="24" src="images/people.png" />&#160;' + this.responseText.split(",")[1];
			}

		}
	};
	xhttp.open("GET", "/Acme-Rendezvous/configuration/getBusinessName.do", true);
	xhttp.send();
}
