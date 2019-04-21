package web;

import model.Crop;
import model.repositories.CropRepository;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"model.repositories"})
@ComponentScan({"model", "web.controllers"})
@EntityScan("model")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(CropRepository repository)
    {
        return (args) ->
        {
            Crop c = new Crop();
            c.setName("Tomato");
            for(Crop crop : repository.findAll())
            {
                if(crop.getId() == 2)
                    c.getAvoids().add(crop);
                else
                    c.getHelps().add(crop);
            }

            repository.save(c);
        };

    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/crop").allowedOrigins("http://localhost:3000").allowedMethods("PUT", "POST", "GET", "DELETE");
                registry.addMapping("/companions").allowedOrigins("http://localhost:3000").allowedMethods("PUT", "POST", "GET", "DELETE");
            }
        };
    }
}