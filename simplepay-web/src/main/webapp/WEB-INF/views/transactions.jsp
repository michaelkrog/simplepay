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
                        <select class="searchfield input-small"><option>Før</option><option selected="true">Efter</option></select>
                        <input type="text" class="searchfield input-small datepicker" value="02-16-2012">
                        &nbsp;|&nbsp;
                        <input id="searchstring" type="text" class="searchfield input-medium datepicker" placeholder="Søgeord">
                        &nbsp;|&nbsp;
                        <select id="status" class="searchfield input-small">
                            <option>Alle</option>
                            <option>Authorized</option>
                            <option>Captured</option>
                            <option>Cancelled</option>
                            <option>Failed</option>
                        </select>
                    </form>
                </div>
            </div>    
            <div class="row" style="min-height: 400px">
                <div class="span12">
                    <table class="table">
                        <thead>
                            <tr>
                                <th width="25%">Ordernummer</th>
                                <th width="25%" class="hidden-phone">Oprettet</th>
                                <th width="25%" class="visible-phone">Oprettet</th>
                                <th width="15%" style="text-align: right">Beløb</th>
                                <th width="15%" class="hidden-phone">Type</th>
                                <th width="20%">Status</th>
                            </tr>
                        </thead>
                        <tbody id="transactions-tbody"/>
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
    <script id="transactionRowTemplate" type="text/x-jquery-tmpl">
        <tr class="transaction-row" style="cursor:pointer;">
            <td>\${orderNumber}</td>
            <td class="hidden-phone">\${dateCreated}</td>
            <td class="visible-phone">\${dateCreated}</td>
            <td style="text-align: right">\${1.0 * authorizedAmount / 100}</td>
            <td class="hidden-phone">\${cardType}</td>
            <td>\${status}</td>
        </tr>
    </script>
    <script>
        var privateKey = '${privateKey}';
        
        function updateData() {
            $("#transactions-tbody").empty();
            $.ajax({
              url: '/api/transactions',
              data: {searchString:$('#searchstring').val(), status:$('#status').val()},
              username:privateKey
            }).done(function(data) {
                $( "#transactionRowTemplate" ).tmpl( data ).appendTo( "#transactions-tbody" );
            });
        }
        
        function main() {
            $('.transaction-row').click(function() {
                $('#transactionModal').modal('show');
            });
            
            $('.searchfield').change(function(){
                updateData();
            });
            
        }
            
        $(document).ready(main);
    </script>

</body>
</html>