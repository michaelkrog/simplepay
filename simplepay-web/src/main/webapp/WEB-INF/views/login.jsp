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
                <h3 style="text-align:center;">Welcome Back!</h3>
            </div>
            <div class="modal-body">
                <form style="margin:0px" class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label" for="inputEmail">Email</label>
                        <div class="controls">
                            <input type="text" name="j_username" class="input-large" placeholder="Email">
                        </div>
                    </div>                    <div class="control-group">
                        <label class="control-label" for="inputEmail">Password</label>
                        <div class="controls">
                            <input type="text" name="j_password" class="input-large" placeholder="Password">
                        </div>
                    </div>

                    <div class="control-group">
                        <div class="controls">
                            <label class="checkbox">
                                <input type="checkbox"> Remember me
                            </label>

                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

                <a href="<c:url value="signup"/>" class="btn pull-left">No account? Sign up!</a>
                <a href="#" class="btn btn-primary">Sign In!</a>
            </div>
        </div>
    </body>
</html>
