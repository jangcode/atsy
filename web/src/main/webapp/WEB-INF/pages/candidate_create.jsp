<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="atsy" tagdir="/WEB-INF/tags" %>
<%@page contentType="text/html;charset=UTF-8" %>
<spring:url value="/secure/positions" var="positions"/>
<spring:url value="/secure/channels" var="channels"/>
<spring:url value="/secure/welcome" var="welcome"/>
<spring:url value="/secure/candidate" var="candidateURL"/>

<atsy:secure_page>
    <jsp:attribute name="pageJs">
        <script src="<c:url value="/resources/thirdparty/bootstrap-validator/validator.js" />" type="text/javascript"></script>
        <script src="<c:url value="/resources/js/atsy-candidate-create.js" />"></script>
    </jsp:attribute>
    <jsp:body>
        <div id="candidate_creation" class="<c:if test="${not empty candidate.candidateId}">display</c:if>">
            <h1 class="page-header">
                <c:choose>
                    <c:when test="${not empty candidate.candidateId}">
                        <p class="showValue">
                            <spring:message code="candidate.show.title" arguments="<small>${candidate.name}</small>"
                                            htmlEscape="false"/>
                        </p>

                        <p class="edit">
                            <spring:message code="candidate.edit.title" arguments="<small>${candidate.name}</small>"
                                            htmlEscape="false"/>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <spring:message code="candidate.create.title"/>
                    </c:otherwise>
                </c:choose>
            </h1>

            <div id="candidate_data">
                <div class="row">
                    <form class="form" role="form" method="POST" id="candidate-create-form" action="${candidateURL}">
                        <div class="globalMessage alert alert-danger" role="alert"
                             style="display: none">
                            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                            <span class="error-message"></span>
                        </div>
                        <div class="form-group"
                             id="nameDiv">
                            <input type="hidden" name="candidateId" id="candidateId" value="${candidate.candidateId}">
                            <spring:message code="candidate.name.field" var="i18nname"/>
                            <label class="control-label col-lg-2 col-md-2 col-sm-2 text-right"
                                   for="name"><spring:message
                                    code="candidate.name.label"/></label>

                            <div class="col-lg-4 col-md-4 col-sm-4">
                                <input type="text" class="input form-control " name="name" id="name"
                                       value="${candidate.name}"
                                       placeholder="${i18nname}" required maxlength="100">

                                <p class="showValue form-control-static">${candidate.name}</p>
                            </div>

                        </div>
                        <div class="form-group"
                             id="placeDiv">
                            <spring:message code="candidate.place.field" var="i18nplace"/>
                            <label class="control-label col-lg-2 col-md-2 col-sm-2 text-right"
                                   for="referer"><spring:message
                                    code="candidate.place.label"/></label>

                            <div class="col-lg-4 col-md-4 col-sm-4">
                                <input type="text" class="input form-control" name="referer" id="referer"
                                       value="${candidate.referer}"
                                       placeholder="${i18nplace}" maxlength="20">

                                <p class="showValue form-control-static">${candidate.referer}</p>
                            </div>

                        </div>
                        <div class="form-group"
                             id="emailDiv">
                            <spring:message code="candidate.email.field" var="i18nemail"/>
                            <label class="control-label col-lg-2 col-md-2 col-sm-2 text-right"
                                   for="email"><spring:message
                                    code="candidate.email.label"/></label>

                            <div class="col-lg-4 col-md-4 col-sm-4">
                                <input type="text" class="input form-control" name="email" id="email"
                                       value="${candidate.email}"
                                       placeholder="${i18nemail}" required maxlength="400">

                                <p class="showValue form-control-static">
                                    <a href="mailto:tesztelek01@te.com"><span class="glyphicon glyphicon-envelope"
                                                                              title="E-mail küldése"></span></a>
                                        ${candidate.email}</p>
                            </div>

                        </div>
                        <div class="form-group"
                             id="englishDiv">
                            <label class="control-label col-lg-2 col-md-2 col-sm-2 text-right"
                                   for="drop"><spring:message
                                    code="candidate.english.label"/></label>

                            <div class="selectContainer col-lg-4 col-md-4 col-sm-4" id="drop">
                                <select class="input form-control" name="languageSkill" id="languageSkill">
                                    <option value=0 <c:if
                                            test="${0 eq candidate.languageSkill}"> selected="selected" </c:if>>
                                        <spring:message code="candidate.english.level.default"/></option>
                                    <c:forEach begin="1" end="10" step="1" var="index">
                                        <option value="${index}" <c:if
                                                test="${index eq candidate.languageSkill}"> selected="selected" </c:if>>${index}</option>
                                    </c:forEach>
                                </select>

                                <p class="showValue form-control-static">${candidate.languageSkill}</p>
                            </div>
                        </div>
                        <div class="form-group"
                             id="phoneDiv">
                            <spring:message code="candidate.phone.field" var="i18nphone"/>
                            <label class="control-label col-lg-2 col-md-2 col-sm-2 text-right"
                                   for="phone"><spring:message
                                    code="candidate.phone.label"/></label>

                            <div class="col-lg-4 col-md-4 col-sm-4">
                                <input type="text" class="input form-control" name="phone" id="phone"
                                       value="${candidate.phone}"
                                       placeholder="${i18nphone}" pattern="^\+?[0-9]+" maxlength="20">

                                <p class="showValue form-control-static">${candidate.phone}</p>
                            </div>

                        </div>
                        <div class="form-group"
                             id="descriptionDiv">
                            <div id="fix" class="col-md-6 col-sm-6 col-lg-6"></div>
                            <spring:message code="candidate.description.field" var="i18ndescription"/>
                            <label class="control-label col-lg-2 col-md-2 col-sm-2 text-right"
                                   for="description"><spring:message
                                    code="candidate.description.label"/></label>

                            <div class="col-lg-10 col-md-10 col-sm-10">
                                <textarea rows="4" cols="4" class="input form-control" id="description"
                                          placeholder="${i18ndescription}">${candidate.description}</textarea>

                                <p class="showValue form-control-static">${candidate.description}</p>
                            </div>

                        </div>
                        <div class="text-right col-lg-12 col-md-12 col-sm-12">
                            <a class="btn btn-default showValue" href="${welcome}" id="cancelButton"><spring:message
                                    code="back.button"/></a>
                            <button class="btn btn-primary showValue" id="enableModify"><spring:message
                                    code="candidate.modify.button"/></button>
                            <c:choose>
                                <c:when test="${not empty candidate.candidateId}">
                                    <button class="btn btn-danger " id="cancelButtonModify" type="reset"><spring:message
                                            code="cancel.button"/></button>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-danger" href="${welcome}" id="cancelButtonAdd"><spring:message
                                            code="cancel.button"/></a>
                                </c:otherwise>
                            </c:choose>
                            <button type="submit" class="btn btn-success">
                                <spring:message code="save.button"/>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</atsy:secure_page>