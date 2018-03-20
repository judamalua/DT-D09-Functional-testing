<%--
 * action-1.jsp
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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<!-- Variables -->

<!-- Form -->
<p>
	<em><spring:message code="form.required.params" /></em>
</p>
<div class="row">
	<form:form action="slider/admin/edit.do" modelAttribute="slider">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<acme:textbox code="slider.title_en" path="title_en" required="true" />
		<acme:textarea code="slider.text_en" path="text_en" required="true" />
		
		<acme:textbox code="slider.title_es" path="title_es" required="true" />
		<acme:textarea code="slider.text_es" path="text_es" required="true" />
		
		<acme:textbox code="slider.pictureUrl" path="pictureUrl" />
		
		
		<div>
			<div class="input-field col s3">
				
				<form:select path="align" id="align">
					<form:option value="left"><spring:message code="slider.align.left"/></form:option>
					<form:option value="center"><spring:message code="slider.align.center"/></form:option>
					<form:option value="right"><spring:message code="slider.align.right"/></form:option>
				</form:select>
				<label for = "align"><spring:message code="slider.align" /></label>
				<form:errors path="align" cssClass="error" />
			</div>
		</div>
		<div class="cleared-div">
			<acme:submit name="save" code="slider.save" />
			<jstl:if test="${slider.id!=0}">
				<acme:delete clickCode="slider.confirm.delete" name="delete"
					code="slider.delete" />
			</jstl:if>
			<acme:cancel url="slider/admin/list.do" code="slider.cancel" />
		</div>
	</form:form>
</div>
