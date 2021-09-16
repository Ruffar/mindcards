package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.table.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @GetMapping(value="login")
    public String loginPage(Model model) {

        model.addAttribute("submission",new LoginSubmission());

        return "login";
    }

    @PostMapping(value="login")
    public String loginSubmit(@ModelAttribute LoginSubmission submission, HttpSession session, HttpServletRequest request) {

        User user = User.getUserFromEmail(AppConfig.getDatabase(),submission.getEmail());
        if (user != null && user.getPassword().equals(submission.getPassword())) {
            session.setAttribute("userId", user.getUserId());
            return "redirect:/";
        }
        else {
            return "redirect:login?invalid";
        }

    }

    @GetMapping(value="profile")
    public String profile(HttpSession session, Model model) {

        ControllerUtil.setUserSession(session, model);

        return "profilePage";
    }

    @GetMapping(value="logout")
    public String logout(HttpSession session, Model model) {

        session.setAttribute("user",null);

        return "redirect:home";
    }

}