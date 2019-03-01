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

    //just to test this
    private String[] crops = new String[] {"tomato", "carrot"};

    private ArrayList<Crop> cropData = new ArrayList<>();

    private  Garden currentGarden;

    private ModelManager()
    {
        for(String name : crops)
        {

            try
            {
                URL url = new URL("https://openfarm.cc/api/v1/crops/"+ name + "/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int status = con.getResponseCode();

                if(status != 200)
                {
                    System.out.println("error");
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                cropData.add(setupCrop(content));
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
        createXML();
    }

    public Garden getCurrentGarden() {
        return currentGarden;
    }

    public void setCurrentGarden(Garden currentGarden) {
        this.currentGarden = currentGarden;
    }

    private void createXML() {
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Crop.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            for(Crop c : cropData)
            {
                marshaller.marshal(c, new File("C:\\git\\biogarden\\src\\main\\resources\\Models\\" + c.getName() +".xml"));
            }
        }
        catch (JAXBException e )
        {
            System.out.println("AU");
        }

    }

    private Crop setupCrop(StringBuffer content) {
        JSONTokener tokener = new JSONTokener(content.toString());
        JSONObject root = new JSONObject(tokener);

        JSONObject data = root.getJSONObject("data");
        JSONObject attributes = data.getJSONObject("attributes");

        Crop newCrop = new Crop();


        newCrop.setName(attributes.getString("name"));
        newCrop.setBinomialName(attributes.getString("binomial_name"));
        newCrop.setDescription(attributes.getString("description"));
        switch(attributes.getString("sun_requirements"))
        {
            case "Full Sun":
                newCrop.setSunRequirement(SunType.FULLSUN);
                break;
            case "Partial Sun":
                newCrop.setSunRequirement(SunType.PARTIALSUN);
                break;
            case "Full Shade":
                newCrop.setSunRequirement(SunType.FULLSHADE);
                break;
            default:
                newCrop.setSunRequirement(SunType.FULLSUN);
                break;
        }
        newCrop.setSowingMethod(attributes.getString("sowing_method"));
        newCrop.setDiameter(attributes.getDouble("spread"));
        newCrop.setRowSpacing(attributes.getDouble("row_spacing"));
        newCrop.setHeight(attributes.getDouble("height"));

        return newCrop;
    }

    public static ModelManager getInstance()
    {
        if(instance == null)
            instance = new ModelManager();
        return instance;
    }

    public final Crop getData(String name)
    {
        for(Crop c : cropData)
        {
            if(c.getName().toLowerCase().equals(name.toLowerCase()))
                return c;
        }
        return null;
    }

}