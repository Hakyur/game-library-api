package ru.rogotovskiy.game_library_api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovskiy.game_library_api.dto.CreateGameDTO;
import ru.rogotovskiy.game_library_api.dto.GameBasicDTO;
import ru.rogotovskiy.game_library_api.dto.GameDTO;
import ru.rogotovskiy.game_library_api.entity.Game;
import ru.rogotovskiy.game_library_api.entity.Genre;
import ru.rogotovskiy.game_library_api.entity.Platform;
import ru.rogotovskiy.game_library_api.exceptions.DuplicateGameException;
import ru.rogotovskiy.game_library_api.exceptions.GameNotFoundException;
import ru.rogotovskiy.game_library_api.mapper.FacadeMapper;
import ru.rogotovskiy.game_library_api.mapper.GameMapper;
import ru.rogotovskiy.game_library_api.repository.GameRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlatformService platformService;

    @Mock
    private GenreService genreService;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private FacadeMapper facadeMapper;

    @InjectMocks
    private GameService gameService;

    private static final Integer GAME_ID = 1;
    private static final String GAME_NAME = "World of Warcraft";
    private static final String GAME_DESCRIPTION = "Массовая многопользовательская ролевая онлайн-игра," +
            " разработанная и издаваемая компанией Blizzard Entertainment.";
    private static final Integer GAME_RELEASE_YEAR = 2004;

    private static final String GAME_NOT_FOUND_TEMPLATE = "Игра с id = %d не найдена!";
    private static final String DUPLICATE_GAME_TEMPLATE = "Игра с названием %s уже существует!";

    private Game game;
    private GameDTO gameDTO;
    private CreateGameDTO createGameDTO;
    private GameBasicDTO gameBasicDTO;

    @BeforeEach
    void setUp() {
        game = new Game(GAME_ID, GAME_NAME, GAME_DESCRIPTION, GAME_RELEASE_YEAR, null, Collections.emptyList());
        gameDTO = new GameDTO(GAME_NAME, GAME_DESCRIPTION, GAME_RELEASE_YEAR, null, Collections.emptyList());
        createGameDTO = new CreateGameDTO(GAME_NAME, GAME_DESCRIPTION, GAME_RELEASE_YEAR, 1, List.of(1, 2));
    }

    @Test
    void getAll_ShouldReturnListOfGames() {
        when(gameRepository.findAll()).thenReturn(List.of(game));

        List<Game> result = gameService.getAll();

        assertEquals(1, result.size());
        assertEquals(game, result.get(0));
        verify(gameRepository).findAll();
    }

    @Test
    void getGameById_ShouldReturnGame_WhenExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        Game result = gameService.getGameById(GAME_ID);

        assertEquals(game, result);
        verify(gameRepository).findById(GAME_ID);
    }

    @Test
    void getGameById_ShouldThrowException_WhenNotExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        GameNotFoundException e = assertThrows(
                GameNotFoundException.class,
                () -> gameService.getGameById(GAME_ID)
        );

        assertEquals(String.format(GAME_NOT_FOUND_TEMPLATE, GAME_ID), e.getMessage());
        verify(gameRepository).findById(GAME_ID);
    }

    @Test
    void getById_ShouldReturnGameDTO_WhenExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));
        when(facadeMapper.toDTO(game)).thenReturn(gameDTO);

        GameDTO result = gameService.getById(GAME_ID);

        assertEquals(gameDTO, result);
        verify(gameRepository).findById(GAME_ID);
        verify(facadeMapper).toDTO(game);
    }

    @Test
    void getById_ShouldThrowException_WhenNotExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        GameNotFoundException e = assertThrows(
                GameNotFoundException.class,
                () -> gameService.getById(GAME_ID)
        );

        assertEquals(String.format(GAME_NOT_FOUND_TEMPLATE, GAME_ID), e.getMessage());
        verify(gameRepository).findById(GAME_ID);
        verify(facadeMapper, never()).toDTO(game);
    }

    @Test
    void createGame_ShouldSaveAndReturnGame() {
        Genre genre = new Genre(1, "MMO", "genre description", Collections.emptyList());
        Platform platform1 = new Platform(1, "platform 1", "description of platform 1", Collections.emptyList());
        Platform platform2 = new Platform(2, "platform 2", "description of platform 2", Collections.emptyList());

        when(gameRepository.existsByName(GAME_NAME)).thenReturn(false);
        when(gameMapper.toEntity(createGameDTO)).thenReturn(game);
        when(genreService.getGenreById(1)).thenReturn(genre);
        when(platformService.getPlatformById(1)).thenReturn(platform1);
        when(platformService.getPlatformById(2)).thenReturn(platform2);
        when(gameRepository.save(game)).thenReturn(game);

        Game result = gameService.createGame(createGameDTO);

        assertEquals(game, result);
        verify(gameRepository).existsByName(GAME_NAME);
        verify(gameMapper).toEntity(createGameDTO);
        verify(genreService).getGenreById(1);
        verify(platformService).getPlatformById(1);
        verify(platformService).getPlatformById(2);
        verify(gameRepository).save(game);
    }

    @Test
    void createGame_ShouldThrowException_WhenNameExists() {
        when(gameRepository.existsByName(GAME_NAME)).thenReturn(true);

        DuplicateGameException e = assertThrows(
                DuplicateGameException.class,
                () -> gameService.createGame(createGameDTO)
        );

        assertEquals(String.format(DUPLICATE_GAME_TEMPLATE, GAME_NAME), e.getMessage());
        verify(gameRepository).existsByName(GAME_NAME);
        verify(gameMapper, never()).toEntity(createGameDTO);
        verify(genreService, never()).getGenreById(1);
        verify(platformService, never()).getPlatformById(1);
        verify(platformService, never()).getPlatformById(2);
        verify(gameRepository, never()).save(game);
    }

    @Test
    void deleteGame_ShouldDeleteGame_WhenExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        gameService.deleteGame(GAME_ID);

        verify(gameRepository).findById(GAME_ID);
        verify(gameRepository).delete(game);
    }

    @Test
    void deleteGame_ShouldThrowException_WhenNotExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        GameNotFoundException e = assertThrows(
                GameNotFoundException.class,
                () -> gameService.deleteGame(GAME_ID)
        );

        assertEquals(String.format(GAME_NOT_FOUND_TEMPLATE, GAME_ID), e.getMessage());
        verify(gameRepository).findById(GAME_ID);
        verify(gameRepository, never()).delete(game);
    }

    @Test
    void updateGame_ShouldUpdateAndReturnGame() {
        Game updatedGame = new Game(1, "Witcher 3", "New description", 2015, null, Collections.emptyList());

        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));
        when(gameRepository.existsByName("Witcher 3")).thenReturn(false);
        when(gameRepository.save(game)).thenReturn(updatedGame);

        gameBasicDTO = new GameBasicDTO("Witcher 3", "New description", 2015);
        Game result = gameService.updateGame(GAME_ID, gameBasicDTO);

        assertEquals(updatedGame.getName(), result.getName());
        assertEquals(updatedGame.getDescription(), result.getDescription());
        assertEquals(updatedGame.getReleaseYear(), result.getReleaseYear());
        verify(gameRepository).findById(GAME_ID);
        verify(gameRepository).existsByName("Witcher 3");
        verify(gameRepository).save(game);
    }

    @Test
    void updateGame_ShouldThrowException_WhenNotExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.empty());

        gameBasicDTO = new GameBasicDTO("Witcher 3", "New description", 2015);

        GameNotFoundException e = assertThrows(
                GameNotFoundException.class,
                () -> gameService.updateGame(GAME_ID, gameBasicDTO)
        );

        assertEquals(String.format(GAME_NOT_FOUND_TEMPLATE, GAME_ID), e.getMessage());
        verify(gameRepository).findById(GAME_ID);
        verify(gameRepository, never()).existsByName("Witcher 3");
        verify(gameRepository, never()).save(game);
    }

    @Test
    void updateGame_ShouldThrowException_WhenNameExists() {
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));
        when(gameRepository.existsByName("Witcher 3")).thenReturn(true);

        gameBasicDTO = new GameBasicDTO("Witcher 3", "New description", 2015);

        DuplicateGameException e = assertThrows(
                DuplicateGameException.class,
                () -> gameService.updateGame(GAME_ID, gameBasicDTO)
        );

        assertEquals(String.format(DUPLICATE_GAME_TEMPLATE, "Witcher 3"), e.getMessage());
        verify(gameRepository).findById(GAME_ID);
        verify(gameRepository).existsByName("Witcher 3");
        verify(gameRepository, never()).save(game);
    }
}