package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.errorHandling.EntityNotFoundException;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CardController {

    @GetMapping(value="pack/{packId}/{mindcardId}")
    public ModelAndView mindcardView(@PathVariable int packId, @PathVariable int mindcardId, HttpSession session) {

        ModelAndView mv = ControllerUtil.getGenericMV(session);

        Mindcard card = Mindcard.getMindcard(AppConfig.getDatabase(), mindcardId);
        if (card == null) throw new EntityNotFoundException("Mindcard", mindcardId);

        List<Infocard> infocards = card.getInfocards();

        mv.addObject("mindcard",card);
        mv.addObject("infocards",infocards);

        mv.setViewName("cards/mindcard");
        return mv;
    }

}
