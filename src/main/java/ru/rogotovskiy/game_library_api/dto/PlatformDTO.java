package ru.rogotovskiy.game_library_api.dto;

import java.util.List;

public record PlatformDTO (
        String name,
        String description,
        List<GameBasicDTO> games
) {}
