package org.acme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.acme.entity.enumerations.RolesEnum;

import java.time.LocalDate;

@Entity
@Table(name="users")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;
    private String phone;
    private LocalDate birthdate;

    @Column(nullable = false)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private RolesEnum role;
}
