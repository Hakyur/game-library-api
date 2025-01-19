package ru.rogotovskiy.game_library_api.exceptions;

public class DuplicateObjectException extends RuntimeException {
    public DuplicateObjectException(String message) {
        super(message);
    }
}
