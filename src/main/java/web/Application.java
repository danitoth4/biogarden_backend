package web;

import model.Companion;
import model.Crop;
import model.CropType;
import model.repositories.CropRepository;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"model.repositories"})
@ComponentScan({"model", "web"})
@EntityScan("model")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**").allowedOrigins("http://localhost:5001", "http://localhost:3000").allowedMethods("PUT", "POST", "GET", "DELETE", "OPTIONS");
            }

            @Override
            public void addResourceHandlers(final ResourceHandlerRegistry registry)
            {
                registry.addResourceHandler("/images/*").addResourceLocations("file:///C:/git/biogarden_backend/src/main/resources/static/images/");
            }
        };
    }
}