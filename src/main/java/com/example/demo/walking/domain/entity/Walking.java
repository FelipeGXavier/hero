package com.example.demo.walking.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Walking {

    static final int THIRTY_MINUTES_WALKING_BASE_PRICE = 25;
    static final int SIXTY_MINUTES_WALKING_BASE_PRICE = 35;
    static final int THIRTY_MINUTES_WALKING_ADDITIONAL_PRICE = 15;
    static final int SIXTY_MINUTES_WALKING_ADDITIONAL_PRICE = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    private double price;

    @Column(nullable = false)
    private int duration;

    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    @Enumerated(EnumType.ORDINAL)
    private WalkingStatus status;

    private String latitude;
    private String longitude;

    @ManyToOne private Caregiver caregiver;

    @ManyToMany
    @JoinTable(
            name = "dog_walking",
            joinColumns = @JoinColumn(name = "walking_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id", referencedColumnName = "id"))
    private List<Pet> pets;

    private Walking(
            @NonNull LocalDateTime scheduledDate,
            @Positive int duration,
            @NotBlank String latitude,
            @NotBlank String longitude,
            List<Pet> pets) {
        this.scheduledDate = scheduledDate;
        this.duration = duration;
        this.status = WalkingStatus.PENDING;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pets = pets;
        this.price = this.calculateWalkingPrice();
    }

    public void acceptWalk(Caregiver caregiver) {
        this.status = WalkingStatus.ACCEPTED;
        this.caregiver = caregiver;
    }

    public void startWalk() {
        if (this.startDate == null
                && this.finishDate == null
                && this.status == WalkingStatus.ACCEPTED
                && this.caregiver != null) {
            this.startDate = LocalDateTime.now();
        } else {
            throw new RuntimeException("Error while starting this walking");
        }
    }

    public void finishWalk() {
        if (this.startDate != null
                && this.finishDate == null
                && this.status == WalkingStatus.ACCEPTED
                && this.caregiver != null) {
            this.finishDate = LocalDateTime.now();
        } else {
            throw new RuntimeException("Error while starting this walking");
        }
    }

    private double calculateWalkingPrice() {
        var total = 0.0;
        var pets = this.pets.size();
        var duration = this.duration;
        var additionalPets = pets - 1;
        if (duration == 30) {
            total = THIRTY_MINUTES_WALKING_BASE_PRICE;
            total +=
                    additionalPets > 0
                            ? additionalPets * THIRTY_MINUTES_WALKING_ADDITIONAL_PRICE
                            : 0.0;
        } else if (duration == 60) {
            total = SIXTY_MINUTES_WALKING_BASE_PRICE;
            total +=
                    additionalPets > 0
                            ? additionalPets * SIXTY_MINUTES_WALKING_ADDITIONAL_PRICE
                            : 0.0;
        }
        return total;
    }

    public static class WalkingBuilder {

        private LocalDateTime scheduledDate;
        private float price;
        private int duration;
        private LocalDateTime startDate;
        private LocalDateTime finishDate;
        private WalkingStatus status;
        private String latitude;
        private String longitude;
        private Caregiver caregiver;
        private List<Pet> pets;

        public WalkingBuilder setScheduledDate(LocalDateTime scheduledDate) {
            this.scheduledDate = scheduledDate;
            return this;
        }

        public WalkingBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public WalkingBuilder setLatitude(String latitude) {
            this.latitude = latitude;
            return this;
        }

        public WalkingBuilder setLongitude(String longitude) {
            this.longitude = longitude;
            return this;
        }

        public WalkingBuilder setCaregiver(Caregiver caregiver) {
            this.caregiver = caregiver;
            return this;
        }

        public WalkingBuilder setPets(List<Pet> pets) {
            this.pets = pets;
            return this;
        }

        public Walking build() {
            Assert.state(pets.size() > 0, "Walking must have at least one dog");
            Assert.notNull(scheduledDate, "Scheduled date can't be null");
            Assert.state(
                    scheduledDate.isAfter(LocalDateTime.now()),
                    "Scheduled date must be greater than now");
            Assert.state(duration == 30 || duration == 60, "Duration must be 30 or 60 minutes");
            return new Walking(scheduledDate, duration, latitude, longitude, pets);
        }
    }
}