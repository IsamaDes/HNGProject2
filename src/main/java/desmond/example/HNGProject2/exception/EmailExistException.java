package desmond.example.HNGProject2.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmailExistException extends RuntimeException {
    public EmailExistException(String message){
        super(message);
    }
}