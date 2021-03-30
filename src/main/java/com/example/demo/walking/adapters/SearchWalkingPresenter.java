package com.example.demo.walking.adapters;

import com.example.demo.common.Presenter;
import com.example.demo.walking.domain.entity.Walking;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class SearchWalkingPresenter implements Presenter<Page<Walking>, WalkingResponse> {

    @Override
    public WalkingResponse handle(Page<Walking> input) {
        List<WalkingResponse.WalkingPayload> walkings = new ArrayList<>();
        var result = new WalkingResponse();
        result.setCurrentPage(input.getNumber());
        result.setTotalElements((int) input.getTotalElements());
        result.setTotalPages(input.getTotalPages());
        var builder = new WalkingResponse.WalkingPayload.WalkingPayloadBuilder();
        input.getContent()
                .forEach(
                        walking -> {
                            var caregiver = walking.getCaregiver();
                            var walkingResponseBuilder =
                                    builder.id(walking.getId())
                                            .caregiverName(
                                                    caregiver != null ? caregiver.getName() : null)
                                            .caregiverTelephone(
                                                    caregiver != null
                                                            ? caregiver.getTelephone().getValue()
                                                            : null)
                                            .scheduledDate(walking.getScheduledDate().toString())
                                            .startDate(walking.getStartDate() != null ? walking.getStartDate().toString() : null)
                                            .finishDate(walking.getFinishDate()  != null ? walking.getFinishDate().toString() : null)
                                            .realDuration(walking.getRealDuration())
                                            .price(walking.getPrice())
                                            .longitude(walking.getLongitude())
                                            .latitude(walking.getLatitude());
                            List<PetResponse> pets = new ArrayList<>();
                            walking.getPets()
                                    .forEach(
                                            pet ->
                                                    pets.add(
                                                            new PetResponse.PetResponseBuilder()
                                                                    .id(pet.getId())
                                                                    .name(pet.getName())
                                                                    .breed(pet.getBreed())
                                                                    .build()));
                            walkingResponseBuilder.pets(pets);
                            walkings.add(walkingResponseBuilder.build());
                        });
        result.setWalkingPayloads(walkings);
        return result;
    }
}
