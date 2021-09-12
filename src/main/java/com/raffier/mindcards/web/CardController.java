package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.table.Infocard;
import com.raffier.mindcards.model.table.Mindcard;
import com.raffier.mindcards.model.table.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CardController {

    @GetMapping(value="pack/{packId}/{mindcardId}")
    public String mindcardView(@PathVariable int packId, @PathVariable int mindcardId, HttpSession session, Model model) {

        ControllerUtil.setUserSession(session,model);

        Mindcard card = Mindcard.getMindcard(AppConfig.getDatabase(), mindcardId);
        if (card == null) return "error";

        List<Infocard> infocards = card.getInfocards();

        model.addAttribute("mindcard",card);
        model.addAttribute("infocards",infocards);
        return "mindcardView";
    }
    
}
