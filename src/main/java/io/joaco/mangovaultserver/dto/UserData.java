package io.joaco.mangovaultserver.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserData {

    @Pattern(regexp = "^[a-zA-Z]\\w{2,}[a-zA-Z0-9]$")
    private String username;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\\w\\s:])(\\S){8,16}$")
    private String password;
}
