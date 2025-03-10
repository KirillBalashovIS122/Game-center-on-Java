package com.gamecenter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {
    @GetMapping("/status")
    public String status() {
        return "Игровой центр работает!";
    }
}