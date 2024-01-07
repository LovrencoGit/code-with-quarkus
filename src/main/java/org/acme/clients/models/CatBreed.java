package org.acme.clients.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatBreed {
    private String breed;
    private String country;
    private String origin;
    private String coat;
    private String pattern;
}
