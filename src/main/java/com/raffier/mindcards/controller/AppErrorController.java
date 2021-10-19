package com.raffier.mindcards.controller;

import com.raffier.mindcards.model.table.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AppErrorController {



    @RequestMapping("error")
    private ModelAndView errorPage(HttpServletRequest request, ModelAndView mv) {
        HttpStatus statusCode = (HttpStatus)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        mv.setStatus(statusCode);
        mv.setViewName("error/generic");

        //mv.addObject("error",error);

        return mv;
    }

}
