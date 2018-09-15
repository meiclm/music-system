package com.music.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PageForward {


    @GetMapping("/user/upPage")
    public String udPage(){
        return "user/uploadPage";
    }

    @GetMapping("/userLogin")
    public String login() {
        return "login";
    }
    @GetMapping("/index")
    public String index(){
        return "index";
    }

}
