<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="da">
  <head>
    <meta charset="utf-8">
    <title></title>
    <jsp:include page="inc/head.jsp" />
  </head>  
    <div class="container">
            
            <jsp:include page="inc/top.jsp" />
            
            <div id="contentwrapper" class="content">
                 
                <div id="splash" class="row">
                    <div id="splash1" class="splash hidden-phone hidden-tablet">
                        <div class="span6">
                            <div style="margin:40px 0px 0px 40px;">
                            <h1>Betaling direkte på din hjemmeside</h1>
                            <h3 style="margin:20px 0px;color:#6f6f6f;">SimplePay gør det nemt for dig at modtage betalinger på din hjemmeside - også uden programmering!</h3>
                            <span class="btn btn-large btn-primary">Kom igang med SimplePay</span>
                            </div>
                        </div>
                        <div class="span6" align="center">
                            <img src="img/splash1.jpg">
                        </div>

                    </div>
                    <div  class="splash hidden-desktop">
                        <div class="span12" style="margin:40px 0px 0px 40px;">
                            <h1>Betaling direkte på din hjemmeside</h1>
                            <h3 style="margin:20px 0px;color:#6f6f6f;">SimplePay gør det nemt for dig at modtage betalinger på din hjemmeside - også uden programmering!</h3>
                            <span class="btn btn-large btn-primary">Kom igang med SimplePay</span>
                        </div>
                    </div>
                    <div id="splash2" class="span12 splash hidden"></div>
                    <div id="splash3" class="span12 splash hidden"></div>
                    <div id="splash4" class="span12 splash hidden"></div>
                </div>

                <div id="teaserwrapper1" class="row teaserwrapper">
                    <div id="teaser1" class="span6">
                        <div style="margin:40px 0px 0px 40px;">
                            <h3>Nemt at implementere</h3>
                            <pre class="prettyprint">
$('#paybutton').click(function(){
    //Disable button while handling payment
    $(this).attr('disabled','disabled');
    Pay.setPublicKey('<public key>');
    Pay.createUnauthorizedToken(10028, 'An amazing order.', function(token) {
        Pay.authorizeTokenRemote(token, 1000, 'DKK', 'http://localhost:8080/ok.html', 'http://localhost:8080/cancel.html');
    });
}); 
                            </pre>
                        </div>    
                    </div>
                    <div id="teaser2" class="span6">This is a teaser</div>
                </div>

                <div id="teaserwrapper2" class="row teaserwrapper">
                    <div id="teaser3" class="span6">This is a teaser</div>
                    <div id="teaser4" class="span6">This is a teaser</div>
                </div>
                
                <div id="teaserwrapper3" class="row teaserwrapper">
                    <div id="teaser5" class="span6">This is a teaser</div>
                    <div id="teaser6" class="span6">This is a teaser</div>
                </div>
                
                <jsp:include page="inc/footer.jsp" />
            </div>
	</div>
        <jsp:include page="inc/scripts.jsp" />
        
</body>
</html>