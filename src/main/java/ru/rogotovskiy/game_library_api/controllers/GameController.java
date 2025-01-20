package ru.rogotovskiy.game_library_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rogotovskiy.game_library_api.dto.CreateGameDTO;
import ru.rogotovskiy.game_library_api.dto.GameBasicDTO;
import ru.rogotovskiy.game_library_api.dto.SuccessResponse;
import ru.rogotovskiy.game_library_api.entity.Game;
import ru.rogotovskiy.game_library_api.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> readAllGames() {
        return ResponseEntity.ok(gameService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<?> readGame(@RequestParam Integer id) {
        return ResponseEntity.ok(gameService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGame(@RequestBody CreateGameDTO gameDTO) {
        Game game = gameService.createGame(gameDTO);
        return ResponseEntity.ok(new SuccessResponse("Игра успешно создана"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGame(@RequestParam Integer id) {
        gameService.deleteGame(id);
        return ResponseEntity.ok(new SuccessResponse("Игра успешно удалена"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateGame(@RequestParam Integer id, @RequestBody GameBasicDTO gameDTO) {
        Game game = gameService.updateGame(id, gameDTO);
        return ResponseEntity.ok(new SuccessResponse("Игра успешно обновлена"));
    }
}
