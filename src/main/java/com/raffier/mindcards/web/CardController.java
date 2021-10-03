package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.CardElement;
import com.raffier.mindcards.model.table.CardGroup;
import com.raffier.mindcards.model.table.CardPack;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.service.RepositoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CardController {

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

    @GetMapping(value="pack/{packId}/card/{mindcardId}")
    public ModelAndView mindcardView(@PathVariable int packId, @PathVariable int mindcardId, HttpSession session) {

        ModelAndView mv = ControllerUtil.getGenericMV(session);

        Mindcard card = RepositoryService.getMindcardRepository().getById(mindcardId);//Mindcard.getMindcard(AppConfig.getDatabase(), mindcardId);
        if (card == null) throw new EntityNotFoundException("Mindcard", mindcardId);

        List<Infocard> infocards = RepositoryService.getInfocardRepository().getFromMindcard(mindcardId);
        System.out.println(infocards.size());
        List<CardElement> infoElements = new ArrayList<>();
        for (Infocard i: infocards) {
            CardElement element = RepositoryService.getElement(i);
            infoElements.add(element);
            System.out.println(element.getDescription());
        }

        mv.addObject("mindcard",RepositoryService.getElement(card));
        mv.addObject("infocards",infoElements);

        mv.setViewName("cards/mindcard");
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

}
