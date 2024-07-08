package desmond.example.HNGProject2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
}