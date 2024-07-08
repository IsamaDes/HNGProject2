package desmond.example.HNGProject2.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import desmond.example.HNGProject2.dto.DataResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL) // Include only non-null fields
    public class SuccessResponse {
        private String status;
        private String message;
        private DataResponse data;
    }
