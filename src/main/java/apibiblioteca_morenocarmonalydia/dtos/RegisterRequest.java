package apibiblioteca_morenocarmonalydia.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        String nombre,

        @NotBlank
        String apellidos,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 6, message = "Mínimo 6 caracteres")
        String password
) {}
