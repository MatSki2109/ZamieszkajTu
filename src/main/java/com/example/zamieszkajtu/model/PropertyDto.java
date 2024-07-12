package com.example.zamieszkajtu.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDto {
    @NotEmpty(message = "Tytuł ogłoszenia jest wymagany")
    private String titleProperty;

    @NotEmpty(message = "Kategoria ogłoszenia jest wymagana")
    private String categoryProperty;

    @NotEmpty(message = "Muszisz podać miasto nieruchomości")
    private String city;

    @NotEmpty(message = "Musisz podać adres nieruchomości")
    private String address;

    @Min(1)
    private double price;

    @Size(min = 10, message = "Opis musi zawierać przynajmniej 10 znaków")
    @Size(max = 2000, message = "Opis nie może zawierać więcej niż 2000 znaków")
    private String description;


    private MultipartFile imageFile;
}
