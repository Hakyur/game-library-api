package ru.rogotovskiy.game_library_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rogotovskiy.game_library_api.dto.PlatformBasicDTO;
import ru.rogotovskiy.game_library_api.dto.SuccessResponse;
import ru.rogotovskiy.game_library_api.entity.Platform;
import ru.rogotovskiy.game_library_api.service.PlatformService;

@Tag(name = "Платформы", description = "Контроллер для работы с игровыми платформами")
@RestController
@RequestMapping("/platform")
public class PlatformController {
    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @Operation(summary = "Получить список всех платформ")
    @GetMapping("/get/all")
    public ResponseEntity<?> readAllPlatforms() {
        return ResponseEntity.ok(platformService.getAll());
    }

    @Operation(summary = "Получить конкретную платформу")
    @GetMapping("/get")
    public ResponseEntity<?> readPlatform(@RequestParam Integer id) {
        return ResponseEntity.ok(platformService.getById(id));
    }

    @Operation(summary = "Создать платформу")
    @PostMapping("/create")
    public ResponseEntity<?> createPlatform(@RequestBody PlatformBasicDTO platformDTO) {
        Platform platform = platformService.createPlatform(platformDTO);
        return ResponseEntity.ok(new SuccessResponse("Платформа успешно добавлена"));
    }

    @Operation(summary = "Удалить платформу")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePlatform(@RequestParam Integer id) {
        platformService.deletePlatform(id);
        return ResponseEntity.ok(new SuccessResponse("Платформа успешно удалена"));
    }

    @Operation(summary = "Обновить информацию о платформе")
    @PutMapping("/update")
    public ResponseEntity<?> updatePlatform(@RequestParam Integer id, @RequestBody PlatformBasicDTO platformDTO) {
        Platform platform = platformService.updatePlatform(id, platformDTO);
        return ResponseEntity.ok(new SuccessResponse("Платформа успешно обновлена"));
    }
}
