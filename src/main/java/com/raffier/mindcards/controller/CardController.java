package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.CardElement;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.service.CardService;
import com.raffier.mindcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

    @GetMapping(value="mindcard")
    public ModelAndView mindcardView(@ModelAttribute("user") User user, @RequestParam(name="id",defaultValue="0") int mindcardId, @RequestParam(name="edit",defaultValue="false") boolean isEditing) {
        ModelAndView mv = new ModelAndView("cards/mindcard");

        CardElement mindcardElement = cardService.getMindcardElement(mindcardId);
        List<CardElement> infoElements = cardService.getInfocardElements(mindcardId);

        boolean isOwner = cardService.isUserMindcardOwner(user,mindcardId);
        mv.addObject("owned",isOwner);

        mv.addObject("mindcard",mindcardElement);
        mv.addObject("infocards",infoElements);

        if (isEditing && isOwner) { mv.setViewName("cards/mindcardEditor"); }
        return mv;
    }

    @PostMapping(value="mindcard")
    public ModelAndView updateMindcard(@ModelAttribute CardElement mindcardElement, @RequestParam(name="id") int mindcardId) {
        ModelAndView mv = new ModelAndView("redirect:/mindcard?id="+mindcardId);

        System.out.println(mindcardElement);
        //if (cardService.isUserMindcardOwner(user,mindcardId)) {
            cardService.updateMindcard(mindcardElement,mindcardId);
        //}

        return mv;
    }

    @PostMapping(value="updateInfocard")
    public ModelAndView updateInfocard(@ModelAttribute("infocard") CardElement infocardElement, @ModelAttribute("user") User user, @RequestParam(name="id") int infocardId) {
        ModelAndView mv = new ModelAndView("redirect:/mindcard?id="+cardService.getInfocardMindcardId(infocardId));

        if (cardService.isUserMindcardOwner(user,infocardId)) {
            cardService.updateMindcard(infocardElement,infocardId);
        }

        return mv;
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
