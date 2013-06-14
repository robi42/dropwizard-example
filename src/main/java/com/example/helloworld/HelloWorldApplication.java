package com.example.helloworld;

import com.codahale.dropwizard.Application;
import com.codahale.dropwizard.assets.AssetsBundle;
import com.codahale.dropwizard.auth.basic.BasicAuthProvider;
import com.codahale.dropwizard.db.DataSourceFactory;
import com.codahale.dropwizard.hibernate.HibernateBundle;
import com.codahale.dropwizard.migrations.MigrationsBundle;
import com.codahale.dropwizard.setup.Bootstrap;
import com.codahale.dropwizard.setup.Environment;
import com.codahale.dropwizard.views.ViewBundle;
import com.example.helloworld.auth.ExampleAuthenticator;
import com.example.helloworld.cli.RenderCommand;
import com.example.helloworld.domain.Person;
import com.example.helloworld.health.TemplateHealthCheck;
import com.example.helloworld.resources.V1ResourceContainer;
import com.example.helloworld.util.SecurityUtilities;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }


    private final HibernateBundle<HelloWorldConfiguration> hibernateBundle =
            new HibernateBundle<HelloWorldConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(final HelloWorldConfiguration configuration) {
                    return configuration.getDatabase();
                }
            };


    @Override
    public String getName() {
        return "hello-world";
    }


    @Override
    public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.addCommand(new RenderCommand());

        bootstrap.addBundle(new AssetsBundle("/assets", "/app", "index.html"));
        bootstrap.addBundle(new MigrationsBundle<HelloWorldConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(final HelloWorldConfiguration configuration) {
                return configuration.getDatabase();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(final HelloWorldConfiguration configuration, final Environment environment)
            throws ClassNotFoundException {

        final Injector injector = Guice.createInjector(
                new HelloWorldGuiceModule(configuration, environment, hibernateBundle));

        environment.admin().setSecurityHandler(SecurityUtilities.createAdminBasicAuthHandler(
                configuration.getAdminBasicAuthUsername(), configuration.getAdminBasicAuthPassword()));
        environment.admin().addHealthCheck("template", injector.getInstance(TemplateHealthCheck.class));

        environment.jersey().addProvider(new BasicAuthProvider<>(new ExampleAuthenticator(
                configuration.getAppBasicAuthUsername(), configuration.getAppBasicAuthPassword()), "App Secured"));
        environment.jersey().addResource(injector.getInstance(V1ResourceContainer.class));
    }
}
