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
    <div class="container">

        <jsp:include page="inc/top.jsp" />

        <div id="contentwrapper" class="content">
            <div class="row" style="background: #FAFAFA;border-bottom: 1px solid #DFDFDF;">
                <div class="span6">
                    <ul class="nav nav-pills" style="padding:6px 20px; margin:0px;">
                      <li>
                          <a href="<c:url value="/dashboard"/>">Oversigt</a>
                      </li>
                      <li class="active"><a href="<c:url value="/dashboard/config"/>">Indstillinger</a></li>
                      <li><a href="<c:url value="/dashboard/transactions"/>">Transaktioner</a></li>
                    </ul>
                </div>
                
            </div>    
            <div class="row" style="min-height: 400px">
                <div class="span12">
                    
                </div>
            </div> 


            <jsp:include page="inc/footer.jsp" />
        </div>
    </div>

    <jsp:include page="inc/scripts.jsp" />
    <jsp:include page="inc/scripts_ui.jsp" />
    <jsp:include page="inc/scripts_service.jsp" />
    
    
    <script>
        var firstDayOfWeek = 1;
        var dateFormat = 'dd/mm/yyyy';
        var dateTimeFormat = 'dd/MM/yyyy HH:mm:ss';
        var numberFormat = '#,###.00';
        var locale = "dk";
        
        var service = new Service();
        
        function main() {
            service.transactions.addListener(transactionListener);
        }
            
        $(document).ready(main);
    </script>

</body>
</html>