package com.example.demo.walking.domain.entity;

import com.example.demo.walking.domain.valueobject.Email;
import com.example.demo.walking.domain.valueobject.Telephone;
import com.example.demo.walking.infra.converters.EmailConverter;
import com.example.demo.walking.infra.converters.TelephoneConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@Getter
public class Caregiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = TelephoneConverter.class)
    private Telephone telephone;

    @Convert(converter = EmailConverter.class)
    private Email email;

    private String name;

    private Caregiver(Telephone telephone, Email email, String name) {
        this.telephone = telephone;
        this.email = email;
        this.name = name;
    }

    public void acceptWalking(Walking walking) {
        walking.acceptWalk(this);
    }

    public static Caregiver of(
            @NonNull Telephone telephone, @NonNull Email email, @NotBlank String name) {
        Assert.isTrue(name.length() > 3, "Name of caregiver must have at least 3 characters");
        return new Caregiver(telephone, email, name);
    }
}
