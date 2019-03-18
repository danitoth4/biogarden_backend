package web.errorhandling;

 public class CropNotFoundException extends RuntimeException{

     public CropNotFoundException(Short id) {
         super("Could not find crop " + id);
     }
}
