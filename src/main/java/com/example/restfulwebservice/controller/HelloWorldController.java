package com.example.restfulwebservice.controller;

import com.example.restfulwebservice.dto.HelloWorldBean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RequiredArgsConstructor
@RestController
public class HelloWorldController {
    private final MessageSource messageSource;

    // GET
    // /hello-world (endpoint)
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "helloWorld";
    }

    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello world");
    }

    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name) {
        return new HelloWorldBean("Hello world, " + name);
    }

    @GetMapping("/hello-world-internationalized")
    public String helloWorldInternationalized(
            @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        return messageSource.getMessage("greeting.message", null, locale);
    }
}