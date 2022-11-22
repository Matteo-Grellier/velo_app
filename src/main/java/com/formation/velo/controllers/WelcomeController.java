package com.formation.velo.controllers;

import com.formation.velo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller    // This means that this class is a Controller

public class WelcomeController {


    private final UserService userService;

    public WelcomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String>  welcome(){
        return  ResponseEntity.ok("Welcome");
    }


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users",userService.findAll());
        return "index";
    }
}
