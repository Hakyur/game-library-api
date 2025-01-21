package ru.rogotovskiy.game_library_api.exceptions;

public class GameNotFoundException extends ObjectNotFoundException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
