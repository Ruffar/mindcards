
$(document).on("click",".browserCard .cardBody",function(event){
    window.location.assign($(this).closest(".browserCard").find(".cardLink").text());
});

$(document).on("click",".favouriteButton",function(event){

    var isFavourited = $(this).hasClass("favourited");
    var cardDiv = $(this).parents(".card");
    var deckId = cardDiv.find(".cardId").text();

    if (isFavourited) {
        $.ajax({
            type: "DELETE",
            url: "/unfavouriteDeck",
            data: {
                deckId: deckId
            },
            success: function(response) {
                $(this).removeClass("favourited");
                var icon = $(this).find("i");
                icon.removeClass("fas");
                icon.addClass("far");
            }
        });
    } else {
        $.ajax({
            type: "POST",
            url: "/favouriteDeck",
            data: {
                deckId: deckId
            },
            success: function(response) {
                $(this).addClass("favourited");
                var icon = $(this).find("i");
                icon.removeClass("far");
                icon.addClass("fas");
            }
        });
    }
});