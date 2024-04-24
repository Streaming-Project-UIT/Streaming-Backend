package com.programming.streaming.controller.Client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class TestController {
    @GetMapping("/hello-world")
    @ResponseBody
    public String test() {
        return "hello-world";
    }
}
