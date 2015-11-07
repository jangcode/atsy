<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@taglib prefix="atsy" tagdir="/WEB-INF/tags" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@attribute name="pageJs" fragment="true" %>
<spring:url value="/resources/img/epam-logo-navbar.png" var="logo"/>
<spring:url value="/login" var="login"/>
<spring:url value="/secure/settings" var="settings"/>
<atsy:page>
    <jsp:attribute name="refs">
         <jsp:invoke fragment="pageJs"/>
    </jsp:attribute>
    <jsp:attribute name="header">
     <nav class="navbar navbar-default navbar-fixed-top">
         <div class="container-fluid">
             <div class="navbar-header">
             <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#atsy-navbar-collapse-1" aria-expanded="false">
                                     <span class="sr-only">Toggle navigation</span>
                                     <span class="icon-bar"></span>
                                     <span class="icon-bar"></span>
                                     <span class="icon-bar"></span>
                                 </button>
                 <a class="navbar-brand" href="#">
                     <img alt="Brand" src="${logo}" class="img-rounded">
                 </a>
             </div>
             <div class="collapse navbar-collapse" id="atsy-navbar-collapse-1">
                 <ul class="nav navbar-nav">
                     <li><a href="#"><span class="glyphicon glyphicon-home" aria-hidden="true"/><span class="visible-xs-inline"><spring:message code="header.menu.home"/></span></a></li>
                     <li><a href="${settings}"><span class="glyphicon glyphicon-cog" aria-hidden="true"/><span class="visible-xs-inline"><spring:message code="header.menu.settings"/></span></a></li>
                 </ul>
                 <ul class="nav navbar-nav navbar-right">
                     <li><a href="${login}"><span class="glyphicon glyphicon-log-out" aria-hidden="true"/><span class="visible-xs-inline"><spring:message code="header.menu.logout"/></span></a></li>
                 </ul>
             </div>
         </div>
     </nav>
    </jsp:attribute>
    <jsp:body>
        <jsp:doBody/>
    </jsp:body>
</atsy:page>