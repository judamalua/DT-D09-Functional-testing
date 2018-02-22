<%--
 * footer.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<jsp:useBean id="date" class="java.util.Date" />

<div class = "footer">

<script src="scripts/cookieAjax.js"></script>
<hr />
<script type="text/javascript">
window.onload = function() {
	  checkCookie();
	};

</script>
<div class="cookies"></div>
<strong>Copyright &copy; <fmt:formatDate value="${date}" pattern="yyyy" /> Acme Inc.</strong>
<br/>
<a href = "law/termsAndConditions.do"><spring:message code = "master.page.terms&conditions"/></a>
<div><spring:message code = "icons.license.one"/> <a href="https://www.flaticon.com/authors/flat-icons" title="Flat Icons">Flat Icons</a> <spring:message code = "icons.license.two"/> <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> <spring:message code = "icons.license.three"/> <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
</div>