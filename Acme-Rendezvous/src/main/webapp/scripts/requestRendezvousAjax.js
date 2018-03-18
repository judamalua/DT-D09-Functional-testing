function getRendezvous(requestId) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementsByClassName("request" + requestId)[0].innerHTML = '<a href="/Acme-Rendezvous/rendezvous/detailed-rendezvous.do?rendezvousId='
					+ this.responseText.split(",")[0] + '&anonymous=false">' + this.responseText.split(",")[1] + '</a>';
		}
	};
	xhttp.open("GET", "/Acme-Rendezvous/request/manager/getRendezvous.do?requestId=" + requestId, true);
	xhttp.send();
}
function getRendezvouses() {
	var elements = document.getElementsByClassName("requestId");
	for ( var i = 0, len = elements.length; i < len; i++) {
		getRendezvous(elements[i].textContent);
	}
}
