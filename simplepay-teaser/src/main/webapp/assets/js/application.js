

function main() {
    
    $('#email-form').submit(function(e) {
       e.preventDefault();
       
        var data = {
            email:$('#email-address').val()
        };
        $.ajax({
          type: 'POST',
          url: "notificationreceiver",
          data: data,
          dataType: 'json'
        }).done(function(data) {
            alert(data.message);
        });
        
    });
}


$(document).ready(main);