package ru.rogotovskiy.game_library_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovskiy.game_library_api.dto.CreateGameDTO;
import ru.rogotovskiy.game_library_api.dto.GameBasicDTO;
import ru.rogotovskiy.game_library_api.entity.Game;
import ru.rogotovskiy.game_library_api.repository.GameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public List<Game> getAll() {
        return null;
    }

    public Object getById(Integer id) {
        return null;
    }

    public Game createGame(CreateGameDTO gameDTO) {
        return null;
    }

    public void deleteGame(Integer id) {

    }

    public Game updateGame(Integer id, GameBasicDTO gameDTO) {
        return null;
    }
}
