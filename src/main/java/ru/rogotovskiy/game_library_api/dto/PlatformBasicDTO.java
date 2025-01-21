package ru.rogotovskiy.game_library_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Базовый DTO платформы (содержит только информацию о платформе)")
public record PlatformBasicDTO(
        @Schema(description = "Название платформы", example = "PC")
        String name,
        @Schema(
                description = "Описание платформы",
                example = "Самая универсальная игровая платформа, предоставляющая " +
                        "широкий выбор игр для разных жанров..."
        )
        String description
) {
}
