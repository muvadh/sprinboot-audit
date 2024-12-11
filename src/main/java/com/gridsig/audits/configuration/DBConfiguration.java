package com.gridsig.audits.configuration;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "auditLogEntityManager",
        transactionManagerRef = "auditLogTransactionManager",
        basePackages = {"com.gridsig.audits.repository"}
)
@EntityScan("com.gridsig.audits.entity")
public class DBConfiguration {

    private final Environment env;

    // Constructor-based dependency injection
    public DBConfiguration(Environment env) {
        this.env = env;
    }

    /**
     * DataSource Bean Configuration with prefix 'spring.datasource'
     * Reads properties like url, username, password from application.properties
     */
    @Primary
    @Bean(name = "auditLogDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource auditLogDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }

    /**
     * Transaction Manager Bean
     * Responsible for managing transactions with the specified EntityManagerFactory
     */
    @Primary
    @Bean(name = "auditLogTransactionManager")
    public PlatformTransactionManager auditLogTransactionManager(
            @Qualifier("auditLogEntityManager") EntityManagerFactory auditLogEntityManagerFactory) {
        return new JpaTransactionManager(auditLogEntityManagerFactory);
    }

    /**
     * EntityManagerFactory Bean
     * Configures the EntityManagerFactory using Hibernate as JPA vendor and the provided DataSource
     */
    @Primary
    @Bean(name = "auditLogEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        // Executor for managing the threads during EntityManagerFactory setup
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setDaemon(true);  // Allow threads to exit when the JVM shuts down
        threadPoolTaskExecutor.afterPropertiesSet();

        // Create and configure EntityManagerFactory
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setBootstrapExecutor(threadPoolTaskExecutor);
        entityManagerFactoryBean.setDataSource(auditLogDataSource());  // Using correct method name
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.gridsig.audits.entity"); // Scans entity packages
        entityManagerFactoryBean.setPersistenceUnitName("auditLogEntityManager");

        return entityManagerFactoryBean;
    }
}
