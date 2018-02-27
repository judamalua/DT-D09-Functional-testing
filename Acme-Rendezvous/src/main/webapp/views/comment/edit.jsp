<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<p><em><spring:message code = "form.required.params"/></em></p>

<form:form id="form" action="${requestURI}" modelAttribute="comment">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="moment"/>

	<acme:textarea code="comment.text" path="text" required="true"/>
	<acme:textbox code="comment.pictureUrl" path="pictureUrl"/>

	<acme:submit name="save" code="comment.save"/>
	<jstl:if test="${replied!=null}">
		<acme:cancel url="rendezvous/detailed-rendezvous.do?rendezvousId=${rendezvous.id}&anonymous=false" code="comment.cancel"/>
	</jstl:if>
	<jstl:if test="${replied==null}">
		<acme:cancel url="rendezvous/detailed-rendezvous.do?rendezvousId=${rendezvousId}&anonymous=false" code="comment.cancel"/>
	</jstl:if>
	
		
</form:form>

