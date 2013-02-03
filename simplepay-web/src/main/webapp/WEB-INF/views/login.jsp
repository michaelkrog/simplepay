<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>SimplePay - Login</title>
        <%@include file="inc/head.jsp" %>
    </head>
    <body style="background:url(./img/background-noise.png)">
        <div class="modal">
            <div class="modal-header">
                <h3>Welcome Back!</h3>
            </div>
            <div class="modal-body">
                <form style="margin:0px">
                    <fieldset>
                        <input type="text" name="j_username" class="input-large" placeholder="Email">
                        <br/>
                        <input type="text" name="j_password" class="input-large" placeholder="Password">
                    </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-primary pull-left">Sign In!</a>
                <a href="<c:url value="signup"/>" class="btn pull-right">No account? Sign up!</a>
            </div>
        </div>
    </body>
</html>
