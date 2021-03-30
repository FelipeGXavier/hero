package com.example.demo.walking.domain.usecase;

public interface SearchWalkingUseCase<O> {

    O search(boolean next, int offset);
}
