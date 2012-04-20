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
                        <select id="datemode" class="searchfield input-small"><option value="before">Før</option><option selected="true" value="after">Efter</option></select>
                        <input id="datepicker" type="text" class="searchfield input-small datepicker" placeholder="Dato">
                        &nbsp;|&nbsp;
                        <input id="searchstring" type="text" class="searchfield input-medium datepicker" placeholder="Søgeord">
                        &nbsp;|&nbsp;
                        <select id="status" class="searchfield input-small">
                            <option value="All">Alle</option>
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
                                <th width="25%">Ordrenummer</th>
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
            <h3 id="transaction-title">Ordre: 12312121213</h3>
        </div>
        <div class="modal-body">
            <h1>DKK 401,25</h1>
            <h6>Status:Godkendt</h6>
            <h6>Dato:1/2/2012 14:54:34</h6>
            <div class="well" style="margin-top:10px">
                        Dette er en beskrivelse af det der er blevet godkendt til betaling.

            </div>
        </div>
        <div class="modal-footer">
            <div id="btn-pay" class="btn btn-danger">Annullér</div>
            <div id="btn-pay" class="btn btn-success">Capture</div>
            <a href="#" class="btn btn-primary" data-dismiss="modal">Luk</a>
        </div>
    </div>
    <jsp:include page="inc/scripts.jsp" />
    <script id="transactionRowTemplate" type="text/x-jquery-tmpl">
        <tr class="transaction-row" style="cursor:pointer;">
            <td>\${orderNumber}</td>
            <td class="hidden-phone">\${$.format.date(new Date(dateCreated), "dd/MM/yyyy HH:mm:ss")}</td>
            <td class="visible-phone">\${$.format.date(new Date(dateCreated), "dd/MM/yyyy")}</td>
            <td style="text-align: right">\${formatMoney(currency, authorizedAmount / 100)} </td>
            <td class="hidden-phone">\${cardType}</td>
            <td>\${status}</td>
        </tr>
    </script>
    <script>
        var privateKey = '${privateKey}';
        var chosenDate = null;
        var numberFormat = {format:"#,###.00", locale:"dk"};
        
        function formatMoney(currency, number) {
            return $.formatNumber(number, {format:currency + " #,###.00", locale:"dk"});
        }
        
        function updateData() {
            var data = {};
            var searchString = $.trim($('#searchstring').val());
            
            if(searchString != '') {
                data.searchString = searchString;
            }
            
            if($('#status').val() != 'All') {
                data.status = $('#status').val();
            }
            
            if(chosenDate != null) {
                if($('#datemode').val() == 'before') {
                    data.beforeTimestamp = chosenDate.getTime()
                } else {
                    data.afterTimestamp = chosenDate.getTime();
                }
            }
            
            
            $("#transactions-tbody").empty();
            $.ajax({
              url: '/api/transactions',
              data: data,
              username:privateKey
            }).done(function(data) {
                $( "#transactionRowTemplate" ).tmpl( data ).appendTo( "#transactions-tbody" );
                $('.transaction-row').click(function() {
                    $('#transactionModal').modal('show');
                });
            });
        }
        
        function main() {
            
            $('.searchfield').change(function(){
                updateData();
            });
            
            $('.datepicker').datepicker({format:'dd/mm/yyyy', weekStart:1});
            $('#datepicker').on('changeDate', function(ev){
                chosenDate = ev.date;
                $('#datepicker').datepicker('hide');
                updateData();
              });
            
            updateData();
        }
            
        $(document).ready(main);
    </script>

</body>
</html>