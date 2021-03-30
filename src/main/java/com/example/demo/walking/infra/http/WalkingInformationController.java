package com.example.demo.walking.infra.http;

import com.example.demo.walking.adapters.WalkingResponse;
import com.example.demo.walking.domain.usecase.SearchWalkingUseCase;
import com.example.demo.walking.domain.usecase.ShowWalkingUseCase;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping("api/v1/walk")
public class WalkingInformationController {

    private final ShowWalkingUseCase showWalking;
    private final SearchWalkingUseCase<WalkingResponse> searchWalking;

    @Autowired
    public WalkingInformationController(
            ShowWalkingUseCase showWalking,
            @Qualifier("SearchWalking") SearchWalkingUseCase<WalkingResponse> searchWalking) {
        this.showWalking = showWalking;
        this.searchWalking = searchWalking;
    }

    @GetMapping("/show/{id}")
    @Transactional
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        var duration = this.showWalking.show(id);
        return new ResponseEntity<>(duration, HttpStatus.OK);
    }

    @GetMapping("index")
    public ResponseEntity<?> search(
            @RequestParam(value = "next", required = false) boolean next,
            @RequestParam(value = "page") int page)
            throws JsonProcessingException {
        return new ResponseEntity<>(
                new ObjectMapper()
                        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                        .writeValueAsString(this.searchWalking.search(next, page)),
                HttpStatus.OK);
    }
}
