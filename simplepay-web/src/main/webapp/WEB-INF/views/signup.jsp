<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>SimplePay - Login</title>
        <%@include file="inc/head.jsp" %>
    </head>
    <body style="background:url(./img/background-noise.png)">
        <div class="modal">
            <div class="modal-header">
                <h3>Create Your SimplePay Account!</h3>
            </div>
            <div class="modal-body">
                <form style="margin:0px">
                    <fieldset>
                        <input type="text" name="username" class="input-large" placeholder="Email">
                        <br/>
                        <input type="text" name="password" class="input-large" placeholder="Password">
                        <br/>
                        <input type="text" name="password2" class="input-large" placeholder="Confirm password">
                    </fieldset>
                </form>
            </div>
            <div class="modal-footer">
                <a href="#" class="btn btn-primary pull-left">Create Your SimplePay Account</a>
            </div>
        </div>
    </body>
</html>
