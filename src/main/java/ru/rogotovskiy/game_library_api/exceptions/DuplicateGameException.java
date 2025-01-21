package ru.rogotovskiy.game_library_api.exceptions;

public class DuplicateGameException extends DuplicateObjectException {
    public DuplicateGameException(String message) {
        super(message);
    }
}
