package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.table.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        return "redirect:home";
    }

    @GetMapping("home")
    public String home(HttpSession session, Model model) {
        ControllerUtil.setUserSession(session, model);
        return "home";
    }

}
