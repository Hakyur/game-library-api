package ru.rogotovskiy.game_library_api.exceptions;

public class DuplicateGenreException extends RuntimeException {
    public DuplicateGenreException(String message) {
        super(message);
    }
}
