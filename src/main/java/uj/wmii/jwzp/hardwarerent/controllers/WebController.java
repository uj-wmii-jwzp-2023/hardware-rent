package uj.wmii.jwzp.hardwarerent.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebController {
    @GetMapping("/home")
    public String home(@RequestParam(name="name", required=false, defaultValue="World") String name,Model model) {
        model.addAttribute("name", name);
        return "homepage";
    }
}
