package com.example.backendgroupgenerateur.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Applique CORS à toutes les routes
                .allowedOrigins("http://192.168.1.108:8081") // 👈 autorise le port du front maison
                .allowedOrigins("http://192.168.1.26:8081") // 👈 autorise le port du front simplon
                .allowedOrigins("http://localhost:8081")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // Autorise tous les headers
                .allowCredentials(true); // Nécessaire si tu utilises les cookies
    }
}
