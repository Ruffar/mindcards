package com.raffier.mindcards.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MindcardErrorController implements ErrorController {

    @RequestMapping("error")
    public String error(HttpServletRequest request, HttpSession session, Model model) {
        int statusCode = (int)request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception)request.getAttribute("javax.servlet.error.exception");

        model.addAttribute("title",String.valueOf(statusCode));
        model.addAttribute("message",exception.getMessage());

        return "error/generic";
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
