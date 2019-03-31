package web.controllers;


import model.ModelManager;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
public class GardenController
{
    @GetMapping("/garden")
    public Garden getGarden()
    {
        return ModelManager.getInstance().getCurrentGarden();
    }

    @PostMapping("/garden")
    @ResponseStatus(HttpStatus.CREATED)
    public Garden postGarden(@RequestBody Garden garden)
    {
        ModelManager.getInstance().setCurrentGarden(garden);
        return garden;
    }

    /*
    @PutMapping("/garden")
    public Garden putGarden(@RequestBody Garden garden)
    {
        return new Garden();
    }*/
}