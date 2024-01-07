package org.acme.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DTOUser {

    private Long id;

    @NotBlank(message = "'name' cannot be blank")
    private String name;
    private String phone;
    private LocalDate birthdate;

    @NotBlank(message = "'email' cannot be blank")
    @Email
    private String email;
    //private String password;

    /**********/

    private Integer age;

}
