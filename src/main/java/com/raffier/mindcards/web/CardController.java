package com.raffier.mindcards.web;

import com.raffier.mindcards.AppSettings;
import com.raffier.mindcards.data.table.Infocard;
import com.raffier.mindcards.data.table.Mindcard;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CardController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value="pack/{packId}/{mindcardId}", method = {RequestMethod.GET})
    public String mindcardView(@PathVariable int packId, @PathVariable int mindcardId, Model model) {

        Mindcard card = Mindcard.getMindcard(AppSettings.database, mindcardId);
        if (card == null) return "error";

        List<Infocard> infocards = card.getInfocards();
        for (Infocard info: infocards) System.out.println(info.getDescription());

        model.addAttribute("mindcard",card);
        model.addAttribute("infocards",infocards);
        return "mindcardView";
    }
    
}
