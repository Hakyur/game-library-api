package ru.rogotovskiy.game_library_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Базовый DTO игры (содержит только информацию об игре)")
public record GameBasicDTO(
        @Schema(description = "Название игры", example = "Counter-Strike: Global Offensive")
        String name,
        @Schema(
                description = "Описание игры",
                example = "Многопользовательская компьютерная игра, разработанная компаниями Valve" +
                        " и Hidden Path Entertainment..."
        )
        String description,
        @Schema(description = "Год выпуска игры", example = "2012")
        Integer releaseYear
) {}
