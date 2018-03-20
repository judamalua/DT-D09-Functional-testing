<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!-- Variable declaration -->
<spring:message code="slider.pictureUrl" var="titlePicture" />
<spring:message code="slider.title_es" var="titleTitle_es" />
<spring:message code="slider.title_en" var="titleTitle_en" />

<!-- Pagination -->
<acme:pagination requestURI="${requestURI}page=" pageNum="${pageNum}"
	page="${page}" />


<!-- Display -->

<display:table name="sliders" id="slider">

	<display:column title="${titlePicture}">
			<img class="serviceImg" src="${slider.pictureUrl}" />
	</display:column>
	<display:column property="title_en" title="${titleTitle_en}"/>
	<display:column property="title_es" title="${titleTitle_es}"/>

	<display:column>
		<acme:button url="slider/admin/edit.do?sliderId=${slider.id}" code="slider.edit" />
	</display:column>
</display:table>

<!-- Creating a new slide -->
	
	<acme:button url="slider/admin/create.do" code="slider.create" />
	
	
	<br/><acme:button url="configuration/admin/list.do" code="slider.return" />
