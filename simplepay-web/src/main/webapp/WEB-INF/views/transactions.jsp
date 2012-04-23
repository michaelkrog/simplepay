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
            <h3 id="transaction-title">Ordre: <span id="dialog-ordernumber"></span></h3>
        </div>
        <div class="modal-body">
            <h1 id="dialog-amount">DKK 401,25</h1>
            <h5>Status: <span id="dialog-status">Godkendt</span></h5>
            <h6>Dato: <span id="dialog-timestamp">1/2/2012 14:54:34</span></h6>
            <div id="dialog-description" class="well" style="margin-top:10px">
                Dette er en beskrivelse af det der er blevet godkendt til betaling.

            </div>
        </div>
        <div class="modal-footer">
            <div id="btn-cancelpayment" class="btn btn-danger">Annullér</div>
            <div id="btn-nextstate" class="btn btn-success">Capture</div>
            <a href="#" class="btn btn-primary" data-dismiss="modal">Luk</a>
        </div>
    </div>
        
    <jsp:include page="inc/scripts.jsp" />
    <jsp:include page="inc/scripts_ui.jsp" />
    <jsp:include page="inc/scripts_service.jsp" />
    
    <script id="transactionRowTemplate" type="text/x-jquery-tmpl">
        <tr class="transaction-row" style="cursor:pointer;background:white;" transactionid="\${id}">
            <td>\${orderNumber}</td>
            <td class="hidden-phone">\${$.format.date(new Date(dateCreated), dateTimeFormat)}</td>
            <td class="visible-phone">\${$.format.date(new Date(dateCreated), dateFormat)}</td>
            <td style="text-align: right">\${formatMoney(currency, authorizedAmount / 100)} </td>
            <td class="hidden-phone">\${cardType}</td>
            <td>\${status}</td>
        </tr>
    </script>
    <script>
        var privateKey = '${privateKey}';
        var firstDayOfWeek = 1;
        var dateFormat = 'dd/mm/yyyy';
        var dateTimeFormat = 'dd/MM/yyyy HH:mm:ss';
        var numberFormat = '#,###.00';
        var locale = "dk";
        
        var service = new Service(privateKey);
        var transactionListener = { onUpdate: updateRow };
        
        var chosenDate = null;
        var lastRetrievedData = null;
        var selectedTransaction = null;
 
        function advanceTransactionState() {
            if(selectedTransaction.status == 'Authorized') {
                service.transactions.charge(selectedTransaction.id, null, function(transaction){
                    updateDialog(transaction);
                });
            }
            
            if(selectedTransaction.status == 'Captured') {
                service.transactions.refund(selectedTransaction.id, null, function(transaction){
                    updateDialog(transaction);
                });
            }
        }
 
        function formatMoney(currency, number) {
            return $.formatNumber(number, {format:currency + " "+numberFormat, locale:locale});
        }
        
        function updateDialog(transaction) {
            $('#dialog-amount').text(formatMoney(transaction.currency,transaction.authorizedAmount/100));
            $('#dialog-status').text(transaction.status);
            $('#dialog-ordernumber').text(transaction.orderNumber);
            $('#dialog-description').text(transaction.description);
            $('#dialog-timestamp').text($.format.date(new Date(transaction.dateCreated), dateTimeFormat));
            
            var newTransaction = transaction.status == "New";
            var cancelable = newTransaction || transaction.status == "Authorized";
            var changeable = !newTransaction && transaction.status != "Refunded";
            var nextStateText;
            
            switch(transaction.status) {
                case 'Authorized':
                    nextStateText = 'Charge';
                    break;
                case 'Captured':
                    nextStateText = 'Refund';
                    break;
            }
            
            //Update visibility of cancel button
            $('#btn-cancelpayment').css('display', (cancelable ? '':'none'));
            
            //Update visibility of next state button
            $('#btn-nextstate').css('display', (changeable ? '':'none'));
            
            //Update text on advance state button
            $('#btn-nextstate').text(nextStateText);                
                            
        }
        
        function updateRow(transaction) {
            selectedTransaction = transaction;
            $('tr[transactionid="'+transaction.id+'"]').replaceWith($( "#transactionRowTemplate" ).tmpl( transaction ));
        }
        
        function updateData() {
        
            var beforeDate = null;
            var afterDate = null;
            var status = null;
        
            var data = {};
            var searchString = $.trim($('#searchstring').val());
            
            if(searchString != '') {
                data.searchString = searchString;
            }
            
            if($('#status').val() != 'All') {
                status = $('#status').val();
            }
            
            if(chosenDate != null) {
                if($('#datemode').val() == 'before') {
                    beforeDate = chosenDate;
                } else {
                    afterDate = chosenDate;
                }
            }
            
            $("#transactions-tbody").empty();
            service.transactions.list($('#searchstring').val(), beforeDate, afterDate, status, function(data) {
                lastRetrievedData = data;
                $( "#transactionRowTemplate" ).tmpl( data ).appendTo( "#transactions-tbody" );
                $('.transaction-row').click(function(evt) {
                    var id = evt.currentTarget.attributes.transactionid.value;
                    $.each(lastRetrievedData, function(index, value) {
                        if(value.id == id) {
                            selectedTransaction = value;
                            updateDialog(value);
                            $('#transactionModal').modal('show');
                        }
                    });
                    
                });
            });
        }
        
        function main() {
            service.transactions.addListener(transactionListener);
            $('.searchfield').change(updateData);
            $('#btn-nextstate').click(advanceTransactionState);
            
            $('.datepicker').datepicker({format:dateFormat, weekStart:firstDayOfWeek});
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