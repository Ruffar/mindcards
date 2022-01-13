
$(document).on("click",".favouriteButton",function(event){

    var favouriteButton = $(this); //Store because $(this) will change inside AJAX
    var isFavourited = $(this).hasClass("favourited");
    var icon = $(this).find("i");
    var favNumber = $(this).find(".favouriteNumber");
    var deckId = $(this).parents(".card").find(".cardId").text();

    if (isFavourited) {
        $.ajax({
            type: "POST",
            url: "/unfavouriteDeck",
            data: {
                deckId: deckId
            },
            success: function(response) {
                favouriteButton.removeClass("favourited");
                icon.removeClass("fa-solid");
                icon.addClass("fa-regular");
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
                favouriteButton.addClass("favourited");
                icon.removeClass("fa-regular");
                icon.addClass("fa-solid");
                favNumber.text( (Number(favNumber.text())+1).toString() );
            }
        });
    }
});