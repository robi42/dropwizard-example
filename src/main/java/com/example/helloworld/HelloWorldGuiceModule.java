package com.example.helloworld;

import com.codahale.dropwizard.ConfiguredBundle;
import com.codahale.dropwizard.hibernate.HibernateBundle;
import com.codahale.dropwizard.lifecycle.setup.ExecutorServiceBuilder;
import com.codahale.dropwizard.setup.Environment;
import com.example.helloworld.db.PersonRepository;
import com.example.helloworld.db.PersonRepositoryImpl;
import com.example.helloworld.domain.Template;
import com.example.helloworld.resources.*;
import com.example.helloworld.services.Greetable;
import com.example.helloworld.services.HelloWorldService;
import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.hibernate.SessionFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

public class HelloWorldGuiceModule extends AbstractModule {

    public static final String DEFAULT_TEMPLATE = "defaultTemplate";


    private final HelloWorldConfiguration configuration;

    private final ExecutorService executorService;

    private final List<ConfiguredBundle<HelloWorldConfiguration>> bundles;


    @SafeVarargs
    public HelloWorldGuiceModule(final HelloWorldConfiguration configuration,
                                 final Environment environment,
                                 final ConfiguredBundle<HelloWorldConfiguration>... bundles) {

        this.configuration = configuration;

        executorService = new ExecutorServiceBuilder(environment.lifecycle(), "main-task-executor-service")
                .minThreads(10)
                .maxThreads(100)
                .workQueue(new LinkedBlockingQueue<Runnable>(100))
                .build();

        this.bundles = Arrays.asList(bundles);
    }


    @Override
    protected void configure() {
        configureHibernate();

        bind(Template.class).annotatedWith(Names.named(DEFAULT_TEMPLATE))
                .toInstance(configuration.createTemplate());

        bind(ExecutorService.class).toInstance(executorService);

        bind(Greetable.class).to(HelloWorldService.class);

        bind(PersonRepository.class).to(PersonRepositoryImpl.class).asEagerSingleton();

        bind(HelloWorldResource.class).to(HelloWorldResourceImpl.class);
        bind(PeopleResource.class).to(PeopleResourceImpl.class);
        bind(PersonResource.class).to(PersonResourceImpl.class);
        bind(ProtectedResource.class).to(ProtectedResourceImpl.class);
        bind(ViewResource.class).to(ViewResourceImpl.class);
    }


    private void configureHibernate() {
        Optional<HibernateBundle> hibernateBundle = Optional.absent();

        for (final ConfiguredBundle<HelloWorldConfiguration> bundle : bundles) {
            if (bundle instanceof HibernateBundle) {
                hibernateBundle = Optional.of((HibernateBundle) bundle);
            }
        }

        if (hibernateBundle.isPresent()) {
            bind(SessionFactory.class).toInstance(hibernateBundle.get().getSessionFactory());
        } else {
            throw new RuntimeException("No Hibernate bundle available for dependency injection");
        }
    }
}
