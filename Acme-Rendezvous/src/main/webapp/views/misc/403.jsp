<%--
 * 403.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<center><img src="https://media.giphy.com/media/5Zesu5VPNGJlm/200.gif"/></center>
<center><h1>Oops! It appears that you don't have access to this resource.</h1></center>
<center><h2>If this is an error, our team of highly trained monkeys is trying to repair this problem right now.</h2></center>
<center><h2>Thank you for using this service.</h2></center>

<center><p><a href="<spring:url value='/' />">Return to the welcome page</a><p></center>
