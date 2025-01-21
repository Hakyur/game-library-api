package ru.rogotovskiy.game_library_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO для создания игры")
public record CreateGameDTO(
        @Schema(description = "Название игры", example = "Counter-Strike: Global Offensive")
        String name,
        @Schema(
                description = "Описание игры",
                example = "Многопользовательская компьютерная игра, разработанная компаниями Valve" +
                        " и Hidden Path Entertainment..."
        )
        String description,
        @Schema(description = "Год выпуска игры", example = "2012")
        Integer releaseYear,
        @Schema(description = "Уникальный идентификатор жанра, которому принадлежит игра", example = "1")
        Integer genreId,
        @Schema(
                description = "Список уникальных идентификаторов платформ, для которых выпущена игра",
                example = "[1, 3, 5]"
        )
        List<Integer> platformsId
) {}
