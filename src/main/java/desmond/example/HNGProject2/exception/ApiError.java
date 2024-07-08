package desmond.example.HNGProject2.exception;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        String status,
        String message,
        int statusCode
){

}