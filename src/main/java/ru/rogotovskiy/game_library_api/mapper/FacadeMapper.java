package ru.rogotovskiy.game_library_api.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rogotovskiy.game_library_api.dto.GameDTO;
import ru.rogotovskiy.game_library_api.dto.GenreDTO;
import ru.rogotovskiy.game_library_api.dto.PlatformDTO;
import ru.rogotovskiy.game_library_api.entity.Game;
import ru.rogotovskiy.game_library_api.entity.Genre;
import ru.rogotovskiy.game_library_api.entity.Platform;

@Component
@RequiredArgsConstructor
public class FacadeMapper {

    private final GameMapper gameMapper;
    private final GenreMapper genreMapper;
    private final PlatformMapper platformMapper;

    public GameDTO toDTO(Game game) {
        return new GameDTO(
                game.getName(),
                game.getDescription(),
                game.getReleaseYear(),
                genreMapper.toBasicDTO(game.getGenre()),
                game.getPlatforms().stream()
                        .map((platformMapper::toBasicDTO))
                        .toList()
        );
    }

    public GenreDTO toDTO(Genre genre) {
        return new GenreDTO(
                genre.getName(),
                genre.getDescription(),
                genre.getGames().stream()
                        .map(gameMapper::toBasicDTO)
                        .toList()
        );
    }

    public PlatformDTO toDTO(Platform platform) {
        return new PlatformDTO(
                platform.getName(),
                platform.getDescription(),
                platform.getGames().stream()
                        .map(gameMapper::toBasicDTO)
                        .toList()
        );
    }
}
