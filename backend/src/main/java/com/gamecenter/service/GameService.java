package com.gamecenter.service;

import com.gamecenter.model.Game;
import com.gamecenter.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameByName(String name) {
        return gameRepository.findByName(name);
    }
}
