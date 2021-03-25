package com.example.demo.walking.infra.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class PingController {

    @GetMapping
    public String ping() {
        return "Pong";
    }
}