package web.errorhandling;

public class ContentNotFoundException extends RuntimeException{

    public ContentNotFoundException(Integer id) {
        super("Could not find this garden.");
    }
}