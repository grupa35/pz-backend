package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.HelloWorld;

@RestController
public class HelloWorldController {

    @RequestMapping("/")
    public HelloWorld welcome()
    {
        return new HelloWorld("Hello, World!");
    }
}
