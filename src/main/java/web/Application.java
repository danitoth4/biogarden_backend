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
            repository.save(c);
            for(Crop crop : repository.findAll())
            {
                log.info(crop.getName());
            }
        };

    }
}