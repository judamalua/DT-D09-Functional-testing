<%@page language="java" contentType="title/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<display:table  name="announcements" id="announcement" requestURI="announcement/user/list.do" pagesize="${pagesize}" class="displayTag"> <!-- Rendezvous Id sent by controller -->
	
	<spring:message code="announcement.moment.format" var = "momentFormat"/>
	<spring:message code="announcement.moment" var = "announcementMoment" />
	<display:column property="moment" title="${announcementMoment}" sortable="true" format="${momentFormat}"/>
	
	<spring:message code="announcement.title" var="title"/>
	<display:column property="title" title="${title}" sortable="false"/>
	
	<spring:message code="announcement.description" var="description"/>
	<display:column property="description" title="${description}" sortable="false"/>
	
	<display:column>
		<a href="announcement/user/delete.do?announcementId=${announcement.id}">
			<spring:message code="announcement.delete"/>
		</a>
	</display:column>
	
</display:table>

	

