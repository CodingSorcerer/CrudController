package net.lonewolfcode.opensource.springutilities.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * this exception is thrown when there is a problem in the TypeConversionService. Since this is usually used in a git command where
 * the id field is in the URL, typically this will throw a 404 if not caught and handled.
 *
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 * @author Rick Marczak
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TypeConversionError extends Exception {
    /**
     * creates a message based on the input.
     * @param input the string that was trying to be converted
     * @param target the name of the class that the string was being converted to
     */
    public TypeConversionError(String input, String target) {
        super(String.format("Error converting string \"%s\" to type \"%s\"", input, target));
    }

    public TypeConversionError(String message){
        super(message);
    }
}
