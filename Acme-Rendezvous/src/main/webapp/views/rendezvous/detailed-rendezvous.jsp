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


<!-- Variable declaration -->
<spring:message code="master.page.moment.format" var="formatMoment" />
<spring:message code="rendezvous.name" var="titleName" />
<spring:message code="rendezvous.description" var="titleDescription" />
<spring:message code="rendezvous.moment" var="titleMoment" />
<spring:message code="rendezvous.announcement.title"
	var="titleAnnouncement" />
<spring:message code="rendezvous.announcement.description"
	var="descriptionAnnouncement" />
<spring:message code="rendezvous.announcement.moment"
	var="momentAnnouncement" />
<spring:message code="rendezvous.comment.text" var="textComment" />
<spring:message code="rendezvous.comment.moment" var="momentComment" />
<spring:message code="rendezvous.comment.picture" var="pictureComment" />
<spring:message code="rendezvous.comment.display" var="displayComment" />
<spring:message code="rendezvous.comment.replies" var="repliesComment" />
<spring:message code="rendezvous.comment.reply" var="replyComment" />


<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />
<spring:message var="format" code="master.page.moment.format.out" />
<fmt:formatDate var="formatMomentRendezvous"
	value="${rendezvous.moment}" pattern="${format}" />
<spring:message code="master.page.moment.format" var="formatMoment" />


<!-- Display -->
<jstl:if test="${rendezvous.pictureUrl != \"\"}">
	<div class="parallax-container">
		<div class="parallax">
			<img src="${rendezvous.pictureUrl}">
		</div>
	</div>
</jstl:if>
<h2>
	<jstl:out value="${rendezvous.name}" />
</h2>
<br>
<h4>
	<spring:message code="rendezvous.description" />
</h4>
<p>
	<jstl:out value="${rendezvous.description}" />
</p>
<br />

<h4>
	<spring:message code="rendezvous.moment" />
</h4>
<p>
	<jstl:out value="${formatMomentRendezvous}" />
</p>

<h4>
	<spring:message code="rendezvous.map" />
</h4>
<iframe class="map"
	src="https://www.google.com/maps/embed/v1/search?q=${rendezvous.gpsCoordinates}&key=AIzaSyBe0wmulZvK1IM3-3jIUgbxt2Ax_QOVW6c"></iframe>
<br />

<!-- Button for joining the rendezvous -->
<security:authorize access="hasRole('USER')">
	<jstl:if
		test="${!userHasRVSPdRendezvous && !anonymous && rendezvous.moment > currentDate}">
		<a href="answer/user/edit.do?rendezvousId=${rendezvous.id}">
			<button class="btn">
				<spring:message code="rendezvous.join" />
			</button>
		</a>
	</jstl:if>
	<jstl:if
		test="${userHasRVSPdRendezvous && !userHasCreatedRendezvous && !anonymous}">

		<a href="answer/user/delete.do?rendezvousId=${rendezvous.id}">
			<button class="btn"
				onclick="javascript:confirm('<spring:message code="rendezvous.leave.confirm"/>')">
				<spring:message code="rendezvous.leave" />
			</button>
		</a>
	</jstl:if>
	<br />
</security:authorize>
<!-- Displaying similar rendezvouses -->
<h4>
	<spring:message code="rendezvous.similar.list" />
</h4>
<display:table name="${rendezvous.similars}" id="similar"
	requestURI="rendezvous/detailed-rendezvous.do" pagesize="${pagesize}">
	<display:column title="${titleName}" sortable="true">
		<a
			href="rendezvous/detailed-rendezvous.do?rendezvousId=${similar.id}&anonymous=${anonymous}"><jstl:out
				value="${similar.name}" /></a>
	</display:column>
	<display:column property="description" title="${titleDescription}" />
	<display:column property="moment" title="${titleMoment}"
		format="${formatMoment}" sortable="true" />

	<display:column>
		<jstl:if test="${similar.adultOnly}">
			<i class="material-icons">do_not_disturb</i>
			<spring:message code="rendezvous.adultOnly" />
		</jstl:if>
	</display:column>
</display:table>
<br />

<!-- Displaying announcements -->
<h4>
	<spring:message code="rendezvous.announcements.list" />
</h4>
<display:table name="${rendezvous.announcements}" id="announcement"
	requestURI="rendezvous/detailed-rendezvous.do" pagesize="${pagesize}">
	<display:column property="title" title="${titleAnnouncement}"
		sortable="true" />
	<display:column property="description"
		title="${descriptionAnnouncement}" />
	<display:column property="moment" title="${momentAnnouncement}"
		format="${formatMoment}" sortable="true" />
	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<acme:button url="announcement/admin/delete.do?announcementId=${announcement.id}" code="announcement.delete"/>
		</display:column>
	</security:authorize>
</display:table>
<br />

<!-- Comments -->
<h4>
	<spring:message code="rendezvous.comment.list" />
</h4>
<!-- Button to create a comment -->
<jstl:if
	test="${!anonymous && userHasRVSPdRendezvous and !rendezvous.deleted}">
	<br />
	<acme:button url="comment/user/create.do?rendezvousId=${rendezvous.id}"
		code="rendezvous.comment.create" />
	<br />
</jstl:if>

<!-- Displaying comments -->
<jstl:forEach var="comment" items="${rendezvous.comments}">
	<acme:showComment comment="${comment}"
		canUserComment="${userHasRVSPdRendezvous}" indent="0"
		anonymous="${anonymous}" rendezvousId="${rendezvous.id}" />
</jstl:forEach>
<jstl:if test="${fn:length(rendezvous.comments) == 0}">
	<spring:message code="rendezvous.comments.empty" />
</jstl:if>
<br />

<!-- Link to attendants -->
<jstl:if test="${rendezvous.finalMode and !rendezvous.deleted}">
	<acme:button url="answer/list.do?rendezvousId=${rendezvous.id}" code="rendezvous.answer.list"/>
</jstl:if>
<security:authorize access="hasRole('USER')">
	<jstl:if test="${userHasCreatedRendezvous and !rendezvous.deleted}">
		<br />
		<!-- Link to questions -->
		<acme:button url="question/user/list.do?rendezvousId=${rendezvous.id}" code="rendezvous.question.list"/>

		<!-- Create a new announcement -->
		<br />
		<jstl:if test="${rendezvous.moment>=currentDate}">
			<acme:button
				url="announcement/user/create.do?rendezvousId=${rendezvous.id}"
				code="rendezvous.announcement.create" />
		</jstl:if>
	</jstl:if>
</security:authorize>
