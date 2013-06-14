package com.example.helloworld.services;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

@Slf4j
@Singleton
public class HelloWorldService implements Greetable {

    private final ExecutorService executorService;


    @Inject
    public HelloWorldService(final ExecutorService executorService) {
        this.executorService = executorService;
    }


    @Override
    public String greet() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                log.info("Hello from task");
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                log.info("Hello from another task");
            }
        });

        return "Hi";
    }
}
