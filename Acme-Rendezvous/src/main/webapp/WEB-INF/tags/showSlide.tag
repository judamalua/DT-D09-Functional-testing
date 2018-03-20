
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

<%@ attribute name="title_en" required="true" %>
<%@ attribute name="title_es" required="true" %>

<%@ attribute name="text_en" required="true" %>
<%@ attribute name="text_es" required="true" %>

<%@ attribute name="pictureUrl" required="true" %>
<%@ attribute name="align" required="true" %>

<%@ attribute name="lang" required="true" %>

<%-- Definition --%>
<jstl:if test="${lang eq 'es'}">
      <li>
        <img src="${pictureUrl}">
        <div class="caption ${align}-align">
          <h3 class = "slider-text">${title_es}</h3>
          <h5 class="light grey-text text-lighten-3 slider-text">${text_es}</h5>
        </div>
      </li>
</jstl:if>

<jstl:if test="${lang eq 'en'}">
      <li>
        <img src="${pictureUrl}">
        <div class="caption ${align}-align">
          <h3 class = "slider-text">${title_en}</h3>
          <h5 class="light grey-text text-lighten-3 slider-text">${text_en}</h5>
        </div>
      </li>
</jstl:if>