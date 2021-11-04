
//This script expects markdown.js to have been loaded before this script

//Editing Cards
$(document).on("click",".editCardButton",function(event){

  var cardDiv = $(this).parents(".card");

  //Match the editor's data with current viewer ones in the case of a cancelled edit
  var cardView = cardDiv.find(".card-view");
  var cardEditor = cardDiv.find(".card-editor");

  cardEditor.find(".description").val(cardView.find(".description").text());

  if (cardView.find(".title") != null) {
    cardEditor.find(".title").val(cardView.find(".title").text());
  }

  //Toggle the visibility of the elements
  cardView.attr("hidden",true);
  cardEditor.attr("hidden",false);

});

$(document).on("click",".cancelEditCardButton",function(event){

  event.preventDefault(); //Prevents the form from automatically doing some action
  var cardDiv = $(this).parents(".card");

  //Toggle the visibility of the elements
  cardDiv.find(".card-view").attr("hidden",false);
  cardDiv.find(".card-editor").attr("hidden",true);

});

$(document).on("click",".deleteCardButton",function(event){

  event.preventDefault();
  var cardDiv = $(this).parents(".card");
  var form = cardDiv.find(".card-editor");
  var cardData = new FormData(form[0]);

  var cardMain = cardDiv.hasClass("card-main");

  $.ajax({
    type: "POST",
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
  var cardDiv = $(this).parents(".card");
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
      cardDiv.find(".card-editor").attr("hidden",true);
    },
    error: function(response) {
      cardDiv.find(".errorLabel").text(response.responseJSON.message);
    }
  });

});

//Adding cards
$(document).on("click",".card-add",function(event){

  var cardDiv = $(this).parents(".card");

  //Match the editor's data with current viewer ones in the case of a cancelled edit
  var cardEditor = cardDiv.find(".card-editor");
  cardEditor.find(".description").val("");
  if (cardEditor.find(".title") != null) {
    cardEditor.find(".title").val("");
  }

  cardDiv.find(".card-add").attr("hidden",true);
  cardDiv.find(".card-editor").attr("hidden",false);

});

$(document).on("click",".cancelAddCardButton",function(event){

  event.preventDefault();
  var cardDiv = $(this).parents(".card");

  cardDiv.find(".card-add").attr("hidden",false);
  cardDiv.find(".card-editor").attr("hidden",true);

});

$(document).on("click",".confirmAddCardButton",function(event){

  event.preventDefault();
  var cardDiv = $(this).parents(".card");
  var form = cardDiv.find(".card-editor")
  var cardData = new FormData(form[0]);

  $.ajax({
    type: "POST",
    url: "/addCard",
    data: cardData,
    cache: false,
    contentType: false,
    processData: false,
    success: function(response) {
      getCardElement(response.card.cardType,response.card.primaryKey).done(function(replacement){
        cardDiv.before(replacement);
        cardDiv.find(".card-view").attr("hidden",false);
        cardDiv.find(".card-editor").attr("hidden",true);
      });
    },
    error: function(response) {
      cardDiv.find(".errorLabel").text(response.responseJSON.message);
    }
  });

});

//Utility
function updateCardElement(cardDTO, cardDiv) {
  var cardView = cardDiv.find(".card-view");
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