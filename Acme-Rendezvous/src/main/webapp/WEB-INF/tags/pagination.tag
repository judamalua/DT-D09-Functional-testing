<%--
 * textbox.tag
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ tag language="java" body-content="empty" %>

<%-- Taglibs --%>

<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<%-- Attributes --%> 
 
<%@ attribute name="requestURI" required="true" %>
<%@ attribute name="pageNum" required="true" %>
<%@ attribute name="page" required="true" %>


<%-- Definition --%>

<jstl:if test="${pageNum!=0}">
	<!-- Pagination -->
	<ul class="pagination"> 
	<jstl:set var = "leftArrow" value = "waves-effect"/>
	<jstl:if test="${page==0}">
	<jstl:set var = "leftArrow" value = "disabled"/>
	</jstl:if>
	<li class="${leftArrow}"><jstl:if test="${leftArrow == \"waves-effect\"}"><a href="${requestURI}${page-1}"></jstl:if><i class="material-icons">chevron_left</i><jstl:if test="${leftArrow == \"waves-effect\"}"></a></jstl:if></li>
	<jstl:forEach begin="1" end="${pageNum}" var="index">
	
	<jstl:set var = "pageEffect" value = "waves-effect"/>
	<jstl:if test="${index-1 == page}">
		<jstl:set var = "pageEffect" value = "active"/>
	</jstl:if>
	
			<li class = "${pageEffect}"><a href="${requestURI}${index-1}">
				<jstl:out value="${index}" />
			</a></li>
			<%-- <jstl:if test="${index!=pageNum}">,</jstl:if> --%>
		</jstl:forEach> 
		
		<jstl:set var = "rightArrow" value = "waves-effect"/>
		<jstl:if test="${page==pageNum-1}">
		<jstl:set var = "rightArrow" value = "disabled"/>
		</jstl:if>
		
		<li class="${rightArrow}"><jstl:if test="${rightArrow == \"waves-effect\"}"><a href="${requestURI}${page+1}"></jstl:if><i class="material-icons">chevron_right</i><jstl:if test="${rightArrow == \"waves-effect\"}"></a></jstl:if></li>
		<br/>
	</ul>
</jstl:if>
