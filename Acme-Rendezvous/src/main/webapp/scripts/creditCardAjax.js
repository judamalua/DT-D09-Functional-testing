function useOldCreditCard() {
	var cardId = document.getElementsByClassName("oldCreditCardId")[0].innerHTML;
	var language = getCookie("language");
	document.getElementsByClassName("cardForm")[0].innerHTML = '<input id="creditCard" name="creditCard" type="hidden" value="' + cardId + '">';
	document.getElementsByClassName("creditCardButton")[0].onclick = function() {
		useNewCreditCard();
	};

	if (language == "es") {
		document.getElementsByClassName("creditCardButton")[0].innerHTML = "Usar una nueva tarjeta";
	} else {
		document.getElementsByClassName("creditCardButton")[0].innerHTML = "Use a new credit card";
	}
};
function useNewCreditCard() {
	/*
	 * var language = getCookie("language"); document.getElementsByClassName("cardForm")[0].innerHTML='<input id="creditCard.id" name="creditCard.id" type="hidden" value="0"><input
	 * id="creditCard.version" name="creditCard.version" type="hidden" value="0"><input id="creditCard.cookieToken" name="creditCard.cookieToken" type="hidden" value=""><div><div
	 * class="row"><div class="input-field col s3"><label for="creditCard.holderName" class="">Holder name*</label><input id="creditCard.holderName"
	 * name="creditCard.holderName" type="text" value=""> </div></div></div><div><div class="row"><div class="input-field col s3"><label for="creditCard.brandName">Brand
	 * name*</label><input id="creditCard.brandName" name="creditCard.brandName" type="text" value=""> </div></div></div><div><div class="row"><div class="input-field col
	 * s3"><label for="creditCard.number">Number*</label><input id="creditCard.number" name="creditCard.number" type="text" value=""></div></div></div><div><div
	 * class="row"><div class="input-field col s3"><label for="creditCard.expirationMonth" class="active">Expiration month*</label><input id="creditCard.expirationMonth"
	 * name="creditCard.expirationMonth" placeholder="MM" type="text" value=""> </div></div></div><div><div class="row"><div class="input-field col s3"><label
	 * for="creditCard.expirationYear" class="active">Expiration year*</label><input id="creditCard.expirationYear" name="creditCard.expirationYear" placeholder="yy" type="text"
	 * value=""></div></div></div><div><div class="row"><div class="input-field col s3"><label for="creditCard.cvv">CVV*</label><input id="creditCard.cvv"
	 * name="creditCard.cvv" type="text" value=""></div></div></div>'; document.getElementsByClassName("creditCardButton")[0].onclick = function () { useOldCreditCard(); };
	 * document.getElementsByClassName("creditCardCookieToken")[0].value=document.getElementsByClassName("creditCardCookieTokenNew")[0].innerHTML; if (language == "es") {
	 * document.getElementsByClassName("creditCardButton")[0].innerHTML="Usar esta tarjeta"; } else { document.getElementsByClassName("creditCardButton")[0].innerHTML="Use this
	 * card"; }
	 */
	location.reload();
};
function checkCreditCard() {
	debugger;
	var cardCookie = getCookie("cardCookie");
	var language = getCookie("language");
	if (cardCookie != null || cardCookie != "") {
		var xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				if (this.responseText != "null") {
					if (language == "es") {
						document.getElementsByClassName("cookieCard")[0].innerHTML = '<div class="row"><div class="col s12 m6"><div class="card red darken-4"><div class="card-content white-text"><span class="card-title">Usar una tarjeta de crédito anterior</span><p>Con el número XXXX XXXX XXXX '
								+ this.responseText.substring(0, 4)
								+ '</p></div><div class="card-action"><a onclick="useOldCreditCard()" class="creditCardButton" href="javascript:void(0);">Usar esta tarjeta</a></div></div></div></div><p hidden="true" class="oldCreditCardId">'
								+ this.responseText.substring(4) + '</p>';
					} else {
						document.getElementsByClassName("cookieCard")[0].innerHTML = '<div class="row"><div class="col s12 m6"><div class="card red darken-4"><div class="card-content white-text"><span class="card-title">Use a previous credit card</span><p>With the number XXXX XXXX XXXX '
								+ this.responseText.substring(0, 4)
								+ '</p></div><div class="card-action"><a onclick="useOldCreditCard()" class="creditCardButton" href="javascript:void(0);">Use this card</a></div></div></div></div><p hidden="true" class="oldCreditCardId">'
								+ this.responseText.substring(4) + '</p>';
					}
				}
			}
		};
		xhttp.open("GET", "/Acme-Rendezvous/request/user/ajaxCard.do?cookieToken=" + cardCookie, true);
		xhttp.send();
	}
};
function saveCreditCardCookie() {
	debugger;
	var cardCookie = document.getElementsByClassName("creditCardCookieToken")[0].value;
	document.cookie = "cardCookie=" + cardCookie + ";path=/";
};