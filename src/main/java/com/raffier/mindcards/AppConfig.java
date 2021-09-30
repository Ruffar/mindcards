package com.raffier.mindcards;

import com.raffier.mindcards.repository.AppDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@ConstructorBinding
@Configuration
@ConfigurationProperties
public class AppConfig {

    private static AppDatabase database;// = new AppDatabase("testDatabase");

    public static AppDatabase getDatabase() { return database; }
    @Value("${databasename}")
    public void setDatabase (String databasename) { AppConfig.database = new AppDatabase(databasename); }

}
