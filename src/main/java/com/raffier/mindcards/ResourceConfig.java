package com.raffier.mindcards;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //@Configuration inherits @Component, making this class a singleton
@EnableWebMvc
public class ResourceConfig implements WebMvcConfigurer { //WebMvcConfigurer is an interface

    @Value("${dynamicContentDirectory}")
    String dirPath; //@Value tells Spring to replace the variable's value with 'dynamicContentDirectory' at singleton instancing

    private final String[] RESOURCE_LOCATIONS = { //Default locations of static resources
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(RESOURCE_LOCATIONS); //Any requests for resources will first be searched within static locations
        registry.addResourceHandler("/cardImages/**")
                .addResourceLocations("file:"+dirPath+"/images/"); //Any requests starting with "cardImages/" will be searched in the dynamic content directory
    }
}
