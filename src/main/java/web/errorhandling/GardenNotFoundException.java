package web.errorhandling;

public class GardenNotFoundException extends RuntimeException{

    public GardenNotFoundException(Integer id) {
        super("Could not find this garden.");
    }
}