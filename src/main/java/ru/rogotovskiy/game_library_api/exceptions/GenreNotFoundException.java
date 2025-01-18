package ru.rogotovskiy.game_library_api.exceptions;

public class GenreNotFoundException extends ObjectNotFoundException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}
