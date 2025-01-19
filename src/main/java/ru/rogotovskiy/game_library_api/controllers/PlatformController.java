package ru.rogotovskiy.game_library_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rogotovskiy.game_library_api.dto.PlatformBasicDTO;
import ru.rogotovskiy.game_library_api.dto.SuccessResponse;
import ru.rogotovskiy.game_library_api.entity.Platform;
import ru.rogotovskiy.game_library_api.service.PlatformService;

@RestController
@RequestMapping("/platform")
public class PlatformController {
    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> readAllPlatforms() {
        return ResponseEntity.ok(platformService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<?> readPlatform(@RequestParam Integer id) {
        return ResponseEntity.ok(platformService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlatform(@RequestBody PlatformBasicDTO platformDTO) {
        Platform platform = platformService.createPlatform(platformDTO);
        return ResponseEntity.ok(new SuccessResponse("Платформа успешно добавлена"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePlatform(@RequestParam Integer id) {
        platformService.deletePlatform(id);
        return ResponseEntity.ok(new SuccessResponse("Платформа успешно удалена"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePlatform(@RequestParam Integer id, @RequestBody PlatformBasicDTO platformDTO) {
        Platform platform = platformService.updatePlatform(id, platformDTO);
        return ResponseEntity.ok(new SuccessResponse("Платформа успешно обновлена"));
    }
}
