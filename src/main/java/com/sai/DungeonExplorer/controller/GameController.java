package com.sai.DungeonExplorer.controller;

import com.sai.DungeonExplorer.dto.RegisterResponseDto;
import com.sai.DungeonExplorer.dto.GameResponseDto;
import com.sai.DungeonExplorer.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/game", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class GameController {

    private final GameService service;

    @PostMapping("/register")
    public RegisterResponseDto register(@RequestParam String name) {
        return service.registerPlayer(name);
    }

    @GetMapping("/move")
    public GameResponseDto move(@RequestParam String direction) {
        return service.move(direction);
    }

    @GetMapping("/use-potion")
    public GameResponseDto potion() {
        return service.usePotion();
    }

    @GetMapping("/escape")
    public GameResponseDto escape() {
        return service.escape();
    }

    @GetMapping("/status")
    public GameResponseDto status() {
        return service.status();
    }
}