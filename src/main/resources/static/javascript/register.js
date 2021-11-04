
$("form").submit(function(event){

  event.preventDefault();
  var submission = $(this).serializeArray();

  $.ajax({
    type: "POST",
    url: "/register",
    data: {
      username: submission[0].value,
      email: submission[1].value,
      password: submission[2].value
    },
    success: function(response) {
      $(errorLabel).text("");
      var urlParams = new URLSearchParams(window.location.search);
      var returnUrl = urlParams.get("return");
      if (returnUrl != null) {
        window.location.replace(returnUrl);
      } else {
        window.location.replace("/home");
      }
    },
    error: function(response) {
      $(errorLabel).text(response.responseJSON.message);
    }
  });

});