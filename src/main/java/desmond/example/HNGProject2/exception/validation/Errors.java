package desmond.example.HNGProject2.exception.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Errors {
    private List<Error> errors;
}