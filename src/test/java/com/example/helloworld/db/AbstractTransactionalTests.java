package com.example.helloworld.db;

import com.codahale.dropwizard.db.DataSourceFactory;
import com.codahale.dropwizard.hibernate.HibernateBundle;
import com.codahale.dropwizard.hibernate.SessionFactoryFactory;
import com.codahale.dropwizard.lifecycle.setup.LifecycleEnvironment;
import com.codahale.dropwizard.logging.LoggingFactory;
import com.codahale.dropwizard.setup.Environment;
import com.example.helloworld.HelloWorldConfiguration;
import com.example.helloworld.domain.Person;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import static ch.qos.logback.classic.Level.ERROR;
import static org.hibernate.cfg.AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractTransactionalTests {
    static {
        LoggingFactory.bootstrap(ERROR);
    }

    private static final String TEST_DATABASE_NAME = "dropwizard_helloworld_test";
    private static final String JDBC_BASE_URL = "jdbc:postgresql://localhost/";

    protected final SessionFactory sessionFactory;

    private final Liquibase liquibase;

    private static HibernateBundle<HelloWorldConfiguration> hibernateBundle;
    private static Environment environment;
    private static Properties jdbcProperties;
    private static DataSourceFactory databaseConfig;


    @BeforeClass
    public static void initialize() throws Exception {
        hibernateBundle = createHibernateBundle();

        environment = mock(Environment.class);
        when(environment.lifecycle()).thenReturn(new LifecycleEnvironment());

        jdbcProperties = new Properties();
        jdbcProperties.load(AbstractTransactionalTests.class.getClassLoader().getResourceAsStream("jdbc.properties"));

        databaseConfig = createDatabaseConfig(jdbcProperties);

        final SessionFactory setupSessionFactory = new SessionFactoryFactory()
                .build(hibernateBundle, environment, databaseConfig, Collections.<Class<?>>emptyList());

        setUpTestDatabase(setupSessionFactory);
    }


    private static void setUpTestDatabase(final SessionFactory sessionFactory) {
        final Session session = sessionFactory.openSession();

        session.createSQLQuery("DROP DATABASE IF EXISTS " + TEST_DATABASE_NAME).executeUpdate();
        session.createSQLQuery("CREATE DATABASE " + TEST_DATABASE_NAME).executeUpdate();
        session.flush();
        sessionFactory.close();
    }


    public AbstractTransactionalTests() throws Exception {
        databaseConfig.setUrl(jdbcProperties.getProperty("url", JDBC_BASE_URL + TEST_DATABASE_NAME));

        sessionFactory = new SessionFactoryFactory().build(hibernateBundle, environment, databaseConfig,
                Arrays.<Class<?>>asList(Person.class));

        liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(),
                new JdbcConnection(((SessionImpl) sessionFactory.openSession()).connection()));
    }


    private static HibernateBundle<HelloWorldConfiguration> createHibernateBundle() {
        return new HibernateBundle<HelloWorldConfiguration>(Person.class) {
            @Override
            protected void configure(final Configuration configuration) {
                configuration.setProperty(CURRENT_SESSION_CONTEXT_CLASS, "thread");
            }

            @Override
            public DataSourceFactory getDataSourceFactory(final HelloWorldConfiguration configuration) {
                return configuration.getDatabase();
            }
        };
    }


    private static DataSourceFactory createDatabaseConfig(final Properties jdbcProperties) {
        final String username = jdbcProperties.getProperty("username");

        final DataSourceFactory databaseConfig = new DataSourceFactory();
        databaseConfig.setDriverClass("org.postgresql.Driver");
        databaseConfig.setUser(username);
        databaseConfig.setPassword(jdbcProperties.getProperty("password", ""));
        databaseConfig.setUrl(JDBC_BASE_URL + username);

        return databaseConfig;
    }


    @Before
    public void setUp() throws Exception {
        liquibase.update(null);

        sessionFactory.getCurrentSession().beginTransaction();
    }

    @After
    public void tearDown() throws Exception {
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().getTransaction().commit();

        liquibase.dropAll();
        liquibase.getDatabase().close();

        sessionFactory.close();
    }
}
