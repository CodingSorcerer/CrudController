package net.lonewolfcode.opensource.springutilities.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.IOException;

/**
 * This exception expresses that the JSON sent in a request body is not what is expected or malformed.
 *
 * @author Rick Marczak
 */

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BadRequestBodyException extends IOException {
    /**
     * Create an empty error object
     */
    public BadRequestBodyException(){
        super();
    }
}
