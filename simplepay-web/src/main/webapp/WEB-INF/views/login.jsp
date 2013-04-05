<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
    <head>
        <title><spring:message code="general.system_name"/> - Login</title>
        <%@include file="inc/head.jsp" %>
    </head>
    <body style="background:url(./img/background-noise.png)">
        <div class="modal">
            <div class="modal-header">
                <h3 style="text-align:center;"><spring:message code="login.welcome_back"/></h3>
            </div>
            <form style="margin:0px" class="form-horizontal" action="<c:url value="/j_spring_security_check"/>" method="POST">
                <c:set var="error" value="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}"/>
                <div class="modal-body">
                    <c:if test="${not empty error}">
                        <div class="alert alert-error">
                            <spring:message code="login.login_failed"/>:<br /> ${error}
                        </div>
                    </c:if>

                    <div class="control-group">
                        <label class="control-label" for="inputEmail"><spring:message code="general.email"/></label>
                        <div class="controls">
                            <input type="text" name="j_username" class="input-large" placeholder="Email">
                        </div>
                    </div>                    <div class="control-group">
                        <label class="control-label" for="inputEmail"><spring:message code="general.password"/></label>
                        <div class="controls">
                            <input type="password" name="j_password" class="input-large" placeholder="Password">
                        </div>
                    </div>

                    <div class="control-group">
                        <div class="controls">
                            <label class="checkbox">
                                <input type="checkbox"> <spring:message code="login.remember_me"/>
                            </label>

                        </div>
                    </div>

                </div>
                <div class="modal-footer">

                    <a href="<c:url value="signup"/>" class="btn pull-left"><spring:message code="login.sign_up"/></a>
                    <button type="submit" class="btn btn-primary"><spring:message code="login.sign_in"/></button>
                </div>
            </form>
        </div>
    </body>
</html>
