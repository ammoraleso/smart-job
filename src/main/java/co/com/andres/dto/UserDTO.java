package co.com.andres.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    @JsonIgnore
    private Long id;

    @NotNull(message = "El campo 'name' es obligatorio")
    private String name;
    @NotNull(message = "El campo 'email' es obligatorio")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Ingrese un formato correcto de email"
    )
    private String email;
    @NotNull(message = "El campo 'password' es obligatorio")
    private String password;
    @NotNull(message = "El campo 'phones' es obligatorio")
    private List<PhoneDTO> phones;
    private Date createdAt;
    private Date updatedAt;
    private Boolean active;
}