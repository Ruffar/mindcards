package com.raffier.mindcards;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "<!DOCTYPE html><html><body><p>This is a paragraph.</p><p>This is a paragraph.</p><p>This is a paragraph.</p></body></html>";
    }
    
}
