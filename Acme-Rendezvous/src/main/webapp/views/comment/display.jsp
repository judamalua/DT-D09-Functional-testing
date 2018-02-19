<%--
 * display.jsp
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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:message var="format" code="master.page.moment.format.out"/>
<spring:message code="master.page.moment.format" var="formatMoment"/>
<dl>
	<spring:message code="comment.author" var="commentAuthor"/>
	<dt><jstl:out value="${commentAuthor}"/></dt>
	<dd><a href="user/display.do?userId=${user.id}"><jstl:out value="${user.name}"/></a></dd>
	
	<spring:message code="comment.moment" var="commentMoment"/>
	<dt><fmt:formatDate value="${commentText}" pattern="${format}"/> </dt>
	<dd><fmt:formatDate value="${commentText}" pattern="${format}"/> </dd>
	
	<spring:message code="comment.text" var="commentText"/>
	<dt><</dt>
	<dd><jstl:out value="${comment.text}"/></dd>
	
	<jstl:if test="${not empty comment.pictureUrl}"> <!-- Solo muestra la imagen si esta existe -->
		<spring:message code="comment.pictureUrl" var="commentPictureUrl"/>
		<dt><jstl:out value="${commentPictureUrl}"/></dt>
		<dd><img src="${comment.pictureUrl}" width="300" height="300"/></dd>
	</jstl:if>
	
</dl>
	
	<security:authorize access="hasRole('USER')"> 
		<spring:message code="comment.reply" var="commentReply"/>
		<a href="comment/user/reply.do?commentId=${comment.id}">
			<button class="btn">
				<jstl:out value="${commentReply}"/>
			</button>		
		</a>
	</security:authorize>
	
	<spring:message code="comment.replies" var="commentReplies"/>
	<dt><jstl:out value="${commentReplies}"/></dt>
	
<display:table name="comments" id="row" pagesize="5" class="displaytag" requestURI="${requestURI}">

	<spring:message code="comment.moment.format" var = "momentFormat"/>
	<spring:message code="comment.moment" var = "commentMoment" />
	<display:column property="moment" title="${commentMoment}" sortable="true" format="${momentFormat}"/>
	
	<spring:message code="comment.text" var = "commentText" />
	<display:column property="text" title="${commentText}" sortable="false"/>
	
	<spring:message code="comment.pictureUrl" var = "commentPictureUrl" />
	<display:column title="${commentPictureUrl}" >
		<img src="${row.pictureUrl}" width="150" height="150"/>
	</display:column>
	
	<spring:message code="comment.replies" var = "commentReplies" />
	<display:column title="${commentReplies}" sortable="false">
		<jstl:if test="${row.comments != null and fn:length(row.comments)>0}"> <!-- Comprueba que tenga respuestas -->
			<a href="comment/listFromComment.do?commentId=${row.id}"><spring:message code="comment.view.replies"/></a>
		</jstl:if>
	</display:column>
	
	<display:column title="${displayComment}">
		<a href="comment/display.do?commentId=${row.id}">
		<button class="btn">
				<spring:message code="rendezvous.comment.display"/>
			</button>
		</a>
	</display:column>
	<security:authorize access="hasRole('USER')"> 
	<spring:message code="comment.reply" var = "commentReply" />
	<display:column >
		<a href="comment/user/reply.do?commentId=${row.id}"><jstl:out value="${commentReply}"/></a>
	</display:column>
	</security:authorize>
</display:table>

