package ru.rogotovskiy.game_library_api.dto;

public record ErrorResponse(
        String message,
        String time
) {}
