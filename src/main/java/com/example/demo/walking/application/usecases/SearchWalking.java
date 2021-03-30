package com.example.demo.walking.application.usecases;

import com.example.demo.common.Presenter;
import com.example.demo.walking.adapters.WalkingResponse;
import com.example.demo.walking.domain.entity.Walking;
import com.example.demo.walking.domain.usecase.SearchWalkingUseCase;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchWalking implements SearchWalkingUseCase<WalkingResponse> {

    private Presenter<Page<Walking>, WalkingResponse> presenter;
    private WalkingRepository walkingRepository;

    @Autowired
    public SearchWalking(
            Presenter<Page<Walking>, WalkingResponse> presenter,
            WalkingRepository walkingRepository) {
        this.presenter = presenter;
        this.walkingRepository = walkingRepository;
    }

    @Override
    public WalkingResponse search(boolean next, int offset) {
        Pageable pageable = PageRequest.of(offset, 10);
        var result = this.walkingRepository.findAll(pageable);
        return this.presenter.handle(result);
    }
}
