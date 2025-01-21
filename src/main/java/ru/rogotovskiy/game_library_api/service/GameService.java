package ru.rogotovskiy.game_library_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovskiy.game_library_api.dto.CreateGameDTO;
import ru.rogotovskiy.game_library_api.dto.GameBasicDTO;
import ru.rogotovskiy.game_library_api.dto.GameDTO;
import ru.rogotovskiy.game_library_api.entity.Game;
import ru.rogotovskiy.game_library_api.exceptions.DuplicateGameException;
import ru.rogotovskiy.game_library_api.exceptions.GameNotFoundException;
import ru.rogotovskiy.game_library_api.mapper.FacadeMapper;
import ru.rogotovskiy.game_library_api.mapper.GameMapper;
import ru.rogotovskiy.game_library_api.repository.GameRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final String GAME_NOT_FOUND_TEMPLATE = "Игра с id = %d не найдена!";
    private final String DUPLICATE_GAME_TEMPLATE = "Игра с названием %s уже существует!";
    private final GameRepository gameRepository;
    private final PlatformService platformService;
    private final GenreService genreService;
    private final FacadeMapper facadeMapper;
    private final GameMapper gameMapper;

    public List<Game> getAll() {
        return gameRepository.findAll();
    }

    public Game getGameById(Integer id) {
        return gameRepository.findById(id).orElseThrow(
                () -> new GameNotFoundException(
                        String.format(GAME_NOT_FOUND_TEMPLATE, id)
                )
        );
    }

    public GameDTO getById(Integer id) {
        Game game = getGameById(id);
        return facadeMapper.toDTO(game);
    }

    public Game createGame(CreateGameDTO gameDTO) {
        if (gameRepository.existsByName(gameDTO.name())) {
            throw new DuplicateGameException(
                    String.format(DUPLICATE_GAME_TEMPLATE, gameDTO.name())
            );
        }

        Game game = gameMapper.toEntity(gameDTO);
        game.setGenre(genreService.getGenreById(gameDTO.genreId()));
        game.setPlatforms(
                gameDTO.platformsId().stream()
                        .map(platformService::getPlatformById)
                        .toList()
        );
        return gameRepository.save(game);
    }

    public void deleteGame(Integer id) {
        Game game = getGameById(id);
        gameRepository.delete(game);
    }

    public Game updateGame(Integer id, GameBasicDTO gameDTO) {
        Game game = getGameById(id);

        if (gameDTO.name() != null && !gameDTO.name().equals(game.getName())) {
            if (gameRepository.existsByName(gameDTO.name())) {
                throw new DuplicateGameException(
                        String.format(DUPLICATE_GAME_TEMPLATE, gameDTO.name())
                );
            }
            game.setName(gameDTO.name());
        }

        if (gameDTO.description() != null) {
            game.setDescription(gameDTO.description());
        }

        if (gameDTO.releaseYear() != null) {
            game.setReleaseYear(gameDTO.releaseYear());
        }

        gameRepository.save(game);
        return game;
    }
}
