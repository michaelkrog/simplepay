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
            <div class="brand">simple<span class="blue">pay</span></div>
        </div>
        <div class="modal-body">
            <form id="form-payment" action="<c:url value="/paymentwindow/"/>" method="POST" class="form-horizontal">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="cardno">Kortnummer</label>
                        <div class="controls">
                            <input type="text" class="input-xlarge" id="cardno" name="cardno" autofocus="autofocus" placeholder="f.eks. 4571000000000001">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="month">Udløbsdato</label>
                        <div class="controls">
                            <input type="text" class="input-mini" id="month" name="month" placeholder="mm">&nbsp;<input type="text" class="input-mini" id="year" name="year"  placeholder="&aring;&aring;">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="cardno">Kontrolnummer</label>
                        <div class="controls">
                            <input type="text" class="input-mini" id="cvc" name="cvc" placeholder="f.eks. 123"><input type="submit" class="hidden" height="1"/>
                        </div>
                    </div>
                    
                    
                </fieldset>
            </form>
        </div>
        <div class="modal-footer">
            <!--a href="#" class="btn">Opret ny profil</a-->
            <div id="btn-pay" class="btn btn-primary">Betal</div>
        </div>
    </div>
    <jsp:include page="inc/scripts.jsp" />
    <script>
        
        function handleSubmission() {
            $.ajax({
                url: '/paymentwindow/handle',
                type: "POST",
                data: {
                    token:'${token}', 
                    publicKey: '${publicKey}',
                    amount:${amount}, 
                    currency:'${currency}'
                }
            }).done(function(data) {
                document.location='${returnUrl}';
            }).fail(function(){
                alert("Kunne ikke gennemføre betaling.");
            });
            return false;
        }
        function submitLogin() {
            $('#form-payment').submit();
        }
        
        
        function main() {
            $('#btn-pay').click(submitLogin);
            
            $('#form-payment').submit(handleSubmission);
        }
        
        $(document).ready(main);
        
    </script>

</body>
</html>