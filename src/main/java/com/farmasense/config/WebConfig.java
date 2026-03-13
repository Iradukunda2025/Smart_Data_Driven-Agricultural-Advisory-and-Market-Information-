package com.farmasense.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map the root URL to the index view
        registry.addViewController("/").setViewName("forward:/index");
        
        // Map the context root to the index view
        registry.addViewController("/FarmaSense_war_exploded").setViewName("forward:/index");
        registry.addViewController("/FarmaSense_war_exploded/").setViewName("forward:/index");
    }
}
