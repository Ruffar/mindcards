
$(document).on("click",".browserCard .cardBody",function(event){
    window.location.assign($(this).closest(".browserCard").find(".cardLink").text());
});