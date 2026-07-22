package com.team3.coffee_order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/sample")
    public String sample() {
        return "sample";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
