package com.example.demo.walking.adapters;

import com.example.demo.walking.domain.entity.Walking;
import com.example.demo.walking.infra.repository.PetRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateWalkRequest {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledDate;

    @Positive private int duration;
    @NotBlank private String latitude;
    @NotBlank private String longitude;
    @NotEmpty private List<Long> pets;

    public Walking toEntity(PetRepository petRepository) {
        var foundPets = petRepository.findPetsWhereIn(this.pets);
        if (foundPets.size() != this.pets.size()) {
            throw new RuntimeException("One or more pets were not found");
        }
        return new Walking.WalkingBuilder()
                .setPets(foundPets)
                .setScheduledDate(this.scheduledDate)
                .setDuration(this.duration)
                .setLatitude(this.latitude)
                .setLongitude(this.longitude)
                .build();
    }
}
