//package ru.kikopark.backend.configs;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//public class DatabaseConfig {
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.web-datasource")
//    public DataSource webDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean webEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("webDataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("../modules/authentication/entities", "../modules/order/entities",  "../modules/base/entities")
//                .persistenceUnit("web")
//                .build();
//    }
//
//    @Bean
//    public PlatformTransactionManager webTransactionManager(@Qualifier("webEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "spring.storage-datasource")
//    public DataSource storageDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean storageEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("storageDataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("../modules/authentication/entities", "../modules/order/entities",  "../modules/base/entities")
//                .persistenceUnit("storage")
//                .build();
//    }
//
//    @Bean
//    public PlatformTransactionManager storageTransactionManager(@Qualifier("storageEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//}
