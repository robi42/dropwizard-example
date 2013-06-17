package com.example.helloworld.db;

import com.codahale.dropwizard.db.DataSourceFactory;
import com.codahale.dropwizard.hibernate.HibernateBundle;
import com.codahale.dropwizard.hibernate.SessionFactoryFactory;
import com.codahale.dropwizard.lifecycle.setup.LifecycleEnvironment;
import com.codahale.dropwizard.logging.LoggingFactory;
import com.codahale.dropwizard.setup.Environment;
import com.example.helloworld.HelloWorldConfiguration;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
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

import javax.persistence.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static ch.qos.logback.classic.Level.ERROR;
import static com.google.common.reflect.ClassPath.ClassInfo;
import static java.lang.ClassLoader.getSystemClassLoader;
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

    private final Session liquibaseSession;
    private final Liquibase liquibase;

    private static List<Class<?>> entities = new ArrayList<>();
    private static HibernateBundle<HelloWorldConfiguration> hibernateBundle;
    private static Environment environment;
    private static Properties jdbcProperties = new Properties();
    private static DataSourceFactory databaseConfig;


    @BeforeClass
    public static void initialize() throws Exception {
        scanForEntities();

        hibernateBundle = createHibernateBundle();

        environment = mock(Environment.class);
        when(environment.lifecycle()).thenReturn(new LifecycleEnvironment());

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
        session.close();
        sessionFactory.close();
    }


    public AbstractTransactionalTests() throws Exception {
        databaseConfig.setUrl(jdbcProperties.getProperty("url", JDBC_BASE_URL + TEST_DATABASE_NAME));

        sessionFactory = new SessionFactoryFactory().build(hibernateBundle, environment, databaseConfig, entities);

        liquibaseSession = sessionFactory.openSession();
        liquibase = new Liquibase("migrations.xml", new ClassLoaderResourceAccessor(),
                new JdbcConnection(((SessionImpl) liquibaseSession).connection()));
    }


    private static void scanForEntities() throws IOException, ClassNotFoundException {
        final String domainPackageName = AbstractTransactionalTests.class.getPackage().getName()
                .replace("db", "domain");
        final ImmutableSet<ClassInfo> domainPackageClassInfos = ClassPath.from(getSystemClassLoader())
                .getTopLevelClasses(domainPackageName);

        for (final ClassInfo classInfo : domainPackageClassInfos) {
            final Class<?> entityCandidate = Class.forName(classInfo.getName());

            if (entityCandidate.isAnnotationPresent(Entity.class)) {
                entities.add(entityCandidate);
            }
        }

        if (entities.size() == 0) {
            throw new RuntimeException(String.format("No entity found in `%s`", domainPackageName));
        }
    }


    private static HibernateBundle<HelloWorldConfiguration> createHibernateBundle() {
        return new HibernateBundle<HelloWorldConfiguration>(entities.get(0), createAdditionalEntitiesContainer()) {
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

    private static Class<?>[] createAdditionalEntitiesContainer() {
        final Class<?>[] additionalEntities;

        if (entities.size() > 1) {
            additionalEntities = new Class[entities.size() - 1];
            int index = 0;

            for (final Class<?> entity : entities.subList(1, entities.size() - 1)) {
                additionalEntities[index] = entity;
                index++;
            }
        } else {
            additionalEntities = new Class[]{entities.get(0)};
        }

        return additionalEntities;
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
        final Session currentSession = sessionFactory.getCurrentSession();
        currentSession.flush();
        currentSession.getTransaction().commit();
        sessionFactory.getCurrentSession().close();

        liquibase.dropAll();
        liquibaseSession.flush();
        liquibase.getDatabase().close();

        sessionFactory.close();
    }
}
