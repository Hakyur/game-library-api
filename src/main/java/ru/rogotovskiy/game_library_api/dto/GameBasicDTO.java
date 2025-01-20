package ru.rogotovskiy.game_library_api.dto;

public record GameBasicDTO(
        String name,
        String description,
        Integer releaseYear
) {}
