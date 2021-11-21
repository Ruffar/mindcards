
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
                $(this).attr("src","/images/asset/favouriteEmpty.png")
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
                $(this).attr("src","/images/asset/favourite.png")
            }
        });
    }
};)

function favouriteDeck(button,deckId) {
    $.ajax({
        type: "POST",
        url: "/favouriteDeck",
        data: {
            deckId: deckId
        },
        success: function(response) {

        }
    });
}