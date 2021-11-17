package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.InvalidCardTypeException;
import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.card.DeckElement;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.service.CardElementService;
import com.raffier.mindcards.service.CardUpdateService;
import com.raffier.mindcards.service.CardType;
import com.raffier.mindcards.service.CardUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@SessionAttributes("user")
public class CardController {

    @Autowired
    CardUpdateService cardUpdateService;
    @Autowired
    CardElementService cardElementService;
    @Autowired
    CardUtilityService cardUtilityService;

    @GetMapping(value="deck/{deckId}")
    public ModelAndView deckView(@ModelAttribute User user, @PathVariable int deckId, ModelAndView mv) {
        mv.setViewName("cards/deck");

        if (!cardUtilityService.canUserAccess(CardType.DECK,user,deckId)) {
            throw new UnauthorisedAccessException("This Deck is private!");
        }

        DeckElement deck = cardElementService.getDeckElement( user, deckId );

        mv.addObject("deck",deck);

        return mv;
    }

    @GetMapping(value="group/{cardGroupId}")
    public ModelAndView groupView(@ModelAttribute User user, @PathVariable int cardGroupId, ModelAndView mv) {
        mv.setViewName("cards/group");

        if (!cardUtilityService.canUserAccess(CardType.CARDGROUP,user,cardGroupId)) {
            throw new UnauthorisedAccessException("This Card Group is private!");
        }

        CardElement<CardGroup> group = cardElementService.getCardGroupElement( user, cardGroupId);

        CardElement<Deck> deck = cardElementService.getDeckElement( user, group.getCard().getDeckId() );

        mv.addObject("deck",deck);
        mv.addObject("group",group);

        return mv;
    }

    @GetMapping(value="mindcard/{mindcardId}")
    public ModelAndView mindcardView(@ModelAttribute User user, @PathVariable int mindcardId, ModelAndView mv) {
        mv.setViewName("cards/mindcard");

        if (!cardUtilityService.canUserAccess(CardType.MINDCARD,user,mindcardId)) {
            throw new UnauthorisedAccessException("This Mindcard is private!");
        }

        CardElement<Mindcard> mindcard = cardElementService.getMindcardElement( user, mindcardId );
        List<CardElement<Infocard>> infoList = cardElementService.getInfocardsFromMindcard( user, mindcardId );
        CardElement<Deck> deck = cardElementService.getDeckElement( user, mindcard.getCard().getDeckId() );

        mv.addObject("deck",deck);
        mv.addObject("mindcard",mindcard);
        mv.addObject("infocards",infoList);

        return mv;
    }

    @PostMapping("saveCard")
    public ResponseEntity<CardElement<?>> saveCard(@ModelAttribute User user, @RequestParam String cardType, @RequestParam int cardId, @RequestParam MultipartFile image, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        boolean isOwner = cardUtilityService.isUserCardOwner(cardTypeEnum, user, cardId);

        CardElement<?> cardElement;
        if (isOwner && cardUpdateService.areHyperlinksValid(description)) {
            switch (cardTypeEnum) {
                case MINDCARD:
                    cardUpdateService.updateMindcard(cardId, title, image, description);
                    cardElement = cardElementService.getMindcardElement( user, cardId );
                    break;
                case INFOCARD:
                    cardUpdateService.updateInfocard(cardId, image, description);
                    cardElement = cardElementService.getInfocardElement( user, cardId );
                    break;
                default:
                    throw new InvalidCardTypeException(cardTypeEnum);
            }
        } else {
            throw new UnauthorisedAccessException();
        }

        return new ResponseEntity<>(cardElement, HttpStatus.ACCEPTED);
    }

    @PostMapping("deleteCard")
    public ResponseEntity<?> deleteCard(@ModelAttribute User user, @RequestParam String cardType, @RequestParam int cardId, Model model) {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        boolean isOwner = cardUtilityService.isUserCardOwner(cardTypeEnum, user, cardId);

        if (isOwner) {
            switch (cardTypeEnum) {
                case MINDCARD:
                    cardUpdateService.deleteMindcard(cardId);
                    break;
                case INFOCARD:
                    cardUpdateService.deleteInfocard(cardId);
                    break;
                case CARDGROUP:
                    cardUpdateService.deleteCardGroup(cardId);
                    break;
                case DECK:
                    cardUpdateService.deleteDeck(cardId);
                    break;
                default:
                    throw new InvalidCardTypeException(cardTypeEnum);
            }
        } else {
            throw new UnauthorisedAccessException();
        }

        return ResponseEntity.ok(null);
    }

    @PostMapping("addCard")
    public ResponseEntity<CardElement<?>> addCard(@ModelAttribute User user, @RequestParam String cardType, @RequestParam int parentCardId, @RequestParam MultipartFile image, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        boolean isOwner;
        if (cardTypeEnum == CardType.DECK) {
            isOwner = true;
        } else {
            CardType parentType = cardTypeEnum.getParentType();
            isOwner = cardUtilityService.isUserCardOwner(parentType, user, parentCardId);
        }

        CardElement<?> cardElement;
        if (isOwner && cardUpdateService.areHyperlinksValid(description)) {
            switch (cardTypeEnum) {
                case MINDCARD:
                    cardElement = cardElementService.getMindcardElement(
                            user, cardUpdateService.addMindcard(parentCardId, title, image, description).getPrimaryKey()
                    );
                    break;
                case INFOCARD:
                    cardElement = cardElementService.getInfocardElement(
                            user, cardUpdateService.addInfocard(parentCardId, image, description).getPrimaryKey()
                    );
                    break;
                case CARDGROUP:
                    cardElement = cardElementService.getCardGroupElement(
                            user, cardUpdateService.addCardGroup(parentCardId, title, image, description).getPrimaryKey()
                    );
                    break;
                case DECK:
                    cardElement = cardElementService.getDeckElement(
                            user, cardUpdateService.addDeck(user.getUserId(), title, image, description).getPrimaryKey()
                    );
                    break;
                default:
                    throw new InvalidCardTypeException(cardTypeEnum);
            }
        } else {
            throw new UnauthorisedAccessException();
        }

        return new ResponseEntity<>(cardElement, HttpStatus.ACCEPTED);
    }

    @GetMapping("getCardElement")
    public String getCardElement(@ModelAttribute User user, @RequestParam String cardType, @RequestParam int cardId, Model model) {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        boolean isOwner = cardUtilityService.isUserCardOwner(cardTypeEnum, user, cardId);

        CardElement<?> cardElement;
        switch(cardTypeEnum) {
            case MINDCARD:
                cardElement = cardElementService.getMindcardElement( user, cardId );
                break;
            case INFOCARD:
                cardElement = cardElementService.getInfocardElement( user, cardId );
                break;
            default:
                throw new InvalidCardTypeException(cardTypeEnum);
        }

        model.addAttribute("cardElement",cardElement);
        model.addAttribute("cardType",cardType.toLowerCase());

        return "fragments/card :: card(cardElement=${cardElement},cardType=${cardType})";
    }

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }

}
