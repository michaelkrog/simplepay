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

    <div id="payModal" class="modal" style="display: block; ">
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
            <a id="btn-cancel" href="#" class="btn">Fortryd</a>
            <div id="btn-pay" class="btn btn-primary">Betal</div>
        </div>
    </div>
    <jsp:include page="inc/scripts.jsp" />
    <script>
        /*@RequestParam String tokenId, @RequestParam String orderNumber, @RequestParam String publicKey, 
                                        @RequestParam long amount, @RequestParam String currency, String cardNumber, String cvc, int expireMonth, int expireYear*/
        function handleSubmission() {
            $.ajax({
                url: '/paymentwindow/handle',
                type: "POST",
                data: {
                    token:'${token}',
                    orderNumber:'',
                    publicKey: '${publicKey}',
                    amount:${amount}, 
                    currency:'${currency}',
                    cardNumber:'4571000000000000',
                    cvc:'323',
                    expireMonth:2,
                    expireYear:2016
                }
            }).done(function(data) {
                document.location='${returnUrl}';
            }).fail(function(){
                alert("Kunne ikke gennemføre betaling.");
            });
            return false;
        }
        
        function submitPayment() {
            $('#form-payment').submit();
        }
        
        function cancelPayment() {
            document.location = '${cancelUrl}';
        }
        
        function main() {
            $('#btn-pay').click(submitPayment);
            $('#btn-cancel').click(cancelPayment);
            
            $('#form-payment').submit(handleSubmission);
        }
        
        $(document).ready(main);
        
    </script>

</body>
</html>