package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.table.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @GetMapping(value="login")
    public ModelAndView loginPage(HttpSession session) {
        ModelAndView mv = ControllerUtil.getGenericMV(session);

        User user = ControllerUtil.getSessionUser(session);
        if (user != null) {
            return new ModelAndView("redirect:profile");
        }

        mv.addObject("submission", new LoginSubmission());
        mv.setViewName("login");
        return mv;
    }

    @PostMapping(value="login")
    public String loginSubmit(@ModelAttribute LoginSubmission submission, @RequestParam(name="return",required=false) String returnUrl, HttpSession session, HttpServletRequest request) {
        ModelAndView mv = ControllerUtil.getGenericMV(session);

        User user = User.getUserFromEmail(AppConfig.getDatabase(),submission.getEmail());
        if (user != null && user.getPassword().equals(submission.getPassword())) {
            session.setAttribute("userId", user.getUserId());
            return "redirect:/"+returnUrl;
        }
        else {
            return "redirect:login?invalid";
        }

    }

    @GetMapping(value="profile")
    public String profile(HttpSession session, Model model) {

        User user = ControllerUtil.getSessionUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user",user);
        return "profilePage";
    }

    @GetMapping(value="logout")
    public String logout(HttpSession session, Model model) {

        session.setAttribute("userId", null);

        return "redirect:/home";
    }

}
