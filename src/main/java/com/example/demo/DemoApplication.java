package com.example.demo;

import com.example.demo.common.Presenter;
import com.example.demo.walking.presentation.SearchWalkingPresenter;
import com.example.demo.walking.infra.data.WalkingResponse;
import com.example.demo.walking.application.usecases.SearchWalking;
import com.example.demo.walking.domain.entity.Walking;
import com.example.demo.walking.domain.usecase.SearchWalkingUseCase;
import com.example.demo.walking.infra.repository.WalkingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableJpaRepositories
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean(name = "SearchWalkingPresenter")
    public Presenter<Page<Walking>,WalkingResponse> walkingPresenterBean() {
        return new SearchWalkingPresenter();
    }

    @Bean(name = "SearchWalking")
    public SearchWalkingUseCase<WalkingResponse> searchWalkingUseCase(
            @Qualifier("SearchWalkingPresenter")
                    Presenter<Page<Walking>, WalkingResponse> presenter,
            WalkingRepository walkingRepository) {
        return new SearchWalking(presenter, walkingRepository);

    }
}
