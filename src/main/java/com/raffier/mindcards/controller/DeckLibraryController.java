package com.raffier.mindcards.controller;

import com.raffier.mindcards.errorHandling.PageIndexException;
import com.raffier.mindcards.errorHandling.UnauthorisedAccessException;
import com.raffier.mindcards.model.web.DeckElement;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.service.CardElementService;
import com.raffier.mindcards.service.DeckService;
import com.raffier.mindcards.service.UserService;
import com.raffier.mindcards.util.SortType;
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
    CardElementService cardElementService;
    @Autowired
    DeckService deckService;
    @Autowired
    UserService userService;

    @GetMapping(value="browse")
    public ModelAndView browseView(@ModelAttribute User user, @RequestParam(required = false) String sort, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String search, ModelAndView mv) {

        SortType sortType = SortType.getSortTypeFromString(sort);
        if (page < 1) {
            throw new PageIndexException(page);
        }

        if (!search.equals("")) {
            mv.setViewName("deckLibrary/search");
            mv.addObject("decks", cardElementService.searchDecks(user, search, 12, page-1));
            mv.addObject("search",search);
        }
        else if (sortType != SortType.NONE) {
            mv.setViewName("deckLibrary/filtered");
            switch(sortType) {
                case POPULAR:
                    mv.addObject("decks", cardElementService.getDeckPopular(user, 12, page-1));
                    break;
                case NEWEST:
                    mv.addObject("decks", cardElementService.getDeckNewest(user, 12, page-1));
                    break;
                default:
                    throw new RuntimeException("Invalid sorting method");
            }
        }
        else {
            mv.setViewName("deckLibrary/random");
            mv.addObject("decks", cardElementService.getDeckRandom(user, 12));
        }

        mv.addObject("sort",sort);
        mv.addObject("pageNo",page);

        return mv;
    }

    @GetMapping(value="revise")
    public ModelAndView oldestViewedView(@ModelAttribute User user, @RequestParam(defaultValue = "1") int page, ModelAndView mv) {

        if (page < 1) {
            throw new PageIndexException(page);
        }

        if (user == null) {
            throw new UnauthorisedAccessException("You need to be logged in...");
        }

        mv.setViewName("deckLibrary/revise");
        mv.addObject("decks", cardElementService.getDeckOldestViewed(user, 12, page-1));
        mv.addObject("pageNumber",page);

        return mv;
    }

    @PostMapping(value="favouriteDeck")
    public ResponseEntity<?> favouriteDeck(@ModelAttribute User user, @RequestParam int deckId) {

        if (user == null) {
            throw new UnauthorisedAccessException();
        }

        if (!deckService.hasUserFavourited(deckId, user.getUserId())) {
            deckService.addFavourite(deckId, user.getUserId());
            return new ResponseEntity<>(null,HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value="unfavouriteDeck")
    public ResponseEntity<?> unfavouriteDeck(@ModelAttribute User user, @RequestParam int deckId) {

        if (user == null) {
            throw new UnauthorisedAccessException();
        }

        if (deckService.hasUserFavourited(deckId, user.getUserId())) {
            deckService.removeFavourite(deckId, user.getUserId());
            return new ResponseEntity<>(null,HttpStatus.OK);
        }

        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);

    }

    //
    @GetMapping(value="getDeckRandom")
    public ResponseEntity<List<DeckElement>> getRandom(@ModelAttribute User user, @RequestParam("amount") int amount) {
        return new ResponseEntity<>(cardElementService.getDeckRandom( user, amount ), HttpStatus.OK);
    }

    @GetMapping(value="getDeckPopular")
    public ResponseEntity<List<DeckElement>> getPopular (@ModelAttribute User user, @RequestParam("amount") int amount, @RequestParam(name="page",defaultValue="0") int page) {
        return new ResponseEntity<>(cardElementService.getDeckPopular( user, amount, page ), HttpStatus.OK);
    }

    //Profile
    @GetMapping(value="profile")
    public ModelAndView myProfile(@ModelAttribute("user") User user, ModelAndView mv) {

        if (user == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        mv.setViewName("redirect:/profile/"+user.getUserId());
        return mv;
    }

    @GetMapping(value="profile/{userId}")
    public ModelAndView profile(@ModelAttribute("user") User user, @PathVariable int userId, ModelAndView mv) {
        mv.setViewName("profilePage");

        User profileUser = userService.getUser(userId);

        mv.addObject("profileUser",profileUser);
        mv.addObject("decks", cardElementService.getDecksFromUser(user, profileUser));

        return mv;
    }

    //
    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }
}
