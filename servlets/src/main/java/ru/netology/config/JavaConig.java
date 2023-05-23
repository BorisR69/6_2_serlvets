package ru.netology.config;

import org.springframework.context.annotation.Bean;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.repository.PostRepositoryImpl;
import ru.netology.service.PostService;

public class JavaConig {
    @Bean
    public PostController PostController (PostService service){
        return new PostController(service);
    }

    @Bean
    public PostService PostService (PostRepository repository) {
        return new PostService(repository);
    }

    @Bean
    public PostRepositoryImpl PostRepositoryImpl (){
        return new PostRepositoryImpl();
    }

}
