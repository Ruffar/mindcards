package com.raffier.mindcards.controller;

import com.raffier.mindcards.model.card.CardElement;
import com.raffier.mindcards.model.table.Deck;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@SessionAttributes("user")
public class DeckLibraryController {

    @Autowired
    DeckService deckService;

    @GetMapping(value="browse")
    public ModelAndView browseView(ModelAndView mv) {
        mv.setViewName("deckLibrary/browse");

        mv.addObject("decks", deckService.getRandom(12));

        return mv;
    }

    @GetMapping(value="browse/search")
    public ModelAndView searchView(@RequestParam(defaultValue = "") String searchTerm, ModelAndView mv) {
        mv.setViewName("deckLibrary/search");

        mv.addObject("decks", deckService.searchDeck(searchTerm));

        return mv;
    }

    //
    @GetMapping(value="getDeckRandom")
    public ResponseEntity<List<CardElement<Deck>>> getRandom(@RequestParam("amount") int amount) {
        return new ResponseEntity<>(deckService.getRandom(amount), HttpStatus.OK);
    }

    @GetMapping(value="getDeckPopular")
    public ResponseEntity<List<CardElement<Deck>>> getRandom(@RequestParam("amount") int amount, @RequestParam(name="page",defaultValue="0") int page) {
        return new ResponseEntity<>(deckService.getPopular(amount,page), HttpStatus.OK);
    }

    //
    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }
}
