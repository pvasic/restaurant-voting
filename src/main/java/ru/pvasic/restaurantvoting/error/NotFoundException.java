package ru.pvasic.restaurantvoting.error;

public class NotFoundException extends IllegalRequestDataException {
    public NotFoundException(String msg) {
        super(msg);
    }
}