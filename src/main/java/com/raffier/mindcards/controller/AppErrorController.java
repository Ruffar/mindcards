package com.raffier.mindcards.controller;

import com.raffier.mindcards.model.web.AppResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {



    @GetMapping("error")
    private ModelAndView errorPage(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("error");

        HttpStatus statusCode = HttpStatus.valueOf((int)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        String message = (String)request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        mv.setStatus(statusCode);

        mv.addObject("error",new AppResponse(statusCode, message));
        //mv.addObject("error",error);

        return mv;
    }

    public String getErrorPath() {
        return "/error";
    }
}
