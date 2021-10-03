package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.LoginSubmission;
import com.raffier.mindcards.model.table.User;
import com.raffier.mindcards.service.RepositoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public ModelAndView loginSubmit(@ModelAttribute("submission") LoginSubmission submission, @RequestParam(name="return",defaultValue="") String returnUrl, HttpSession session, HttpServletRequest request, BindingResult result) {
        ModelAndView mv = ControllerUtil.getGenericMV(session);

        User user = RepositoryService.getUserRepository().getByLogin(submission.getEmail(), submission.getPassword());//User.getUserByLogin(AppConfig.getDatabase(),submission.getEmail(),submission.getPassword());
        if (user != null) {
            session.setAttribute("userId", user.getUserId());
            return new ModelAndView("redirect:"+returnUrl);
        }
        else {
            mv.addObject("invalid",true);
            mv.setViewName("login");
            return mv;
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
