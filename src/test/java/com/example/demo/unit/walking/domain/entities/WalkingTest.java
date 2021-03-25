package com.example.demo.unit.walking.domain.entities;

import com.example.demo.walking.domain.Owner;
import com.example.demo.walking.domain.Pet;
import com.example.demo.walking.domain.Walking;
import com.example.demo.walking.domain.valueobject.CEP;
import com.example.demo.walking.domain.valueobject.Email;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

public class WalkingTest {

    @Test
    public void walkingWithoutPets() {
        var owner = Owner.of(new CEP("12345678"), new Email("email@a.com"), "Felipe", "", 123);
        var pet = Pet.of("Tex", "-", "-", owner);
        Walking walking =
                new Walking.WalkingBuilder()
                        .setDuration(30)
                        .setLongitude("")
                        .setLatitude("")
                        .setScheduledDate(LocalDateTime.now().minusDays(1L))
                        .setPets(Collections.singletonList(pet))
                        .build();
    }
}
