package com.raffier.mindcards.controller;

import com.raffier.mindcards.model.web.AppResponse;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserService userService;

    //Login page
    @GetMapping(value="login")
    public ModelAndView loginPage(@ModelAttribute("user") User user, HttpSession session) {
        ModelAndView mv = new ModelAndView("user/login");

        if (user != null) {
            return new ModelAndView("redirect:profile");
        }

        return mv;
    }

    //Login to user request
    @PostMapping(value="login")
    @ResponseBody
    public AppResponse loginSubmit(@RequestParam String email, @RequestParam String password, HttpSession session) throws SQLException {

        User user = userService.userLogin(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            return new AppResponse(HttpStatus.OK);
        }

        return new AppResponse(HttpStatus.BAD_REQUEST);

    }

    //Register page
    @GetMapping(value="register")
    public ModelAndView registerPage(@ModelAttribute("user") User user, HttpSession session) {
        ModelAndView mv = new ModelAndView("user/register");

        if (user != null) {
            return new ModelAndView("redirect:profile");
        }

        return mv;
    }

    //Register new user request
    @PostMapping(value="register")
    public AppResponse registerSubmit(@RequestParam String username, @RequestParam String email, @RequestParam String password, HttpSession session) throws SQLException {

        User user = userService.userRegister(username, email, password);
        if (user != null) {
            session.setAttribute("user", user);
            return new AppResponse(HttpStatus.OK);
        }

        return new AppResponse(HttpStatus.BAD_REQUEST);

    }

    //Logout request
    @GetMapping(value="logout")
    public ModelAndView logout(HttpSession session, SessionStatus status, HttpServletRequest request) {

        status.setComplete(); //Say that the request is completed without doing anything else
        session.removeAttribute("user"); //Remove the user account attribute from their session

        return new ModelAndView("redirect:"+request.getHeader("Referer")); //Redirect user to same page
    }

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }

}
