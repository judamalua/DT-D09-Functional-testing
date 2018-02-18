<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="rendezvous/user/edit.do" modelAttribute="rendezvous">

	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="questions"/>
	<form:hidden path="announcements"/>
	<form:hidden path="comments"/>
	<form:hidden path="users"/>

	<form:label path="name">
		<spring:message code="rendezvous.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	
	<form:label path="description">
		<spring:message code="rendezvous.description"/>
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	
	<spring:message code="rendezvous.moment.placeholder" var="momentPlaceholder"/>
	<form:label path="moment">
		<spring:message code="rendezvous.moment"/>
	</form:label>
	<form:input path="moment" placeholder="${momentPlaceholder}" />
	<form:errors cssClass="error" path="moment"/>
	
	<form:label path="pictureUrl">
		<spring:message code="rendezvous.pictureUrl"/>
	</form:label>
	<form:input path="pictureUrl"/>
	<form:errors cssClass="error" path="pictureUrl"/>
	
	<spring:message code="rendezvous.gpsCoordinates.placeholder" var="gpsPlaceholder"/>
	<form:label path="gpsCoordinates">
		<spring:message code="rendezvous.gpsCoordinates"/>
	</form:label>
	<form:input path="gpsCoordinates" placeholder="${gpsPlaceholder}"/>
	<form:errors cssClass="error" path="gpsCoordinates"/>
	
	<form:label path="similars">
		<spring:message code="rendezvous.similars"/>
	</form:label>
	<form:select path="similars" multiple="true" >
		<form:option value="0" selected="true" >---------</form:option>
		<jstl:forEach items="${rendezvouses}" var="item">
			<form:option value="${item.id}"><jstl:out value="${item.name}"/></form:option>
		</jstl:forEach>
	</form:select>
	<br/>
	<br/>
	
	<form:label path="finalMode">
		<spring:message code="rendezvous.finalMode"/>
	</form:label>
	<form:checkbox path="finalMode"/>
	<form:errors cssClass="error" path="finalMode"/>
	
	<form:label path="adultOnly">
		<spring:message code="rendezvous.adultOnly"/>
	</form:label>
	<form:checkbox path="adultOnly"/>
	<form:errors cssClass="error" path="adultOnly"/>
	<br/>
	<br/>
	
	
	
	<input type="submit" 
		class="btn"
		name="save"
		value="<spring:message code="rendezvous.save"/>"/>
	<jstl:if test="${rendezvous.id!=0}">
		<input type="submit"
			class="btn"
			name="delete"
			value="<spring:message code="rendezvous.delete"/>"
			onclick="return confirm('<spring:message code="rendezvous.confirm.delete"/>')"/>
	</jstl:if>
	
	<input type="button" 
		class="btn"
		name="cancel"
		value="<spring:message code="rendezvous.cancel"/>"
		onclick="javascript:relativeRedir('rendezvous/user/list.do')"/>

</form:form>
