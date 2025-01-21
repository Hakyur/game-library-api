package ru.rogotovskiy.game_library_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rogotovskiy.game_library_api.dto.CreateGameDTO;
import ru.rogotovskiy.game_library_api.dto.GameBasicDTO;
import ru.rogotovskiy.game_library_api.dto.SuccessResponse;
import ru.rogotovskiy.game_library_api.entity.Game;
import ru.rogotovskiy.game_library_api.service.GameService;

@Tag(name = "Игры", description = "Контроллер для работы с играми")
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Получить список всех игр")
    @GetMapping("/get/all")
    public ResponseEntity<?> readAllGames() {
        return ResponseEntity.ok(gameService.getAll());
    }

    @Operation(summary = "Получить конкретную игру")
    @GetMapping("/get")
    public ResponseEntity<?> readGame(@RequestParam Integer id) {
        return ResponseEntity.ok(gameService.getById(id));
    }

    @Operation(summary = "Создать (добавить) игру")
    @PostMapping("/create")
    public ResponseEntity<?> createGame(@RequestBody CreateGameDTO gameDTO) {
        Game game = gameService.createGame(gameDTO);
        return ResponseEntity.ok(new SuccessResponse("Игра успешно создана"));
    }

    @Operation(summary = "Удалить игру")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGame(@RequestParam Integer id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok(new SuccessResponse("Игра успешно удалена"));
    }

    @Operation(summary = "Обновить информацию об игре")
    @PutMapping("/update")
    public ResponseEntity<?> updateGame(@RequestParam Integer id, @RequestBody GameBasicDTO gameDTO) {
        Game game = gameService.updateGame(id, gameDTO);
        return ResponseEntity.ok(new SuccessResponse("Игра успешно обновлена"));
    }
}
