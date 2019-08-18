package com.casic.atp.apiwrapper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/hello")
public class HelloController {
    @RequestMapping(value = "/index")
    public String hello(Model model) {
        model.addAttribute("name", "Dear");
        return "hello";
    }
}