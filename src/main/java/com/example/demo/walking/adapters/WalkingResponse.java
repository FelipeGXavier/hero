package com.example.demo.walking.adapters;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class WalkingResponse {

    private int currentPage;
    private int totalElements;
    private int totalPages;

    @JsonProperty(value = "data")
    private List<WalkingPayload> walkingPayloads;

    @Builder
    public static class WalkingPayload implements Serializable {

        private final Long id;
        private final String scheduledDate;
        private final String longitude;
        private final String latitude;
        private final int realDuration;
        private final String startDate;
        private final String finishDate;
        private final String caregiverName;
        private final String caregiverTelephone;
        private final double price;
        private final List<PetResponse> pets;
    }
}
