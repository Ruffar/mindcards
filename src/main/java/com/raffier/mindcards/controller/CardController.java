package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.card.CardImagePair;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.service.CardModelService;
import com.raffier.mindcards.service.CardService;
import com.raffier.mindcards.service.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("user")
public class CardController {

    @Autowired
    CardService cardService;
    @Autowired
    CardModelService cardModelService;

    /*@GetMapping(value="pack/{packId}")
    public ModelAndView packView(@PathVariable int packId, HttpSession session) {

        ModelAndView mv = ControllerUtil.getGenericMV(session);

        CardPack pack = CardPack.getCardPack(AppConfig.getDatabase(), packId);
        if (pack == null) throw new EntityNotFoundException("Card Pack", packId);

        //List<Infocard> infocards = card.getInfocards();

        mv.addObject("pack",pack);
        //mv.addObject("infocards",infocards);

        mv.setViewName("cards/pack");
        return mv;
    }*/

    @GetMapping(value="mindcard/{mindcardId}")
    public ModelAndView mindcardView(@ModelAttribute("user") User user, @PathVariable int mindcardId, ModelAndView mv) {
        mv.setViewName("cards/mindcard");

        CardElement<Mindcard> mindcard = cardModelService.getCardElement(cardService.getMindcardImagePair(mindcardId));
        List<CardImagePair<Infocard>> infoPairList = cardService.getInfocardsFromMindcard(mindcardId);
        List<CardElement<Infocard>> infoList = new ArrayList<>();
        for (CardImagePair<Infocard> pair : infoPairList) {
            infoList.add(cardModelService.getCardElement(pair));
        }

        boolean isOwner = cardService.isUserCardOwner(CardType.MINDCARD,user,mindcardId);
        mv.addObject("isOwner",isOwner);

        mv.addObject("mindcard",mindcard);
        mv.addObject("infocards",infoList);

        return mv;
    }

    @PostMapping("saveCard")
    public String saveCard(@ModelAttribute User user, @RequestParam(defaultValue="false") boolean fromAjax, @RequestParam(defaultValue="false") boolean isMain, @RequestParam String cardType, @RequestParam int cardId, @RequestParam int imageId, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        CardElement<?> cardElement = new CardElement<>();
        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        boolean isOwner = cardService.isUserCardOwner(cardTypeEnum, user, cardId);

        if (isOwner && cardModelService.isDescriptionValid(description)) {
            switch (cardTypeEnum) {
                case MINDCARD:
                    cardService.updateMindcard(cardId, title, imageId, description);
                    break;
                case INFOCARD:
                    cardService.updateInfocard(cardId, imageId, description);
                    break;
            }
        } else {
            throw new UnauthorisedAccessException();
        }

        return getCardElement(user, isMain, cardType, cardId, model);
    }

    @GetMapping("getCardElement")
    public String getCardElement(@ModelAttribute User user, @RequestParam(defaultValue="false") boolean isMain, @RequestParam String cardType, @RequestParam int cardId, Model model) {

        CardElement<?> cardElement = new CardElement<>();
        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        boolean isOwner = cardService.isUserCardOwner(cardTypeEnum, user, cardId);

        switch(cardTypeEnum) {
            case MINDCARD:
                cardElement = cardModelService.getCardElement(cardService.getMindcardImagePair(cardId));
                break;
            case INFOCARD:
                cardElement = cardModelService.getCardElement(cardService.getInfocardImagePair(cardId));
                break;
        }

        model.addAttribute("isMain",isMain);
        model.addAttribute("isOwner",isOwner);
        model.addAttribute("cardElement",cardElement);
        model.addAttribute("cardType",cardType);

        return "fragments/card :: card(isMain=${isMain},isOwner=${isOwner},cardElement=${cardElement},cardType=${cardType})";
    }

    @GetMapping("getCardEditor")
    public String getCardEditor(@ModelAttribute User user, @RequestParam(defaultValue="false") boolean isMain, @RequestParam String cardType, @RequestParam int cardId, @RequestParam int imageId, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        CardElement<?> cardElement = new CardElement<>();
        CardType cardTypeEnum = CardType.getCardTypeFromString(cardType);
        if (!cardService.isUserCardOwner(cardTypeEnum, user, cardId)) {
            throw new UnauthorisedAccessException();
        }

        switch(CardType.getCardTypeFromString(cardType)) {
            case MINDCARD:
                cardElement = cardModelService.getCardElement(cardService.getMindcardImagePair(cardId));
                break;
            case INFOCARD:
                cardElement = cardModelService.getCardElement(cardService.getInfocardImagePair(cardId));
                break;
        }

        model.addAttribute("isMain",isMain);
        model.addAttribute("cardElement",cardElement);
        model.addAttribute("cardType",cardType);

        //System.out.println(cardElement.getImage().getImagePath());

        return "fragments/card :: cardEditor(isMain=${isMain},cardElement=${cardElement},cardType=${cardType})";
    }

    /*@GetMapping(value="pack/{packId}/group/{cardGroupId}")
    public ModelAndView groupView(@PathVariable int packId, @PathVariable int cardGroupId, HttpSession session) {

        ModelAndView mv = ControllerUtil.getGenericMV(session);

        CardGroup group = CardGroup.getCardGroup(AppConfig.getDatabase(), cardGroupId);
        if (group == null) throw new EntityNotFoundException("Card Group", cardGroupId);

        //List<Mindcard> mindcards = group.getCard();

        mv.addObject("group",group);
        //mv.addObject("infocards",infocards);

        mv.setViewName("cards/group");
        return mv;
    }*/

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }

}
