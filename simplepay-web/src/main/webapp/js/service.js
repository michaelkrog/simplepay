
function TransactionCrud(key) {
    this.key = key;
    
    this.list = function(search, dateBefore, dateAfter, status, callback) {
        var data = {};
        var search = (search == null ? "" : $.trim(search));
            
        if(search != '') {
            data.searchString = search;
        }
            
        if(status != null && status != '') {
            data.status = $('#status').val();
        }
            
        if(dateBefore != null) {
            data.beforeTimestamp = dateBefore;
        }
        
        if(dateAfter != null) {
            data.afterTimestamp = dateAfter;
        }

        $.ajax({
            url: '/api/transactions',
            data: data,
            username:this.key
        }).done(callback(data));
    }
    
    this.read = function(id, callback) {
        $.ajax({
            url: '/api/transactions'+id,
            username:this.key
        }).done(callback(data));
    }
    
    this.charge = function(id, amount, callback) {
        $.ajax({
            url: '/api/transactions/'+id+'/charge' + (amount != null ? '?amount='+amount : ''),
            type:'POST',
            username:this.key
        }).done(function(data) {
            callback(data);
        });
    }
    
    this.refund = function(id, amount, callback) {
        $.ajax({
            url: '/api/transactions/'+id+'/refund' + (amount != null ? '?amount='+amount : ''),
            type:'POST',
            username:this.key
        }).done(function(data) {
            callback(data);
        });
    }
    
    this.cancel = function(id, callback) {
        $.ajax({
            url: '/api/transactions/'+id+'/cancel',
            type:'POST',
            username:this.key
        }).done(function(data) {
            callback(data);
        });
    }
}

function Service(key) {
    this.key = key;
    
    this.transactions = new TransactionCrud(key);
    
}


