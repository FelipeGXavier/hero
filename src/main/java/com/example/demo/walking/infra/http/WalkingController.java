package com.example.demo.walking.infra.http;

import com.example.demo.walking.adapters.CreateWalkRequest;
import com.example.demo.walking.common.LoadLoggedUser;
import com.example.demo.walking.domain.usecase.AssignCaregiverToWalkingUseCase;
import com.example.demo.walking.domain.usecase.CreateWalkingUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/walk")
public class WalkingController {

    private final CreateWalkingUseCase createWalking;
    private final AssignCaregiverToWalkingUseCase acceptWalking;
    private final LoadLoggedUser loadLoggedUser;

    @Autowired
    public WalkingController(
            CreateWalkingUseCase createWalking,
            AssignCaregiverToWalkingUseCase acceptWalking,
            LoadLoggedUser loadLoggedUser) {
        this.createWalking = createWalking;
        this.acceptWalking = acceptWalking;
        this.loadLoggedUser = loadLoggedUser;
    }

    @PostMapping
    public ResponseEntity<?> createWalk(@RequestBody @Valid CreateWalkRequest request) {
        var walking = this.createWalking.create(request);
        return new ResponseEntity<>(walking, HttpStatus.CREATED);
    }

    @GetMapping("/accept/{id}")
    @Transactional
    public ResponseEntity<?> acceptWalk(@PathVariable Long id) {
        var loggedCaregiver = this.loadLoggedUser.loadLoggedUser();
        this.acceptWalking.acceptWalking(loggedCaregiver, id);
        return ResponseEntity.ok().build();
    }
}
