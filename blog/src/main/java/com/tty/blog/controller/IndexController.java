package com.tty.blog.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController implements ErrorController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @RequestMapping("/detail")
    public String detail(){
        return "index";
    }
    @RequestMapping("/gossip")
    public String gossip(){
        return "index";
    }
    @RequestMapping("/guestbook")
    public String guestbook(){
        return "index";
    }
    @RequestMapping("/friends")
    public String friends() {
        return "index";
    }
    @RequestMapping("/user/{uid}")
    public String userInfo(){
        return "index";
    }
    @RequestMapping("/notice")
    public String notice() {
        return "index";
    }
    @RequestMapping("/notfound")
    public String notFound(){
        return "index";
    }
    @RequestMapping("/error")
    public String errorPage() {
        return "index";
    }
}
