
//This script expects markdown.js and cardUtility.js to have been loaded before this script

//Editing Cards
$(document).on("click",".editCardButton",function(event){

    var cardDiv = $(this).closest(".card");
    var cardType = getCardType(cardDiv);

    var cardEditorTemp = $("#cardTemplates").find(".editorCard."+cardType);
    cardDiv.append(cardEditorTemp.html());
    //Match the editor's data with current viewer ones in the case of a cancelled edit
    var cardView = cardDiv.find(">.cardBody.card-view");
    var cardViewButtons = cardDiv.find(">.cornerButtons.card-view");
    var cardEditor = cardDiv.find(">.cardBody.card-editor");

    cardEditor.find("[name='cardId']").val(cardDiv.find(">.cardId").text());
    cardEditor.find(">.card-text>.description").val(cardView.find(">.card-text>.description").text());

    if (cardView.find(">.card-text>.title") != null) {
        cardEditor.find(">.card-text>.title").val(cardView.find(">.title").text());
    }

    //Toggle the visibility of the elements
    cardView.attr("hidden",true);
    cardViewButtons.attr("hidden",true);

});

$(document).on("click",".cancelEditCardButton",function(event){

    event.preventDefault(); //Prevents the form from automatically doing some action
    var cardDiv = $(this).closest(".card");
    var cardType = getCardType(cardDiv);

    //Toggle the visibility of the elements
    cardDiv.find(">.card-view").attr("hidden",false);
    cardDiv.find(">.card-editor").remove();

});

$(document).on("click",".deleteCardButton",function(event){

    event.preventDefault();
    var cardDiv = $(this).closest(".card");
    var form = cardDiv.find(".cardBody.card-editor");
    var cardData = new FormData(form[0]);

    var cardMain = cardDiv.hasClass("card-main");

    $.ajax({
        type: "DELETE",
        url: "/deleteCard",
        data: cardData,
        cache: false,
        contentType: false,
        processData: false,
        success: function(response) {
            cardDiv.remove();
            if (cardMain) {
                $("parentButton").click();
            }
        },
        error: function(response) {
            cardDiv.find(".errorLabel").text(response.responseJSON.message);
        }
    });

});

$(document).on("click",".saveCardButton",function(event){

    event.preventDefault();
    var cardDiv = $(this).closest(".card");
    var form = cardDiv.find(">.cardBody.card-editor");
    var cardData = new FormData(form[0]); //Convert all form fields into a dictionary

    var imageChange = cardData["imageChange"];
    if (imageChange == "upload") {
        if (cardData["imageUrl"]) delete cardData["imageUrl"];
    } else if (imageChange == "url") {
        if (cardData["imageFile"]) delete cardData["imageFile"];
    } else {
        if (cardData["imageFile"]) delete cardData["imageFile"];
        if (cardData["imageUrl"]) delete cardData["imageUrl"];
    }

    var cardMain = cardDiv.hasClass("card-main");
    var cardTitle = cardData.get("title");

    $.ajax({
        type: "POST",
        url: "/saveCard",
        data: cardData,
        cache: false,
        contentType: false,
        processData: false,
        success: function(response) {
            updateCardElement(response,cardDiv);

            if (cardMain) { //Is this the page's main card?
                $(document).attr("title",cardTitle); //Change title of the page
            }

            cardDiv.find(".card-view").attr("hidden",false);
            cardDiv.find(".card-editor").remove();
        },
        error: function(response) {
            cardDiv.find(".errorLabel").text(response.responseJSON.message);
        }
    });

});

//Images
$(document).on("change",".imageSelect",function(event){

    var cardDecor = $(this).closest(".card-decoration");
    var imageChange = $(this).val();
    var fileUploader = cardDecor.find("[name='imageFile']");
    var urlField = cardDecor.find("[name='imageUrl']")

    if (imageChange == "upload") {
        fileUploader.attr("hidden", false);
        urlField.attr("hidden", true)
    } else if (imageChange == "url") {
        fileUploader.attr("hidden", true);
        urlField.attr("hidden", false)
    } else {
        fileUploader.attr("hidden", true);
        urlField.attr("hidden", true)
    }
})

$(document).on("submit",".imgcontainer",function(event){
    event.preventDefault(); //prevent submission of form by pressing Enter on input
});

//Adding cards
$(document).on("click",".infocardAdder",function(event){

    event.preventDefault();

    var cardDiv = $(this);
    var cardId = $(".card-main").find(">.cardId").text();

    $.ajax({
        type: "POST",
        url: "/addCard",
        data: {cardType: "infocard", parentCardId: cardId},

        success: function(response) {
            var newDiv = $("#cardTemplates").find(".displayCard.infocard").clone();
            newDiv.insertBefore(cardDiv);
            updateCardElement(response,newDiv);
        },
        error: function(response) {
            cardDiv.find(".errorLabel").text(response.responseJSON.message);
        }
    });

});

$(document).on("click",".mainCardAdder",function(event){

    event.preventDefault();

    var cardData = {};
    var parentIdElement = $(this).find(".cardId");
    if (parentIdElement != null && parentIdElement.text() != "") {
        cardData.parentCardId = parentIdElement.text();
    }
    cardData.cardType = getCardType($(this));

    $.ajax({
        type: "POST",
        url: "/addCard",
        data: cardData,

        success: function(response) {
            window.location.assign("/"+cardData.cardType+"/"+response.primaryKey);
        }
    });

});

//Adding/removing Group Mindcards
$(document).on("click",".removeFromGroupButton",function(event){

    var cardDiv = $(this).closest(".card");
    var mindcardId = cardDiv.find(">.cardId").text();

    var cardGroupId = $(".maincontainer .card-main>.cardId").text();

    $.ajax({
        type: "DELETE",
        url: "/removeGroupMindcard",
        data: {"mindcardId": mindcardId, "cardGroupId": cardGroupId},

        success: function(response) {
            cardDiv.remove();
        }
    });

});

$(document).on("click",".groupMindcardAdder",function(event){

    var popup = $(".groupMindcardPopup");
    popup.attr("hidden",false);

});

$(document).on("click",".groupMindcardPopup .closeButton",function(event){

    var popup = $(".groupMindcardPopup");
    popup.attr("hidden",true);

});

$(document).on("submit",".groupMindcardPopup .popupSearch",function(event){

    event.preventDefault();

    var popup = $(".groupMindcardPopup");
    var popupCardGrid = popup.find(".card-grid")
    popupCardGrid.html(""); //Clear current cards inside
    var deckId = popup.find(">.deckId").text();
    var searchString = $(this).find(".searchBar").val();

    $.ajax({
        type: "GET",
        url: "/searchDeckMindcards",
        data: {deckId: deckId, search: searchString},

        success: function(response) {
            //Add each received mindcard onto the popup
            response.forEach(function(mindcard, i) {
                var newDiv = $("#cardTemplates").find(".adderBrowserCard.mindcard").clone();
                newDiv.appendTo(popupCardGrid);
                updateCardElement(mindcard,newDiv);
            });
        }
    });

});

$(document).on("click",".groupMindcardPopup .adderBrowserCard",function(event){

    var popup = $(".groupMindcardPopup");
    var cardGroupId = popup.find(">.cardGroupId").text();

    var mindcardId = $(this).find(">.cardId").text();

    $.ajax({
        type: "POST",
        url: "/addGroupMindcard",
        data: {mindcardId: mindcardId, cardGroupId: cardGroupId},
    });

    popup.attr("hidden",true);

});
