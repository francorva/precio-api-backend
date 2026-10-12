package com.example.price_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Asegúrate de que termine con / al final
        registry.addResourceHandler("/foto_producto/**")
                .addResourceLocations("file:C:/Users/User/OneDrive/Documents cloud/Proyecto Developer/proyecto-de-precios-angular/public/foto_producto/");
    }
}