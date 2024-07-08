package desmond.example.HNGProject2.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
    public class DefaultExceptionHandler {
        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ApiError> UsernameNotFoundException(
                UsernameNotFoundException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(EmailExistException.class)
        public ResponseEntity<ApiError> EmailIsTakenException(
                EmailExistException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(UserNotVerifiedException.class)
        public ResponseEntity<ApiError> UserNotVerifiedException(
                UserNotVerifiedException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        private ResponseEntity<ApiError> buildErrorResponse(String message, HttpStatus status) {
            ApiError apiError = new ApiError(
                    status.getReasonPhrase(),
                    message,
                    status.value()
            );
            return new ResponseEntity<>(apiError, status);
        }
}
