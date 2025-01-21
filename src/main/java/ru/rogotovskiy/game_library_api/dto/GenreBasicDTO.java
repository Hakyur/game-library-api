package ru.rogotovskiy.game_library_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Базовый DTO жанра (содержит только информацию о жанре)")
public record GenreBasicDTO(
        @Schema(description = "Название жанра", example = "Shooter")
        String name,
        @Schema(
                description = "Описание жанра",
                example = "Поджанр экшен-игр, где основной упор делается на стрельбу из различного оружия."
        )
        String description
) {}
