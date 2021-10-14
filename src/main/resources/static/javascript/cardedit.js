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
    success: function(data){
      cardDiv.replaceWith(data);
      var desc = cardDiv.find(".cardText").find(".description");
      desc.html("poop");
      if (cardMain) {
        $(document).attr("title",cardTitle)
      }
    }
  });

});

function getCardData(cardElement) {
    var isMain = cardElement[0].classList.contains("card-main");

    var cardType = cardElement.find(".cardType").text();
    var cardId = cardElement.find(".cardId").text();

    var imageId = cardElement.find(".imageId").text();

    var cardText = cardElement.find(".cardtext");
    var titleElement = cardText.find(".title");
    var title = (titleElement != null) ? titleElement.text() : "";
  var description = cardText.find(".description").text();

  return { isMain : isMain, cardType : cardType, cardId : cardId, imageId : imageId, title : title, description : description }
}

function getCardEditorData(cardElement) {
    var isMain = cardElement[0].classList.contains("card-main");

    var cardType = cardElement.find(".cardType").text();
    var cardId = cardElement.find(".cardId").text();

    var imageId = cardElement.find(".imageId").text();

    var cardText = cardElement.find(".cardtext");
    var titleElement = cardText.find(".title");
    var title = (titleElement != null) ? titleElement.val() : "";
  var description = cardText.find(".description").val();

  return { isMain : isMain, cardType : cardType, cardId : cardId, imageId : imageId, title : title, description : description }
}