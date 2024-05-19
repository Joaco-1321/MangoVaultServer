package io.joaco.mangovaultserver.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserData {

    @Pattern(regexp = "^[a-zA-Z]\\w{2,}[a-zA-Z0-9]$",
             message = """
                       must be at least 4 characters long.
                       must begin with a letter.
                       can contain letters, numbers and "_".
                       must not end with an "_".""")
    private String username;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\s:])(\\S){8,16}$",
             message = """
                       must be between 8 and 16 characters long with no space.
                       must contain at least 1 number.
                       must contain at least 1 uppercase letter.
                       must contain at least 1 lowercase letter.
                       must contain at least 1 non-alphanumeric character.""")
    private String password;
}
