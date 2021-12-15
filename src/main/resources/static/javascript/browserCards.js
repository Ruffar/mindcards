
$(document).on("click",".browserCard .cardBody",function(event){
    window.location.assign($(this).closest(".browserCard").find(".cardLink").text());
});

$(document).on("click",".favouriteButton",function(event){

    var isFavourited = $(this).hasClass("favourited");
    var icon = $(this).find("i");
    var favNumber = $(this).find(".favouriteNumber");
    var cardDiv = $(this).parents(".card");
    var deckId = cardDiv.find(".cardId").text();

    if (isFavourited) {
        $.ajax({
            type: "POST",
            url: "/unfavouriteDeck",
            data: {
                deckId: deckId
            },
            success: function(response) {
                $(this).removeClass("favourited");
                icon.removeClass("fas");
                icon.addClass("far");
                favNumber.text( (Number(favNumber.text())-1).toString() );
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
                icon.removeClass("far");
                icon.addClass("fas");
                favNumber.text( (Number(favNumber.text())+1).toString() );
            }
        });
    }
});