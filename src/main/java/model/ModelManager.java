package model;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.bind.*;
import org.json.*;

public class ModelManager
{

    //singleton
    private static ModelManager instance = null;

    private  Garden currentGarden = new Garden(200, 200);


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

}