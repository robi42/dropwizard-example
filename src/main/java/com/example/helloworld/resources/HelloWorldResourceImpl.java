package com.example.helloworld.resources;

import com.example.helloworld.domain.Saying;
import com.example.helloworld.domain.Template;
import com.google.common.base.Optional;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import static com.example.helloworld.HelloWorldGuiceModule.DEFAULT_TEMPLATE;

@Slf4j
@Singleton
public class HelloWorldResourceImpl implements HelloWorldResource {

    private final Template template;
    private final AtomicLong counter;
    private final ExecutorService executorService;


    @Inject
    public HelloWorldResourceImpl(final @Named(DEFAULT_TEMPLATE) Template template,
                                  final ExecutorService executorService) {
        this.template = template;
        this.executorService = executorService;
        counter = new AtomicLong();
    }


    @Override
    public Saying sayHello(final Optional<String> name) {
        return new Saying(counter.incrementAndGet(), template.render(name));
    }

    @Override
    public void receiveHello(final Saying saying) {
        final Future<String> task = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return saying.getContent();
            }
        });

        try {
            log.info(task.get());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Something went wrong while executing saying content retrieving task", e);
        }

        log.info("Received a saying: {}", saying);
    }
}
