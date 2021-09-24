package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.table.User;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

public interface ControllerUtil {

    static ModelAndView getGenericMV(HttpSession session) {
        ModelAndView mv = new ModelAndView();

        Integer userId = (Integer) session.getAttribute("userId");
        User user = null;
        if (userId != null && userId != 0) {
            user = User.getUser(AppConfig.getDatabase(),userId);
        }
        mv.addObject("user",user);

        return mv;
    }

    static void setSessionUser(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        User user = null;
        if (userId != null && userId != 0) {
            user = User.getUser(AppConfig.getDatabase(),userId);
        }
        model.addAttribute("user",user);
    }

    static User getSessionUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null && userId != 0) {
            return User.getUser(AppConfig.getDatabase(),userId);
        }
        return null;
    }

}
