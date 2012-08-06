






function main() {
    $('#email-address-button').click(function() {
        alert("Du skrev: " + $('#email-address').val());
    });
}


$(document).ready(main);