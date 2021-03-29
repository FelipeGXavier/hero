package com.example.demo.walking.infra.http;

import com.example.demo.walking.adapters.CreateWalkRequest;
import com.example.demo.common.LoadLoggedUser;
import com.example.demo.walking.domain.usecase.*;
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
    private final FinishWalkingUseCase finishWalking;
    private final StartWalkingUseCase startWalking;
    private final ShowWalkingUseCase showWalkingUseCase;
    private final LoadLoggedUser loadLoggedUser;

    @Autowired
    public WalkingController(
            CreateWalkingUseCase createWalking,
            AssignCaregiverToWalkingUseCase acceptWalking,
            FinishWalkingUseCase finishWalking,
            StartWalkingUseCase startWalking,
            ShowWalkingUseCase showWalkingUseCase,
            LoadLoggedUser loadLoggedUser) {
        this.createWalking = createWalking;
        this.acceptWalking = acceptWalking;
        this.finishWalking = finishWalking;
        this.startWalking = startWalking;
        this.showWalkingUseCase = showWalkingUseCase;
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

    @GetMapping("/start/{id}")
    @Transactional
    public ResponseEntity<?> startWalk(@PathVariable("id") Long id) {
        var loggedCaregiver = this.loadLoggedUser.loadLoggedUser();
        this.startWalking.startWalk(loggedCaregiver, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/finish/{id}")
    @Transactional
    public ResponseEntity<?> finishWalk(@PathVariable("id") Long id) {
        var loggedCaregiver = this.loadLoggedUser.loadLoggedUser();
        this.finishWalking.finish(loggedCaregiver, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/show/{id}")
    @Transactional
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        var duration = this.showWalkingUseCase.show(id);
        return new ResponseEntity<>(duration, HttpStatus.OK);
    }
}
