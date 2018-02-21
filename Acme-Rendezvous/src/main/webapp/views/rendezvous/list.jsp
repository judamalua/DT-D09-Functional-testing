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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Variable declaration -->
<spring:message code="rendezvous.name" var="titleName" />
<spring:message code="rendezvous.description" var="titleDescription" />
<spring:message code="rendezvous.moment" var="titleMoment" />
<spring:message code="master.page.moment.format" var="formatMoment" />
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />

<jstl:if test="${pageNum!=0}">
	<!-- Pagination -->
	<span class="pagebanner"> <jstl:forEach begin="1"
			end="${pageNum}" var="index">
			<a href="${requestURI}?&pageage=${index-1}&anonymous=${anonymous}">
				<jstl:out value="${index}" />
			</a>
			<jstl:if test="${index!=pageNum}">,</jstl:if>
		</jstl:forEach> <br />
	</span>
</jstl:if>
<!-- Pagination -->

<!-- Display -->
<display:table name="rendezvouses" id="rendezvous"
	requestURI="${requestUri}">

	<display:column property="name" title="${titleName}" />
	<display:column property="description" title="${titleDescription}" />
	<display:column property="moment" title="${titleMoment}"
		format="${formatMoment}" />
	<display:column>
		<acme:button url="rendezvous/detailed-rendezvous.do?rendezvousId=${rendezvous.id}&anonymous=${anonymous}" code="rendezvous.details"/>
	</display:column>

	<display:column>
		<jstl:if test="${!rendezvous.finalMode and !rendezvous.deleted and rendezvous.moment>currentDate}">
			<acme:button url="rendezvous/user/edit.do?rendezvousId=${rendezvous.id}" code="rendezvous.edit"/>
		</jstl:if>
	</display:column>


	<display:column>
		<jstl:if test="${rendezvous.deleted}">
			<img src="images/deleted-rendezvous.png" />
			<spring:message code="rendezvous.deleted" />
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${rendezvous.adultOnly}">
			<img src="images/18.png" />
			<spring:message code="rendezvous.adultOnly" />
		</jstl:if>
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<acme:button url="rendezvous/admin/delete.do?rendezvousId=${rendezvous.id}" code="rendezvous.delete"/>
		</display:column>
	</security:authorize>

</display:table>

<!-- Creating a new rendezvous -->

<jstl:if test="${not anonymous}">
	<acme:button url="rendezvous/user/create.do" code="rendezvous.create"/>
</jstl:if>
