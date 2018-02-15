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
<spring:message code="master.page.moment.format" var="formatMoment"/>
<spring:message code="rendezvous.name" var="titleName"/>
<spring:message code="rendezvous.description" var="titleDescription"/>
<spring:message code="rendezvous.moment" var="titleMoment"/>
<spring:message code="rendezvous.announcement.title" var="titleAnnouncement"/>
<spring:message code="rendezvous.announcement.description" var="descriptionAnnouncement"/>
<spring:message code="rendezvous.announcement.moment" var="momentAnnouncement"/>
<spring:message code="rendezvous.comment.text" var="textComment"/>
<spring:message code="rendezvous.comment.moment" var="momentComment"/>
<spring:message code="rendezvous.comment.display" var="displayComment"/>


<jsp:useBean id="now" class=java.util.Date/>
<fmt:formatDate var="currentDate" value="${now}" pattern="yyyy-MM-dd HH:mm"/>
<fmt:formatDate var="formatMomentRendezvous" value="${rendezvous.moment}" pattern="<spring:message code="master.page.moment.pattern.out"/>"/>
<spring:message code="master.page.moment.format" var="formatMoment"/>


<!-- Display -->
<img src="${rendezvous.pictureUrl}" alt="${rendezvous.name}"/>

<h2><jstl:out value="${rendezvous.name}"/></h2>

<p><jstl:out value="${rendezvous.description}"/></p>
<br/>

<p><jstl:out value="${formatMomentRendezvous}"/></p>

<iframe class="map" src="https://www.google.com/maps/embed/v1/search?q=${rendezvous.gpsCoordinates}&key=AIzaSyBe0wmulZvK1IM3-3jIUgbxt2Ax_QOVW6c"></iframe>

<!-- Displaying similar rendevouses -->
<display:table name="rendezvouse.similars" id="similar" requestURI="rendezvouse/detailed-rendezvous.do" pagesize="10">
	<display:column property="name" title="${titleName}"/>
	<display:column property="description" title="${titleDescription}"/>
	<display:column property="moment" title="${titleMoment}" format="${formatMoment}"/>
	<display:column>
		<a href="rendezvous/user/detailed-rendezvous.do?rendezvousId=${rendezvous.id}">
			<button class="btn">
				<spring:message code="rendevous.edit"/>
			</button>
		</a>
	</display:column>
</display:table>	

<!-- Displaying announcements -->
<display:table name="rendezvouse.announcements" id="announcement" requestURI="rendezvouse/detailed-rendezvous.do" pagesize="10">
	<display:column property="title" title="${titleAnnouncement}"/>
	<display:column property="description" title="${descriptionAnnouncement}"/>
	<display:column property="moment" title="${momentAnnouncement}" format="${formatMoment}"/>
</display:table>	

<!-- Displaying comments -->
<display:table name="rendezvouse.comments" id="comment" requestURI="rendezvouse/detailed-rendezvous.do" pagesize="10">
	<display:column>
		<img src="${comment.pictureUrl}" width="150" height="150">
	</display:column>
	<display:column property="text" title="${textComment}"/>
	<display:column property="moment" title="${momentComment}" format="${formatMoment}"/>
	<display:column>
		<a href="comment/listFromComment.do?commentId=${comment.id}">
			<button class="btn">
				<spring:message code="rendezvous.comment.comments"/>
			</button>
		</a>
	</display:column>
	<display:column title="${displayComment}">
		<a href="comment/display.do?commentId=${comment.id}">
		<button class="btn">
				<spring:message code="rendezvous.comment.display"/>
			</button>
		</a>
	</display:column>
</display:table>	
