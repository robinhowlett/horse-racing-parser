package com.robinhowlett.parsers.exceptions;

import com.robinhowlett.exceptions.HorseRacingException;

/**
 * Superclass of all checked exceptions for this parser
 */
public class ParserException extends HorseRacingException {

    public ParserException() {
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
