package com.example.demo.util;

import com.example.demo.walking.adapters.CreateWalkRequest;
import com.example.demo.walking.domain.entity.Pet;
import com.example.demo.walking.domain.entity.Walking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TestFactory {

    public static CreateWalkRequest createWalkRequest( int duration, String latitude, String longitude, List<Long> pets) {
        var scheduledDate = LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.SECONDS);
        return new CreateWalkRequest(scheduledDate, duration, latitude, longitude, pets);
    }

    public static Walking createWalking(
            LocalDateTime scheduledDate,
            String latitude,
            String longitude,
            int duration,
            List<Pet> pets) {
        return new Walking.WalkingBuilder()
                .setScheduledDate(scheduledDate)
                .setLongitude(longitude)
                .setLatitude(latitude)
                .setDuration(duration)
                .setPets(pets)
                .build();
    }
}
