package com.raffier.mindcards;

import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class AppConfig {

    @Bean
    @Value("${databasename}")
    public AppDatabase appDatabase(String dbName) {
        return new AppDatabase(dbName);
    }

}
