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

<form:form id="form" action="${requestURI}" modelAttribute="comment">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<form:hidden path="moment"/>
	<form:hidden path="comments"/>
	
	<form:label path="text">
		<spring:message code="comment.text"/> 
	</form:label>
	<form:textarea path="text"/>
	<form:errors cssClass="error" path="text"/>
	<br/>
	
	<form:label path="pictureUrl">
		<spring:message code="comment.pictureUrl"/> 
	</form:label>
	<form:input path="pictureUrl"/>
	<form:errors cssClass="error" path="pictureUrl"/>
	<br/>

	
	<input type="submit" name="save" class="btn" value="<spring:message code="comment.save"/>"/>
	
	<input type="button" name="cancel" class="btn"
		value="<spring:message code="comment.cancel" />"
		onclick="javascript: window.history.back();" />
		
</form:form>

