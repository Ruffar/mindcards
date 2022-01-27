package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.errorHandling.ImageChangeException;
import com.raffier.mindcards.errorHandling.InvalidCardTypeException;
import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import com.raffier.mindcards.model.web.*;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.service.*;
import com.raffier.mindcards.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
        int userId = user == null ? 0 : user.getUserId(); //userId is 0 (deleted) if there is no user because int can't be null
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
        mv.addObject("isOwned",userId == deck.getOwnerId());
    }

    //Deck page
    @GetMapping(value="deck/{deckId}")
    public ModelAndView deckView(@ModelAttribute User user, @PathVariable int deckId, @RequestParam(defaultValue = "") String search, ModelAndView mv) {
        mv.setViewName("cards/deck");

        DeckElement deck = cardElementService.getDeckElement( user, deckId ); //Get deck HTML element
        //Get mindcards and card groups of the deck
        List<CardElement<Mindcard>> mindcards;
        List<CardElement<CardGroup>> cardGroups;
        if (!search.equals("")) {
            mindcards = cardElementService.searchMindcards(deckId, search, 12, 0);
            cardGroups = cardElementService.searchCardGroups(deckId, search, 12, 0);
        } else {
            mindcards = cardElementService.getRandomMindcardsFromDeck(deckId, 12);
            cardGroups = cardElementService.getRandomCardGroupsFromDeck(deckId, 12);
        }

        handleCardPage(user, deck.getCard(), mv);

        mv.addObject("deck",deck);
        mv.addObject("mindcards",mindcards);
        mv.addObject("cardGroups",cardGroups);

        return mv;
    }

    @GetMapping(value="cardGroup/{cardGroupId}")
    public ModelAndView groupView(@ModelAttribute User user, @PathVariable int cardGroupId, ModelAndView mv) {
        mv.setViewName("cards/group");

        CardElement<CardGroup> group = cardElementService.getCardGroupElement( cardGroupId);
        List<CardElement<Mindcard>> mindcards = cardElementService.getMindcardsFromCardGroup( group.getCard().getCardGroupId() );
        DeckElement deck = cardElementService.getDeckElement( user, group.getCard().getDeckId() );

        handleCardPage(user, deck.getCard(), mv);

        mv.addObject("deck",deck);
        mv.addObject("mindcards",mindcards);
        mv.addObject("cardGroup",group);

        return mv;
    }

    @GetMapping(value="mindcard/{mindcardId}")
    public ModelAndView mindcardView(@ModelAttribute User user, @PathVariable int mindcardId, ModelAndView mv) {
        mv.setViewName("cards/mindcard");

        CardElement<Mindcard> mindcard = cardElementService.getMindcardElement( mindcardId );
        List<CardElement<Infocard>> infocards = cardElementService.getInfocardsFromMindcard( mindcardId );
        DeckElement deck = cardElementService.getDeckElement( user, mindcard.getCard().getDeckId() );

        handleCardPage(user, deck.getCard(), mv);

        mv.addObject("deck",deck);
        mv.addObject("mindcard",mindcard);
        mv.addObject("infocards",infocards);

        return mv;
    }

    @PostMapping("saveCard")
    public ResponseEntity<CardElement<?>> saveCard(@ModelAttribute User user,
                                                   @RequestParam String cardType, //Type of card
                                                   @RequestParam int cardId, //ID of the card
                                                   @RequestParam(defaultValue="none") String imageChange, //What happens to the image, must be one of the imageChangeEnums
                                                   @RequestParam(required = false) MultipartFile imageFile, //If imageChange = upload, this stores the image file
                                                   @RequestParam(required = false) String imageUrl, //If imageChange = url, this stores the image URL
                                                   @RequestParam(defaultValue = "") String title, //Title of the card
                                                   @RequestParam(defaultValue="") String description) //Description of card
    {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);

        //Create image update data
        ImageChangeType imageChangeEnum = ImageChangeType.getImageChangeTypeFromString(imageChange);
        ImageUpdate imageUpdateData;
        switch(imageChangeEnum) {
            case UPLOAD:
                if (imageFile == null || imageFile.isEmpty()) throw new ImageChangeException("Image not yet uploaded...");
                imageUpdateData = new ImageFileUpdate(imageFile);
                break;
            case URL:
                if (imageUrl == null || imageUrl.equals("")) throw new ImageChangeException("Image URL must not be empty...");
                imageUpdateData = new ImageUrlUpdate(imageUrl);
                break;
            default:
                imageUpdateData = new ImageUpdate(imageChangeEnum);
                break;
        }

        // Update card data //
        boolean isOwner = cardUtilityService.isUserCardOwner(cardTypeEnum, user, cardId);

        CardElement<?> cardElement;
        if (isOwner && cardUpdateService.areHyperlinksValid(description)) { //Check if Hyperlinks are valid; if not, it will raise Invalid Hyperlink Exception
            switch (cardTypeEnum) {
                case DECK:
                    cardUpdateService.updateDeck(cardId, title, imageUpdateData, description);
                    cardElement = cardElementService.getDeckElement( user, cardId );
                    break;
                case CARDGROUP:
                    cardUpdateService.updateCardGroup(cardId, title, imageUpdateData, description);
                    cardElement = cardElementService.getCardGroupElement( cardId );
                    break;
                case MINDCARD:
                    cardUpdateService.updateMindcard(cardId, title, imageUpdateData, description);
                    cardElement = cardElementService.getMindcardElement( cardId );
                    break;
                case INFOCARD:
                    cardUpdateService.updateInfocard(cardId, imageUpdateData, description);
                    cardElement = cardElementService.getInfocardElement( cardId );
                    break;
                default:
                    throw new InvalidCardTypeException(cardTypeEnum);
            }
        } else {
            throw new UnauthorisedAccessException();
        }

        return new ResponseEntity<>(cardElement, HttpStatus.ACCEPTED); //Return card after updating
    }

    @DeleteMapping("deleteCard")
    public ResponseEntity<?> deleteCard(@ModelAttribute User user, @RequestParam String cardType, @RequestParam int cardId) {

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
            throw new UnauthorisedAccessException(); //Can't delete card as user doesn't own the card
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("addCard")
    public ResponseEntity<CardElement<?>> addCard(@ModelAttribute User user, @RequestParam String cardType, @RequestParam(defaultValue = "0") int parentCardId) {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);

        // Check if the deck that card is being added for is owned by user (or immediately add if a deck is being added)
        boolean isOwner;
        if (cardTypeEnum == CardType.DECK) {
            isOwner = true;
        } else {
            CardType parentType = cardTypeEnum.getParentType();
            isOwner = cardUtilityService.isUserCardOwner(parentType, user, parentCardId);
        }

        // Add deck
        CardElement<?> cardElement;
        if (isOwner) {
            switch (cardTypeEnum) {
                case MINDCARD:
                    cardElement = cardElementService.getMindcardElement(
                            cardUpdateService.addMindcard(parentCardId).getPrimaryKey()
                    );
                    break;
                case INFOCARD:
                    cardElement = cardElementService.getInfocardElement(
                            cardUpdateService.addInfocard(parentCardId).getPrimaryKey()
                    );
                    break;
                case CARDGROUP:
                    cardElement = cardElementService.getCardGroupElement(
                            cardUpdateService.addCardGroup(parentCardId).getPrimaryKey()
                    );
                    break;
                case DECK:
                    cardElement = cardElementService.getDeckElement(
                            user, cardUpdateService.addDeck(user.getUserId()).getPrimaryKey()
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

    //Get card element
    @GetMapping("getCardElement")
    public ResponseEntity<CardElement<?>> getCardElement(@ModelAttribute User user, @RequestParam String cardType, @RequestParam int cardId) {

        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);

        //Get the card element from cardId and its cardType
        CardElement<?> cardElement;
        Deck deck;
        switch(cardTypeEnum) {
            case MINDCARD:
                cardElement = cardElementService.getMindcardElement( cardId );
                deck = cardElementService.getMindcardDeck( user, cardId ).getCard();
                break;
            case INFOCARD:
                cardElement = cardElementService.getInfocardElement( cardId );
                deck = cardElementService.getInfocardDeck( user, cardId ).getCard();
                break;
            case CARDGROUP:
                cardElement = cardElementService.getCardGroupElement( cardId );
                deck = cardElementService.getCardGroupDeck( user, cardId ).getCard();
                break;
            case DECK:
                cardElement = cardElementService.getDeckElement( user, cardId );
                deck = (Deck) cardElement.getCard();
                break;
            default:
                throw new InvalidCardTypeException(cardTypeEnum);
        }

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

        return new ResponseEntity<>(cardElement, HttpStatus.OK);
    }

    @GetMapping("searchDeckMindcards")
    public ResponseEntity<List<CardElement<Mindcard>>> getCardElement(@ModelAttribute User user, @RequestParam int deckId, @RequestParam String search) {

        //boolean isOwner = cardUtilityService.isUserCardOwner(cardTypeEnum, user, cardId);

        Deck deck = cardElementService.getDeckElement( user, deckId ).getCard();
        List<CardElement<Mindcard>> mindcards = cardElementService.searchMindcards(deckId, search, 10, 0);

        int userId = user == null ? 0 : user.getUserId();
        //Make sure that card does not have ID 0 (i.e. the card is deleted)
        if (deckId == 0) {
            throw new EntityNotFoundException("This card has been deleted...");
        }
        //Check if deck can be accessed
        if (deck.isPrivate() && deck.getOwnerId() != userId) {
            throw new UnauthorisedAccessException("This Deck is private!");
        }

        return new ResponseEntity<>(mindcards, HttpStatus.OK);
    }

    //Group Mindcards
    private void handleCardGroupAccess(User user, int cardGroupId) {

        if (user == null || !cardUtilityService.isUserCardOwner(CardType.CARDGROUP,user,cardGroupId)) {
            throw new UnauthorisedAccessException("You do not own this card group!");
        }

    }

    @PostMapping("addGroupMindcard")
    public ResponseEntity<?> addMindcardToCardGroup(@ModelAttribute User user, @RequestParam int mindcardId, @RequestParam int cardGroupId) {

        handleCardGroupAccess(user,cardGroupId);

        if (!cardGroupService.isMindcardInCardGroup(mindcardId,cardGroupId)) {
            cardGroupService.addMindcardToCardGroup(mindcardId, cardGroupId);
            return new ResponseEntity<>(null,HttpStatus.CREATED);
        }

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("removeGroupMindcard")
    public ResponseEntity<?> removeGroupMindcard(@ModelAttribute User user, @RequestParam int mindcardId, @RequestParam int cardGroupId) {

        System.out.println("hey");
        handleCardGroupAccess(user,cardGroupId);

        if (cardGroupService.isMindcardInCardGroup(mindcardId,cardGroupId)) {
            System.out.println("hi");
            cardGroupService.removeMindcardFromCardGroup(mindcardId, cardGroupId);
            return new ResponseEntity<>(new AppResponse(HttpStatus.OK),HttpStatus.OK);
        }

        System.out.println("bye");
        return new ResponseEntity<>(new AppResponse(HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    //Model attributes
    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }

}
