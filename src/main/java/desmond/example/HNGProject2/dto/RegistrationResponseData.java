package desmond.example.HNGProject2.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseData {
    private String accessToken;
    private UserResponse user;
}







