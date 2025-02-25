package com.gamecenter.gamecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GameCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameCenterApplication.class, args);
    }

    @Configuration
    public static class CorsConfig {

        @Bean
        public WebMvcConfigurer corsConfigurer() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    // Разрешаем запросы с фронтенда (http://localhost:5173)
                    registry.addMapping("/api/**") // Разрешаем CORS для всех эндпоинтов, начинающихся с /api
                            .allowedOrigins("http://localhost:5173") // Укажите адрес вашего фронтенда
                            .allowedMethods("GET", "POST", "PUT", "DELETE") // Разрешаем методы
                            .allowedHeaders("*") // Разрешаем все заголовки
                            .allowCredentials(true); // Разрешаем передачу куки и авторизационных данных
                }
            };
        }
    }
}
