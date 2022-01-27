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

    //Browse page request
    @GetMapping(value="browse")
    public ModelAndView browseView(@ModelAttribute User user, @RequestParam(required = false) String sort, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String search, ModelAndView mv) {

        //Get sort type from parameter
        SortType sortType = SortType.getSortTypeFromString(sort);

        if (page < 1) {
            throw new PageIndexException(page); //Throw error if index is outside of range (< 1)
        }

        //If search parameter is not empty, then do a deck search
        if (!search.equals("")) {
            mv.setViewName("deckLibrary/search");
            mv.addObject("decks", cardElementService.searchDecks(user, search, 12, page-1));
            mv.addObject("search",search);
        }
        //If there is no search but a sort type, then decks by a sort order
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
        //If none of the above conditions are met, then give the user random decks
        else {
            mv.setViewName("deckLibrary/random");
            mv.addObject("decks", cardElementService.getDeckRandom(user, 12));
        }

        //Set ModelView objects for HTML
        mv.addObject("sort",sort);
        mv.addObject("pageNo",page);

        return mv;
    }

    //Revise page request
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


    //Favourite deck request
    @PostMapping(value="favouriteDeck")
    public ResponseEntity<?> favouriteDeck(@ModelAttribute User user, @RequestParam int deckId) {

        if (user == null) {
            throw new UnauthorisedAccessException(); //Deny this request if user is not logged in
        }

        //Accept request if user hasn't favourited the deck yet
        if (!deckService.hasUserFavourited(deckId, user.getUserId())) {
            deckService.addFavourite(deckId, user.getUserId());
            return new ResponseEntity<>(null,HttpStatus.ACCEPTED);
        }

        //Return error if neither conditions are met for any reason
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);

    }

    //Unfavourite deck request
    @DeleteMapping(value="unfavouriteDeck")
    public ResponseEntity<?> unfavouriteDeck(@ModelAttribute User user, @RequestParam int deckId) {

        if (user == null) {
            throw new UnauthorisedAccessException(); //Deny this request if user is not logged in
        }

        //Accept request if user has favourited the deck
        if (deckService.hasUserFavourited(deckId, user.getUserId())) {
            deckService.removeFavourite(deckId, user.getUserId());
            return new ResponseEntity<>(null,HttpStatus.ACCEPTED);
        }

        //Return error if neither conditions are met for any reason
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);

    }


    //Profile default request
    @GetMapping(value="profile")
    public ModelAndView myProfile(@ModelAttribute("user") User user, ModelAndView mv) {

        //If user isn't logged in, redirect to login page
        if (user == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        //Redirect to actual profile of user using userId
        mv.setViewName("redirect:/profile/"+user.getUserId());
        return mv;
    }

    //Profile page request
    @GetMapping(value="profile/{userId}")
    public ModelAndView profile(@ModelAttribute("user") User user, @PathVariable int userId, ModelAndView mv) {
        mv.setViewName("profilePage");

        User profileUser = userService.getUser(userId);

        mv.addObject("profileUser",profileUser);
        mv.addObject("decks", cardElementService.getDecksFromUser(user, profileUser));

        return mv;
    }

    //Get the user attribute
    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }
}
