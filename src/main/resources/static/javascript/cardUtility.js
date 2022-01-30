
//Utility
function getCardType(cardDiv) {
    if (cardDiv.hasClass("mindcard")) {
        return "mindcard";
    } else if (cardDiv.hasClass("infocard")) {
        return "infocard";
    } else if (cardDiv.hasClass("cardGroup")) {
        return "cardGroup";
    } else if (cardDiv.hasClass("deck")) {
        return "deck";
    }
    return "";
}

function updateCardElement(cardDTO, cardDiv) {
  var cardView = cardDiv.find(".cardBody.card-view");

  cardDiv.find(">.cardId").text(cardDTO.primaryKey);

  cardView.find(">.card-text>.description").text(cardDTO.description);
  parseCardDescription(cardView); //Update the unescaped description

  var imgContainer = cardView.find(">.card-decoration>.imgcontainer");
  if (cardDTO.imagePath != "") {
    imgContainer.attr("hidden", false);
    imgContainer.css("background-image","url("+cardDTO.imagePath+")");
  } else {
    imgContainer.attr("hidden", true);
    imgContainer.css("background-image","");
  }

  var title = cardView.find(">.card-text>.title");
  if (title != null && cardDTO.title != null) {
    title.text(cardDTO.title);
  }
}