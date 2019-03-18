package web.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CropNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CropNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String CropNotFoundHandler(CropNotFoundException ex)
    {
        return ex.getMessage();
    }
}
