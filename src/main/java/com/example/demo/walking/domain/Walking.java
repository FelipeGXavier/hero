package com.example.demo.walking.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Walking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime scheduledDate;

    private float price;

    @Column(nullable = false)
    private int duration;

    private LocalDateTime startDate;
    private LocalDateTime finishDate;
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

    private Walking(LocalDateTime scheduledDate, float price, int duration, LocalDateTime startDate, LocalDateTime finishDate, WalkingStatus status, String latitude, String longitude, Caregiver caregiver, List<Pet> pets) {
        this.scheduledDate = scheduledDate;
        this.price = price;
        this.duration = duration;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.caregiver = caregiver;
        this.pets = pets;
    }

    static class WalkingBuilder {

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

        public WalkingBuilder setPrice(float price) {
            this.price = price;
            return this;
        }

        public WalkingBuilder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public WalkingBuilder setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public WalkingBuilder setFinishDate(LocalDateTime finishDate) {
            this.finishDate = finishDate;
            return this;
        }

        public WalkingBuilder setStatus(WalkingStatus status) {
            this.status = status;
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

        public Walking createWalking() {
            return new Walking(scheduledDate, price, duration, startDate, finishDate, status, latitude, longitude, caregiver, pets);
        }
    }
}
