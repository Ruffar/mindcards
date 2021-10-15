package com.raffier.mindcards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.card.MindcardElements;
import com.raffier.mindcards.model.table.*;
import com.raffier.mindcards.service.CardModelService;
import com.raffier.mindcards.service.CardService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ModelAndView mindcardView(@ModelAttribute("user") User user, @PathVariable int mindcardId) {
        ModelAndView mv = new ModelAndView("cards/mindcard");

        CardElement<Mindcard> mindcard = cardModelService.getCardElement(cardService.getMindcardImage(mindcardId));
        List<Pair<Infocard,Image>> infoPairList = cardService.getInfocardsFromMindcard(mindcardId);
        List<CardElement<Infocard>> infoList = new ArrayList<>();
        for (Pair<Infocard,Image> pair : infoPairList) {
            infoList.add(cardModelService.getCardElement(pair));
        }

        boolean isOwner = cardService.isUserMindcardOwner(user,mindcardId);
        mv.addObject("isOwner",isOwner);

        mv.addObject("mindcard",mindcard);
        mv.addObject("infocards",infoList);

        return mv;
    }

    @PostMapping("saveCard")
    public String saveCard(@ModelAttribute User user, @RequestParam boolean isMain, @RequestParam String cardType, @RequestParam int cardId, @RequestParam int imageId, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        boolean isOwner = false;

        CardElement<?> cardElement = new CardElement<>();
        switch(cardType) {
            case "mindcard" :
                if (cardService.isUserMindcardOwner(user, cardId)) {
                    isOwner = true;
                    cardService.updateMindcard(cardId, title, imageId, description);
                }
                cardElement = cardService.getMindcardElement(cardId);
                break;
            case "infocard":
                if (cardService.isUserInfocardOwner(user, cardId)) {
                    isOwner = true;
                    cardService.updateInfocard(cardId, imageId, description);
                }
                cardElement = cardService.getInfocardElement(cardId);
                break;
        }

        model.addAttribute("cardElement",cardElement);

        return "fragments/card :: card(isMain=${'"+isMain+"'},isOwner=${'"+isOwner+"'},cardElement=${cardElement},cardType='"+cardType+"')";
    }

    @GetMapping("getCardElement")
    public String getCardElement(@RequestParam boolean isMain, @RequestParam String cardType, @RequestParam int cardId, @RequestParam int imageId, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        CardElement<?> cardElement = new CardElement<>();
        switch(cardType) {
            case "mindcard" :
                cardElement = cardModelService.getCardElement(cardService.getMindcardImage(cardId));
                break;
            case "infocard":
                cardElement = cardModelService.getCardElement(cardService.getInfocardImage(cardId));
                break;
        }

        model.addAttribute("cardElement",cardElement);

        return "fragments/card :: card(isMain=${'"+isMain+"'},isOwner=${'"+isOwner+"'},cardElement=${cardElement},cardType='"+cardType+"')";
    }

    @GetMapping("getCardEditor")
    public String getCardEditor(@RequestParam boolean isMain, @RequestParam String cardType, @RequestParam int cardId, @RequestParam int imageId, @RequestParam(defaultValue = "") String title, @RequestParam String description, Model model) {

        CardElement<?> cardElement = new CardElement<>();
        switch(cardType) {
            case "mindcard" :
                cardElement = cardModelService.getCardElement(cardService.getMindcardImage(cardId));
                break;
            case "infocard":
                cardElement = cardModelService.getCardElement(cardService.getInfocardImage(cardId));
                break;
        }

        model.addAttribute("cardElement",cardElement);

        return "fragments/card :: cardEditor(isMain=${'"+isMain+"'},cardElement=${cardElement},cardType='"+cardType+"')";
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
