package cn.hit.joker.newmsdivide.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author joker
 * @version 1.0
 * @date 2021/5/20 15:14
 * @description hello page
 */

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String helloPage() {
        return "hello";
    }
}
