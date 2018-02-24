<%--
 * layout.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<base
	href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="shortcut icon" href="favicon.ico"/> 



<script type="text/javascript" src="scripts/jquery.js"></script>
<script type="text/javascript" src="scripts/jquery-ui.js"></script>
<script type="text/javascript" src="scripts/jmenu.js"></script>
<script type="text/javascript" src="scripts/materialize.js"></script>


<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"> -->
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/2.0.4/css/bootstrap.min.css"> --> <!-- BOOTSTRAP 2.0.4 -->

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css"><!-- MATERIALIZE -->

<!-- <script type="text/javascript"
	src="scripts/locales/bootstrap-datetimepicker.es.js" charset="UTF-8"></script> -->

<!-- <script src="https://maxcdn.bootstrapcdn.com/bootstrap/2.0.4/js/bootstrap.min.js"></script> --> <!-- BOOTSTRAP 2.0.4 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script> <!-- MATERIALIZE -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"> <!-- MATERIALIZE ICONS -->

<link rel="stylesheet" href="styles/common.css" type="text/css">
<link rel="stylesheet" href="styles/jmenu.css" media="screen"
	type="text/css" />
<link rel="stylesheet" href="styles/displaytag.css" type="text/css">
<!-- <link rel="stylesheet" href="styles/style.css" type="text/css"> -->
<!-- <link rel="stylesheet" href="styles/bootstrap-datetimepicker.css"
	type="text/css"> -->



<title><tiles:insertAttribute name="title" ignore="true" /></title>

<script type="text/javascript">
	$(document).ready(function() {
		$("#jMenu").jMenu();
	});

	function askSubmission(msg, form) {
		if (confirm(msg))
			form.submit();
	}
	
	function relativeRedir(loc) {	
		var b = document.getElementsByTagName('base');
		if (b && b[0] && b[0].href) {
  			if (b[0].href.substr(b[0].href.length - 1) == '/' && loc.charAt(0) == '/')
    		loc = loc.substr(1);
  			loc = b[0].href + loc;
		}
		window.location.replace(loc);
	}
</script>

</head>

<body>

	<div>
		<tiles:insertAttribute name="header" />
	</div>
	<div class = "body">
	
		<h1>
			<tiles:insertAttribute name="title" />
		</h1>
		<jstl:if test="${message != null}">
			<br />
			<span class="message"><spring:message code="${message}" /></span>
		</jstl:if>
		<tiles:insertAttribute name="body" />	
	</div>
	
		<tiles:insertAttribute name="footer" />
	

</body>
</html>