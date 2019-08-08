package web.errorhandling;

 public class CropNotFoundException extends RuntimeException{

     public CropNotFoundException(int id) {
         super("Could not find crop " + id);
     }
}
