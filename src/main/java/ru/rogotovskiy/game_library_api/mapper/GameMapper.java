package ru.rogotovskiy.game_library_api.mapper;

import org.springframework.stereotype.Component;
import ru.rogotovskiy.game_library_api.dto.CreateGameDTO;
import ru.rogotovskiy.game_library_api.dto.GameBasicDTO;
import ru.rogotovskiy.game_library_api.entity.Game;

import java.util.Collections;

@Component
public class GameMapper {

    public GameBasicDTO toBasicDTO(Game game) {
        return new GameBasicDTO(game.getName(), game.getDescription(), game.getReleaseYear());
    }

    public Game toEntity(GameBasicDTO gameDTO) {
        return new Game(
                null,
                gameDTO.name(),
                gameDTO.description(),
                gameDTO.releaseYear(),
                null,
                Collections.emptyList()
        );
    }

    public Game toEntity(CreateGameDTO gameDTO) {
        return new Game(
                null,
                gameDTO.name(),
                gameDTO.description(),
                gameDTO.releaseYear(),
                null,
                Collections.emptyList()
        );
    }
}
