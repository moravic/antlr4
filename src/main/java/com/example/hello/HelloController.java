package com.example.hello;

import com.example.util.ParseSQLUtil;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() throws Exception {

        ParseSQLUtil.parse();

        return "Greetings from Spring Boot!";
    }



}
