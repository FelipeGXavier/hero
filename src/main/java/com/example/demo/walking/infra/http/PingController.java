package com.example.demo.walking.infra.http;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class PingController {

    @GetMapping("/ping")
    @ApiOperation(value = "Ping test endpoint")
    public String ping() {
        return "Pong";
    }
}
