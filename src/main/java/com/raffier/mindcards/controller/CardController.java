package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.InvalidCardTypeException;
import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.card.DeckElement;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.service.*;
import com.raffier.mindcards.util.CardType;
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
    @Autowired
    DeckService deckService;
    @Autowired
    CardGroupService cardGroupService;

    private void handleCardPage(User user, Deck deck, ModelAndView mv) {
        int deckId = deck.getDeckId();
        int userId = user == null ? 0 : user.getUserId();
        //Make sure that card does not have ID 0 (i.e. the card is deleted)
        if (deckId == 0) {
            throw new EntityNotFoundException("This card has been deleted...");
        }
        //Check if deck can be accessed
        if (deck.isPrivate() && deck.getOwnerId() != userId) {
            throw new UnauthorisedAccessException("This Deck is private!");
        }
        //Update lastViewed if the user is logged in and has favourited the card
        if (user != null && deckService.hasUserFavourited(deckId,userId)) {
            deckService.updateLastViewed(deckId, userId);
        }
        //Add ownership
        System.out.println(userId);
        System.out.println(deck.getOwnerId());
        mv.addObject("isOwned",userId == deck.getOwnerId());
    }

    @GetMapping(value="deck/{deckId}")
    public ModelAndView deckView(@ModelAttribute User user, @PathVariable int deckId, ModelAndView mv) {
        mv.setViewName("cards/deck");

        DeckElement deck = cardElementService.getDeckElement( user, deckId );
        List<CardElement<Mindcard>> mindcards = cardElementService.getRandomMindcardsFromDeck( user, deckId, 12 );
        List<CardElement<CardGroup>> cardGroups = cardElementService.getRandomCardGroupsFromDeck( user, deckId, 12 );

        handleCardPage(user, deck.getCard(), mv);

        mv.addObject("deck",deck);
        mv.addObject("mindcards",mindcards);
        mv.addObject("cardGroups",cardGroups);

        return mv;
    }

    @GetMapping(value="group/{cardGroupId}")
    public ModelAndView groupView(@ModelAttribute User user, @PathVariable int cardGroupId, ModelAndView mv) {
        mv.setViewName("cards/group");

        CardElement<CardGroup> group = cardElementService.getCardGroupElement( user, cardGroupId);
        List<CardElement<Mindcard>> mindcards = cardElementService.getMindcardsFromCardGroup( user, group.getCard().getCardGroupId() );
        DeckElement deck = cardElementService.getDeckElement( user, group.getCard().getDeckId() );

        handleCardPage(user, deck.getCard(), mv);

        mv.addObject("deck",deck);
        mv.addObject("mindcards",mindcards);
        mv.addObject("group",group);

        return mv;
    }

    @GetMapping(value="mindcard/{mindcardId}")
    public ModelAndView mindcardView(@ModelAttribute User user, @PathVariable int mindcardId, ModelAndView mv) {
        mv.setViewName("cards/mindcard");

        CardElement<Mindcard> mindcard = cardElementService.getMindcardElement( user, mindcardId );
        List<CardElement<Infocard>> infocards = cardElementService.getInfocardsFromMindcard( user, mindcardId );
        DeckElement deck = cardElementService.getDeckElement( user, mindcard.getCard().getDeckId() );

        handleCardPage(user, deck.getCard(), mv);

        mv.addObject("deck",deck);
        mv.addObject("mindcard",mindcard);
        mv.addObject("infocards",infocards);

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

        return new ResponseEntity<>(null,HttpStatus.OK);
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

        return new ResponseEntity<>(cardElement, HttpStatus.CREATED);
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

    //Group Mindcards
    private void handleCardGroupAccess(User user, int cardGroupId) {

        if (user == null || !cardUtilityService.isUserCardOwner(CardType.CARDGROUP,user,cardGroupId)) {
            throw new UnauthorisedAccessException("You do not own this card group!");
        }

    }

    @PostMapping("addMindcardToCardGroup")
    public ResponseEntity<?> addMindcardToCardGroup(@ModelAttribute User user, @RequestParam int mindcardId, @RequestParam int cardGroupId) {

        handleCardGroupAccess(user,cardGroupId);

        if (!cardGroupService.isMindcardInCardGroup(mindcardId,cardGroupId)) {
            cardGroupService.addMindcardToCardGroup(mindcardId, cardGroupId);
            return new ResponseEntity<>(null,HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("removeMindcardFromCardGroup")
    public ResponseEntity<?> removeMindcardFromCardGroup(@ModelAttribute User user, @RequestParam int mindcardId, @RequestParam int cardGroupId) {

        handleCardGroupAccess(user,cardGroupId);

        if (cardGroupService.isMindcardInCardGroup(mindcardId,cardGroupId)) {
            cardGroupService.removeMindcardFromCardGroup(mindcardId, cardGroupId);
            return new ResponseEntity<>(null,HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }

}
