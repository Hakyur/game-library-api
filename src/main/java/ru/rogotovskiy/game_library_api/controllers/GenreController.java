package ru.rogotovskiy.game_library_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rogotovskiy.game_library_api.dto.GenreBasicDTO;
import ru.rogotovskiy.game_library_api.dto.SuccessResponse;
import ru.rogotovskiy.game_library_api.entity.Genre;
import ru.rogotovskiy.game_library_api.service.GenreService;

@Tag(name = "Жанры", description = "Контроллер для работы с жанрами игр")
@RestController
@RequestMapping("/genre")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Получить список всех жанров")
    @GetMapping("/get/all")
    public ResponseEntity<?> readAllGenres() {
        return ResponseEntity.ok(genreService.getAll());
    }

    @Operation(summary = "Получить конкретный жанр")
    @GetMapping("/get")
    public ResponseEntity<?> readGenre(@RequestParam Integer id) {
        return ResponseEntity.ok(genreService.getById(id));
    }

    @Operation(summary = "Создать жанр")
    @PostMapping("/create")
    public ResponseEntity<?> createGenre(@RequestBody GenreBasicDTO genreDTO) {
        Genre genre = genreService.createGenre(genreDTO);
        return ResponseEntity.ok(new SuccessResponse("Жанр успешно создан"));
    }

    @Operation(summary = "Удалить жанр")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGenre(@RequestParam Integer id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok(new SuccessResponse("Жанр успешно удалён"));
    }

    @Operation(summary = "Обновить информацию о жанре")
    @PutMapping("/update")
    public ResponseEntity<?> updateGenre(@RequestParam Integer id, @RequestBody GenreBasicDTO genreDTO) {
        Genre genre = genreService.updateGenre(id, genreDTO);
        return ResponseEntity.ok(new SuccessResponse("Жанр успешно обновлён"));
    }

}
