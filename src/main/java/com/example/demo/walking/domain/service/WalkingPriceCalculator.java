package com.example.demo.walking.domain.service;

import com.example.demo.walking.domain.entity.Walking;

import static com.example.demo.walking.domain.entity.Walking.*;

public class WalkingPriceCalculator {

    public double calculate(int duration, int pets) {
        var total = 0.0;
        var additionalPets = pets - 1;
        if (duration == 30) {
            total = Walking.THIRTY_MINUTES_WALKING_BASE_PRICE;
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
}
