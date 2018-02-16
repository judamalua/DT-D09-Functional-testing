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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
<spring:message code="rendezvous.comment.picture" var="pictureComment"/>
<spring:message code="rendezvous.comment.display" var="displayComment"/>
<spring:message code="rendezvous.comment.replies" var="repliesComment"/>
<spring:message code="rendezvous.comment.reply" var="replyComment"/>


<jsp:useBean id="now" class="java.util.Date"/>
<fmt:formatDate var="currentDate" value="${now}" pattern="yyyy-MM-dd HH:mm"/>
<spring:message var="format" code="master.page.moment.format.out"/>
<fmt:formatDate var="formatMomentRendezvous" value="${rendezvous.moment}" pattern="${format}" />
<spring:message code="master.page.moment.format" var="formatMoment"/>


<!-- Display -->
<img src="${rendezvous.pictureUrl}" alt="${rendezvous.name}"/>

<h2><jstl:out value="${rendezvous.name}"/></h2>

<p><jstl:out value="${rendezvous.description}"/></p>
<br/>

<p><jstl:out value="${formatMomentRendezvous}"/></p>

<iframe class="map" src="https://www.google.com/maps/embed/v1/search?q=${rendezvous.gpsCoordinates}&key=AIzaSyBe0wmulZvK1IM3-3jIUgbxt2Ax_QOVW6c"></iframe>
<br/>

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
<br/>

<!-- Displaying announcements -->
<display:table name="rendezvouse.announcements" id="announcement" requestURI="rendezvouse/detailed-rendezvous.do" pagesize="10">
	<display:column property="title" title="${titleAnnouncement}"/>
	<display:column property="description" title="${descriptionAnnouncement}"/>
	<display:column property="moment" title="${momentAnnouncement}" format="${formatMoment}"/>
</display:table>	
<br/>

<!-- Button to create a comment -->
<jstl:if test="${!anonymous && userHasRVSPdRendezvous}">
	<br/>
	<a href="comment/user/create.do?rendezvousId=${rendezvous.id}">
		<button class="btn">
				<spring:message code="rendezvous.comment.create"/>
			</button>
	</a>
</jstl:if>
<br/>

<!-- Displaying comments -->
<display:table name="rendezvous.comments" id="comment" requestURI="rendezvous/detailed-rendezvous.do" pagesize="10">
	<display:column title="${pictureComment}">
		<jstl:if test="${not empty comment.pictureUrl}">
			<img src="${comment.pictureUrl}" width="150" height="150">
		</jstl:if>
	</display:column>
	<display:column property="text" title="${textComment}"/>
	<display:column property="moment" title="${momentComment}" format="${formatMoment}"/>
	<display:column title="${repliesComment}">
		<jstl:if test="${comment.comments != null and fn:length(comment.comments)>0}"> <!-- Comprueba que tenga respuestas -->
			<a href="comment/listFromComment.do?commentId=${comment.id}">
				<button class="btn">
					<spring:message code="rendezvous.comment.comments"/>
				</button>
			</a>
		</jstl:if>
	</display:column>
	<display:column title="${displayComment}">
		<a href="comment/display.do?commentId=${comment.id}">
		<button class="btn">
				<spring:message code="rendezvous.comment.display"/>
			</button>
		</a>
	</display:column>
	
	<jstl:if test="${userHasRVSPdRendezvous}">	
		<display:column title="${replyComment}">
			<a href="comment/user/reply.do?commentId=${comment.id}">
			<button class="btn">
				<spring:message code="rendezvous.comment.reply"/>
			</button>
		</a>
		</display:column>
	</jstl:if>
	
	<security:authorize access="hasRole('ADMIN')"> 
		<display:column>
			<a href="comment/admin/delete.do?commentId=${comment.id}">
				<button class="btn">
					<spring:message code="rendezvous.delete"/>
				</button>
			</a>
		</display:column>
	</security:authorize>
</display:table>	
<br/>

<jstl:if test="${!anonymous && userHasCreatedRendezvous}">
	<br/>
	<a href="question/user/list.do?rendezvousId=${rendezvous.id}">
		<button class="btn">
				<spring:message code="rendezvous.question.list"/>
			</button>
	</a>
</jstl:if>
