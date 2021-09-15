package com.raffier.mindcards.web;

import com.raffier.mindcards.AppConfig;
import com.raffier.mindcards.model.table.User;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public interface ControllerUtil {

    static void setUserSession(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        User user = null;
        if (userId != null && userId != 0) {
            user = User.getUser(AppConfig.getDatabase(),userId);
        }
        model.addAttribute("user",user);
    }

    static void setPageModule(Model model, PageModule pageModule) {
        model.addAttribute("module", pageModule.toString());
    }

    public enum PageModule {
        none("none"),
        home("home"),
        login("login");

        private String stringVal;

        private PageModule(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() { return this.stringVal; }
    }

}
