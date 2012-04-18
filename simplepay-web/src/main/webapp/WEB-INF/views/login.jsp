<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html lang="da">
    <head>
        <meta charset="utf-8">
        <title></title>
        <jsp:include page="inc/head.jsp" />
    </head>  

    <div id="loginModal" class="modal" style="display: block; ">
        <div class="modal-header">
            <!--a class="close" data-dismiss="modal">×</a-->
            <h3>Log ind</h3>
        </div>
        <div class="modal-body">
            <form id="form-login" action="<c:url value="/j_spring_security_check"/>" method="POST" class="form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="username">Brugernavn</label>
                        <div class="controls">
                            <input type="text" class="input-xlarge" id="username" name="j_username" autofocus="autofocus">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="password">Password</label>
                        <div class="controls">
                            <input type="password" class="input-xlarge" id="password" name="j_password"><input type="submit" class="hidden" height="1"/>
                        </div>
                    </div>

                </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <!--a href="#" class="btn">Opret ny profil</a-->
            <div id="btn-login" class="btn btn-primary">Log ind</div>
        </div>
    </div>
    <jsp:include page="inc/scripts.jsp" />
    <script>
        
        function submitLogin() {
            $('#form-login').submit();
        }
        
        function main() {
            $('#btn-login').click(submitLogin);
        }
        
        $(document).ready(main);
        
    </script>

</body>
</html>