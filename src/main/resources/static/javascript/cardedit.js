$(document).on("click",".editCardButton",function(event){

    var cardDiv = $(this).parents(".card");
    var cardData = getCardData(cardDiv);

    $.ajax({
    type: "GET",
    url: "/getCardEditor",
    data: cardData,
    success: function(data){
      cardDiv.replaceWith(data);
    }
  });

});

$(document).on("click",".saveCardButton",function(event){

  var cardDiv = $(this).parents(".card");
  var cardData = getCardEditorData(cardDiv);

  var cardMain = cardData.isMain;
  var cardTitle = cardData.title;

  $.ajax({
    type: "POST",
    url: "/saveCard",
    data: cardData,
    success: function(data) {
      cardDiv.replaceWith(data);
      if (cardMain) {
        $(document).attr("title",cardTitle)
      }
    },
    error: function() {
      getCardElement(cardDiv, cardMain, cardData.cardType, cardData.cardId);
    }
  });

});

$(document).on("click",".cancelEditCardButton",function(event){

  var cardDiv = $(this).parents(".card");
  var cardData = getCardEditorData(cardDiv);
  getCardElement(cardDiv, cardData.isMain, cardData.cardType, cardData.cardId);

});

function getCardElement(cardDiv, isMain, cardType, cardId) {
  $.ajax({
    type: "GET",
    url: "/getCardElement",
    data: {isMain: isMain, cardType: cardType, cardId: cardId},

    success: function(data) {
      cardDiv.replaceWith(data);
      if (isMain) {
        $(document).attr("title",cardTitle)
      }
    }
  });
}

function getCardData(cardElement) {
  var classList = cardElement[0].classList;
  var isMain = classList.contains("card-main");

  var cardType = getCardTypeFromList(classList);
  var cardId = cardElement.find(".cardId").text();

  var imageId = cardElement.find(".imageId").text();

  var cardText = cardElement.find(".cardtext");
  var titleElement = cardText.find(".title");
  var title = (titleElement != null) ? titleElement.text() : "";
  var description = cardText.find(".description").text();

  return { isMain : isMain, cardType : cardType, cardId : cardId, imageId : imageId, title : title, description : description }
}

function getCardEditorData(cardElement) {
  var classList = cardElement[0].classList;
  var isMain = classList.contains("card-main");

  var cardType = getCardTypeFromList(classList);
  var cardId = cardElement.find(".cardId").text();

  var imageId = cardElement.find(".imageId").text();

  var cardText = cardElement.find(".cardtext");
  var titleElement = cardText.find(".title");
  var title = (titleElement != null) ? titleElement.val() : "";
  var description = cardText.find(".description").val();

  return { isMain : isMain, cardType : cardType, cardId : cardId, imageId : imageId, title : title, description : description }
}

function getCardTypeFromList(classList) {
  var cardType = "none";
  if (classList.contains("infocard")) {
    cardType = "infocard";
  } else if (classList.contains("mindcard")) {
    cardType = "mindcard";
  } else if (classList.contains("cardgroup")) {
    cardType = "cardgroup";
  } else if (classList.contains("cardpack")) {
    cardType = "cardpack";
  }
  return cardType;
}