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
                <div class="span4">
                    <h1 style="padding:20px;">Transaktioner</h1>
                </div>
                <div class="span8">
                    <form class="form-inline pull-right" style="padding:20px;">
                        <select class="input-small"><option>Før</option><option selected="true">Efter</option></select>
                        <input type="text" class="input-small datepicker" value="02-16-2012">
                        &nbsp;|&nbsp;
                        <input type="text" class="input-medium datepicker" placeholder="Søgeord">
                        &nbsp;|&nbsp;
                        <select class="input-small">
                            <option>Alle</option>
                            <option>Authorized</option>
                            <option>Captured</option>
                            <option>Cancelled</option>
                            <option>Failed</option>
                        </select>
                    </form>
                </div>
            </div>    
            <div class="row">
                <div class="span12">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Ordernummer</th>
                                <th class="hidden-phone">Oprettet</th>
                                <th class="visible-phone">Oprettet</th>
                                <th>Beløb</th>
                                <th class="hidden-phone">Type</th>
                                <th>Status</th>
                            </tr>
                        <tbody>
                            <c:forEach var="transaction" items="${transactions}">
                                <tr class="transaction-row" style="cursor:pointer;">
                                    <td>${transaction.orderNumber}</td>
                                    <td class="hidden-phone"><fmt:formatDate value="${transaction.dateCreated}" type="both" timeStyle="medium" dateStyle="short" /></td>
                                    <td class="visible-phone"><fmt:formatDate value="${transaction.dateCreated}" type="date" dateStyle="short" /></td>
                                    <td><fmt:formatNumber value='${1.0 * transaction.authorizedAmount / 100}' currencyCode="${transaction.currency}" type='currency'/></td>
                                    <td class="hidden-phone">${transaction.cardType}</td>
                                    <td>${transaction.status}</td>
                                </tr>
                            </c:forEach>
                            </thead>
                    </table>

                </div>
            </div> 


            <jsp:include page="inc/footer.jsp" />
        </div>
    </div>

    <div id="transactionModal" class="modal hide fade" style="display: block; ">
        <div class="modal-header">
            <a class="close" data-dismiss="modal">×</a>
            <h3 id="transaction-title">Ordrenummer</h3>
        </div>
        <div class="modal-body">
            
        </div>
        <div class="modal-footer">
            <div id="btn-pay" class="btn">Capture</div>
            <a href="#" class="btn btn-primary" data-dismiss="modal">Luk</a>
        </div>
    </div>
    <jsp:include page="inc/scripts.jsp" />
    <script>
        //var privateKey = '${privateKey}';    
        function main() {
            $('.transaction-row').click(function() {
                $('#transactionModal').modal('show');
            });
            
            $.ajax({
              url: '/api/transactions'
            }).done(function(data) {
                alert(data);
            });
        }
            
        $(document).ready(main);
    </script>

</body>
</html>