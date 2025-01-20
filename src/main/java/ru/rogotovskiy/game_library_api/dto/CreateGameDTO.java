package ru.rogotovskiy.game_library_api.dto;

import java.util.List;

public record CreateGameDTO(
        String name,
        String description,
        Integer releaseYear,
        Integer genreId,
        List<Integer> platformsId
) {}
