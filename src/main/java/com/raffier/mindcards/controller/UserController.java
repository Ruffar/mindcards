package com.raffier.mindcards.controller;

import com.raffier.mindcards.model.LoginSubmission;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value="login")
    public ModelAndView loginPage(@ModelAttribute("user") User user, HttpSession session) {
        ModelAndView mv = new ModelAndView("login");

        if (user != null) {
            return new ModelAndView("redirect:profile");
        }

        mv.addObject("submission", new LoginSubmission());
        mv.setViewName("login");
        return mv;
    }

    @PostMapping(value="login")
    public ModelAndView loginSubmit(@ModelAttribute("submission") LoginSubmission submission, @RequestParam(name="return",defaultValue="") String returnUrl, HttpSession session, HttpServletRequest request, BindingResult result) {
        ModelAndView mv = new ModelAndView("login");

        User user = userService.userLogin(submission);
        if (user != null) {
            session.setAttribute("user", user);
            return new ModelAndView("redirect:"+returnUrl);
        }
        else {
            mv.addObject("loginError",userService.getLoginError(submission));
            return mv;
        }

    }

    @GetMapping(value="profile")
    public ModelAndView profile(@ModelAttribute("user") User user, HttpSession session) {
        ModelAndView mv = new ModelAndView("profilePage");

        if (user == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        mv.addObject("user",user);
        return mv;
    }

    @GetMapping(value="logout")
    public ModelAndView logout(HttpSession session, SessionStatus status, HttpServletRequest request) {

        status.setComplete();
        session.removeAttribute("user");

        return new ModelAndView("redirect:"+request.getHeader("Referer"));
    }

    @ModelAttribute("user")
    public User getUser(HttpSession session) {
        return (User)session.getAttribute("user");
    }

}
