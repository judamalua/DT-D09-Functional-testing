function getCommentUserName(commentId) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById("commentUser" + commentId + "name").innerHTML = this.responseText;
		}
	};
	xhttp.open("GET", "/Acme-Rendezvous/comment/ajax/name.do?commentId=" + commentId, true);
	xhttp.send();
}
function getCommentUserSurname(commentId) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById("commentUser" + commentId + "surname").innerHTML = this.responseText;
		}
	};
	xhttp.open("GET", "/Acme-Rendezvous/comment/ajax/surname.do?commentId=" + commentId, true);
	xhttp.send();
}
function getCommentUserId(commentId) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			document.getElementById("commentUser" + commentId + "id").href = "user/display.do?actorId=" + commentId;
		}
	};
	xhttp.open("GET", "/Acme-Rendezvous/comment/ajax/id.do?commentId=" + commentId, true);
	xhttp.send();
}
function getCommentUser(commentId) {
	getCommentUserId(commentId);
	getCommentUserName(commentId);
	getCommentUserSurname(commentId);
}
function getCommentUsers() {
	var elements = document.getElementsByClassName("commentId");
	for ( var i = 0, len = elements.length; i < len; i++) {
		getCommentUser(elements[i].textContent);
	}
}
