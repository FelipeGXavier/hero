package com.example.demo.walking.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 30)
    private String breed;

    @Column(length = 150)
    private String restrictions;

    @ManyToOne private Owner owner;

    public Pet(String name, String breed, String restrictions, Owner owner) {
        this.name = name;
        this.breed = breed;
        this.restrictions = restrictions;
        this.owner = owner;
    }

    public static Pet of(String name, String breed, String restrictions, Owner owner) {
        Assert.notNull(name, "Pet name can't be null");
        return new Pet(name, breed, restrictions, owner);
    }
}
