
//This script expects markdown.js to have been loaded before this script

//Utility
function getCardType(cardDiv) {
    if (cardDiv.hasClass("mindcard")) {
        return "mindcard";
    } else if (cardDiv.hasClass("infocard")) {
        return "infocard";
    } else if (cardDiv.hasClass("cardgroup")) {
        return "cardgroup";
    } else if (cardDiv.hasClass("deck")) {
        return "deck";
    }
    return "";
}

//Editing Cards
$(document).on("click",".editCardButton",function(event){

    var cardDiv = $(this).closest(".card");
    var cardType = getCardType(cardDiv);

    var cardEditorTemp = $("#cardTemplates").find(".editorCard."+cardType);
    cardDiv.append(cardEditorTemp.html());
    //Match the editor's data with current viewer ones in the case of a cancelled edit
    var cardView = cardDiv.find(".card-view");
    var cardEditor = cardDiv.find(".card-editor");

    cardEditor.find("[name='cardId']").val(cardView.find(".cardId").text());
    cardEditor.find(".description").val(cardView.find(".description").text());

    if (cardView.find(".title") != null) {
        cardEditor.find(".title").val(cardView.find(".title").text());
    }

    //Toggle the visibility of the elements
    cardView.attr("hidden",true);
    //cardEditor.attr("hidden",false);

});

$(document).on("click",".cancelEditCardButton",function(event){

    event.preventDefault(); //Prevents the form from automatically doing some action
    var cardDiv = $(this).closest(".card");
    var cardType = getCardType(cardDiv);

    //var cardDisplayTemp = $("cardTemplates").find(".cardDisplay."+cardType);
    //cardDiv.html(cardDisplayTemp);

    //Toggle the visibility of the elements
    cardDiv.find(".card-view").attr("hidden",false);
    cardDiv.find(".card-editor").remove();

});

$(document).on("click",".deleteCardButton",function(event){

    event.preventDefault();
    var cardDiv = $(this).closest(".card");
    var form = $(this).closest(".card-editor");
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
    var form = cardDiv.find(".card-editor");
    var cardData = new FormData(form[0]);

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

$(document).on("click",".addInfocard",function(event){

    event.preventDefault();

    var cardDiv = $(this);
    var cardId = $(".card-main").find(".cardId").text();

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

    var parentId = 0;
    if ($(this).find(".cardId")) {
        parentId = $(this).find(".cardId").text();
    }
    var cardType = getCardType($(this));

    $.ajax({
        type: "POST",
        url: "/addCard",
        data: {cardType: cardType, parentCardId: cardId},

        success: function(response) {
            window.location.assign("/"+cardType+"/"+response.primaryKey);
        },
        error: function(response) {
            console.log(response.responseJSON.message);
        }
    });

});

//Utility
function updateCardElement(cardDTO, cardDiv) {
  var cardView = cardDiv.find(".card-view");

  cardView.find(".cardId").text(cardDTO.primaryKey);

  cardView.find(".description").text(cardDTO.description);
  parseCardDescription(cardView); //Update the unescaped description

  var imgContainer = cardView.find(".imgcontainer");
  if (cardDTO.imagePath != "") {
    imgContainer.attr("hidden", false);
    imgContainer.css("background-image","url("+cardDTO.imagePath+")");
  } else {
    imgContainer.attr("hidden", true);
    imgContainer.css("background-image","");
  }

  var title = cardView.find(".title");
  if (title != null && cardDTO.title != null) {
    title.text(cardDTO.title);
  }
}

function getCardElement(cardType, cardId) {
  return $.ajax({
    type: "GET",
    url: "/getCardElement",
    data: {cardType: cardType, cardId: cardId},

    success: function(response) {

    },
    error: function(response) {
      console.log(response);
    }
  });
}

function getFormData(form) {
  var rawData = form.serializeArray();
  var outData = {};

  $.map(rawData, function(rawElement, i) {
    outData[rawElement["name"]] = rawElement["value"];
  });

  return outData;
}