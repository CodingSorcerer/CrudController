package net.lonewolfcode.opensource.springutilities.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is to express something not being found or a 404. This error should translate to
 * 404 when you implement controller advice.
 *
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 * @author Rick Marczak
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {
    /**
     * Create an empty error object
     */
    public NotFoundException(){
        super();
    }

    /**
     * Create an error object with a message.
     * @param message error message
     */
    public NotFoundException(String message){
        super(message);
    }
}
