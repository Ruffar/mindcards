package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.InvalidCardTypeException;
import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.service.CardUpdateService;
import com.raffier.mindcards.service.CardType;
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

    @GetMapping(value="deck/{deckId}")
    public ModelAndView deckView(@ModelAttribute("user") User user, @PathVariable int deckId, ModelAndView mv) {
        mv.setViewName("cards/deck");

        CardElement<Deck> deck = cardUpdateService.getDeckElement( deckId );

        boolean isOwner = cardUpdateService.isUserCardOwner(CardType.DECK,user,deckId);
        mv.addObject("isOwner",isOwner);

        mv.addObject("deck",deck);

        return mv;
    }

    @GetMapping(value="group/{cardGroupId}")
    public ModelAndView groupView(@ModelAttribute("user") User user, @PathVariable int cardGroupId, ModelAndView mv) {
        mv.setViewName("cards/group");

        CardElement<CardGroup> group = cardUpdateService.getCardGroupElement(cardGroupId);

        CardElement<Deck> deck = cardUpdateService.getDeckElement( group.getCard().getDeckId() );

        boolean isOwner = cardUpdateService.isUserCardOwner(CardType.CARDGROUP,user,cardGroupId);
        mv.addObject("isOwner",isOwner);

        mv.addObject("deck",deck);
        mv.addObject("group",group);

        return mv;
    }

    @GetMapping(value="mindcard/{mindcardId}")
    public ModelAndView mindcardView(@ModelAttribute("user") User user, @PathVariable int mindcardId, ModelAndView mv) {
        mv.setViewName("cards/mindcard");

        CardElement<Mindcard> mindcard = cardUpdateService.getMindcardElement(mindcardId);
        List<CardElement<Infocard>> infoList = cardUpdateService.getInfocardsFromMindcard(mindcardId);
        CardElement<Deck> deck = cardUpdateService.getDeckElement( mindcard.getCard().getDeckId() );

        boolean isOwner = cardUpdateService.isUserCardOwner(CardType.MINDCARD,user,mindcardId);
        mv.addObject("isOwner",isOwner);

        mv.addObject("deck",deck);
        mv.addObject("mindcard",mindcard);
        mv.addObject("infocards",infoList);

        return mv;
    }

    @PostMapping("saveCard")
    public ResponseEntity<CardElement<?>> saveCard(@ModelAttribute User user, @RequestParam String cardType, @RequestParam int cardId, @RequestParam MultipartFile image, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        boolean isOwner = cardUpdateService.isUserCardOwner(cardTypeEnum, user, cardId);

        CardElement<?> cardElement;
        if (isOwner && cardUpdateService.areHyperlinksValid(description)) {
            switch (cardTypeEnum) {
                case MINDCARD:
                    cardUpdateService.updateMindcard(cardId, title, image, description);
                    cardElement = cardUpdateService.getMindcardElement(cardId);
                    break;
                case INFOCARD:
                    cardUpdateService.updateInfocard(cardId, image, description);
                    cardElement = cardUpdateService.getInfocardElement(cardId);
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
        boolean isOwner = cardUpdateService.isUserCardOwner(cardTypeEnum, user, cardId);

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
            isOwner = cardUpdateService.isUserCardOwner(parentType, user, parentCardId);
        }

        CardElement<?> cardElement;
        if (isOwner && cardUpdateService.areHyperlinksValid(description)) {
            switch (cardTypeEnum) {
                case MINDCARD:
                    cardElement = cardUpdateService.getMindcardElement(
                            cardUpdateService.addMindcard(parentCardId, title, image, description).getPrimaryKey()
                    );
                    break;
                case INFOCARD:
                    cardElement = cardUpdateService.getInfocardElement(
                            cardUpdateService.addInfocard(parentCardId, image, description).getPrimaryKey()
                    );
                    break;
                case CARDGROUP:
                    cardElement = cardUpdateService.getCardGroupElement(
                            cardUpdateService.addCardGroup(parentCardId, title, image, description).getPrimaryKey()
                    );
                    break;
                case DECK:
                    cardElement = cardUpdateService.getDeckElement(
                            cardUpdateService.addDeck(user.getUserId(), title, image, description).getPrimaryKey()
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
        boolean isOwner = cardUpdateService.isUserCardOwner(cardTypeEnum, user, cardId);

        CardElement<?> cardElement;
        switch(cardTypeEnum) {
            case MINDCARD:
                cardElement = cardUpdateService.getMindcardElement(cardId);
                break;
            case INFOCARD:
                cardElement = cardUpdateService.getInfocardElement(cardId);
                break;
            default:
                throw new InvalidCardTypeException(cardTypeEnum);
        }

        model.addAttribute("isOwner",isOwner);
        model.addAttribute("cardElement",cardElement);
        model.addAttribute("cardType",cardType.toLowerCase());

        return "fragments/card :: card(isOwner=${isOwner},cardElement=${cardElement},cardType=${cardType})";
    }

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }

}
