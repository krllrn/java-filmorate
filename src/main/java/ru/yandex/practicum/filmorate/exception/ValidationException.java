package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends Error {
    public ValidationException (final String message) {
        super(message);
    }
}
