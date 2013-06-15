package com.example.helloworld.resources;

import com.example.helloworld.domain.User;
import com.example.helloworld.services.Greetable;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Slf4j
@Singleton
public class ProtectedResourceImpl implements ProtectedResource {

    private final Greetable greetingService;
    private final ExecutorService executorService;


    @Inject
    public ProtectedResourceImpl(final Greetable greetingService, final ExecutorService executorService) {
        this.greetingService = greetingService;
        this.executorService = executorService;
    }


    @Override
    public String showSecret(final User user) {
        return String.format("Hey there, %s. You know the secret!", user.getName());
    }

    @Override
    public String not() {
        log.info(greetingService.greet());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                log.info("Juhuu");
            }
        });

        return "NOT";
    }
}
