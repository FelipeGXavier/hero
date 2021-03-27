package com.example.demo.walking.infra.http;

import com.example.demo.walking.adapters.CreateWalkRequest;
import com.example.demo.walking.domain.usecase.CreateWalkingUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/walk")
public class WalkingController {

    private final CreateWalkingUseCase createWalking;

    @Autowired
    public WalkingController(CreateWalkingUseCase createWalking) {
        this.createWalking = createWalking;
    }

    @PostMapping
    public ResponseEntity<?> createWalk(@RequestBody @Valid CreateWalkRequest request) {
        var walking = this.createWalking.create(request);
        return new ResponseEntity<>(walking, HttpStatus.CREATED);
    }
}
