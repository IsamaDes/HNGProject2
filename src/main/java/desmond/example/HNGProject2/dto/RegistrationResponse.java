package desmond.example.HNGProject2.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {
    private String status;
    private String message;
    private RegistrationResponseData data;
}

