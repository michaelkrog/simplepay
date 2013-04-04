<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title><spring:message code="general.system_name"/> - Login</title>
        <%@include file="inc/head.jsp" %>
    </head>
    <body style="background:url(./img/background-noise.png)">
        <div class="modal">
            <div class="modal-header">
                <h3 style="text-align:center">Create Your SimplePay Account!</h3>
            </div>
            <div class="modal-body">
                <form style="margin:0px" class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label" for="inputEmail"><spring:message code="general.email"/></label>
                        <div class="controls">
                            <input type="text" name="username" class="input-large" placeholder="Email">
                        </div>
                    </div>                    <div class="control-group">
                        <label class="control-label" for="inputEmail"><spring:message code="general.password"/></label>
                        <div class="controls">
                            <input type="text" name="password" class="input-large" placeholder="Password">
                        </div>
                    </div><div class="control-group">
                        <label class="control-label" for="inputEmail"><spring:message code="general.confirm"/></label>
                        <div class="controls">
                            <input type="text" name="password" class="input-large" placeholder="Confirm password">
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-primary">Create Your SimplePay Account</a>
            </div>
        </div>
    </body>
</html>
