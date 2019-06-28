package model;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.bind.*;

import model.repositories.CropRepository;
import org.json.*;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class ModelManager
{

    //singleton
    private static ModelManager instance = null;

    private  Garden currentGarden = new Garden(20 * 5, 10 * 5);

    private ArrayList<Crop> crops;

    private ModelManager()
    {
    }

    public Garden getCurrentGarden() {
        return currentGarden;
    }

    public void setCurrentGarden(Garden currentGarden) {
        this.currentGarden = currentGarden;
    }

    public static ModelManager getInstance()
    {
        if(instance == null)
            instance = new ModelManager();
        return instance;
    }


    public ArrayList<Crop> getCrops() {
        return crops;
    }


}