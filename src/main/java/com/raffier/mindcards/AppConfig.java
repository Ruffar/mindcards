package com.raffier.mindcards;

import com.raffier.mindcards.model.AppDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@ConstructorBinding
@Configuration
@ConfigurationProperties
public class AppConfig {

    private static AppDatabase database;// = new AppDatabase("testDatabase");

    public static AppDatabase getDatabase() { return database; }
    @Value("${databasename}")
    public void setDatabase (String databasename) { AppConfig.database = new AppDatabase(databasename); }

}
