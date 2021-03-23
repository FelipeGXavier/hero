package com.example.demo.walking.domain;

import com.example.demo.walking.domain.valueobject.CEP;
import com.example.demo.walking.domain.valueobject.Email;
import com.example.demo.walking.infra.mappers.CepConverter;
import com.example.demo.walking.infra.mappers.EmailConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Entity
@NoArgsConstructor
@Getter
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = CepConverter.class)
    private CEP cep;

    @Convert(converter = EmailConverter.class)
    private Email email;

    private String name;
    private String street;
    private int residencyNumber;

    private Owner(CEP cep, Email email, String name, String street, int residencyNumber) {
        this.cep = cep;
        this.email = email;
        this.name = name;
        this.street = street;
        this.residencyNumber = residencyNumber;
    }

    public static Owner of(
            @NonNull CEP cep,
            @NonNull Email email,
            @NotBlank String name,
            @NotBlank String street,
            @Positive int residencyNumber) {
        return new Owner(cep, email, name, street, residencyNumber);
    }
}
