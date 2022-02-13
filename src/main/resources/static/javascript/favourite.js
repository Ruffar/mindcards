
$(document).on("click",".favouriteButton",function(event){ //Bind function to Click event on any element with favouriteButton class

    var favouriteButton = $(this); //Store because $(this) will change inside AJAX
    var isFavourited = $(this).hasClass("favourited");
    var icon = $(this).find("i");
    var favNumber = $(this).find(".favouriteNumber");
    var deckId = $(this).parents(".card").find(".cardId").text();

    if (isFavourited) {
        $.ajax({
            type: "DELETE",
            url: "/unfavouriteDeck?deckId="+deckId,
            success: function(response) {
                //Change HTML visuals
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
            //Change HTML visuals
                favouriteButton.addClass("favourited");
                icon.removeClass("fa-regular");
                icon.addClass("fa-solid");
                favNumber.text( (Number(favNumber.text())+1).toString() );
            }
        });
    }
});