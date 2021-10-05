package com.raffier.mindcards.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        return "redirect:home";
    }

    @GetMapping("home")
    public ModelAndView home(HttpSession session, Model model) {
        return new ModelAndView("home");
    }

}
