package ru.rogotovskiy.game_library_api.dto;

import java.util.List;

public record GameDTO (
        String name,
        String description,
        Integer releaseYear,
        GenreBasicDTO genre,
        List<PlatformBasicDTO> platforms
) {}
