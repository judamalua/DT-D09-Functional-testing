<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Variable declaration -->
<spring:message code="rendezvous.name" var="titleName"/>
<spring:message code="rendezvous.description" var="titleDescription"/>
<spring:message code="rendezvous.moment" var="titleMoment"/>
<spring:message code="master.page.moment.format" var="formatMoment"/>
<jsp:useBean id="now" class=java.util.Date/>
<fmt:formatDate var="currentDate" value="${now}" pattern="yyyy-MM-dd HH:mm"/>


<!-- Display -->
<display:table name="rendezvouses" id="rendezvous" requestURI="${requestUri}" pagesize="10">
	
	<display:column property="name" title="${titleName}"/>
	<display:column property="description" title="${titleDescription}"/>
	<display:column property="moment" title="${titleMoment}" format="${formatMoment}"/>
	<display:column>
		<a href="rendezvous/detailed-rendezvous.do?rendezvousId=${rendezvous.id}">
			<button class="btn">
				<spring:message code="rendevous.edit"/>
			</button>
		</a>
	</display:column>
	<display:column>
		<jstl:if test="${!rendezvous.finalMode and !rendezvous.deleted and rendezvous.moment>currentMoment}">
			<a href="rendezvous/user/create.do?rendezvousId=${rendezvous.id}">
				<button class="btn">
					<spring:message code="rendevous.edit"/>
				</button>
			</a>
		</jstl:if>
	</display:column>
</display:table>

<!-- Creating a new rendezvous -->
<a href="rendezvous/user/create.do">
	<button class="btn">
		<spring:message code="rendevous.create"/>
	</button>
</a>
