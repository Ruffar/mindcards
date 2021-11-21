
$("form").submit(function(event){

  event.preventDefault();
  var submission = $(this).serializeArray();

  $.ajax({
    type: "POST",
    url: "/login",
    data: {
      email: submission[0].value,
      password: submission[1].value
    },
    success: function(response) {
      $(errorLabel).text("");
      var urlParams = new URLSearchParams(window.location.search);
      var returnUrl = urlParams.get("return");
      if (returnUrl != null) {
        window.location.assign(returnUrl);
      } else {
        window.location.assign("/home");
      }
    },
    error: function(response) {
      $(errorLabel).text(response.responseJSON.message);
    }
  });

});