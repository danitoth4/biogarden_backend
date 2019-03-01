package web;

import model.Crop;
import model.repositories.CropRepository;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;

@SpringBootApplication
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
                log.info(c.getName());
            }
        };

    }
}