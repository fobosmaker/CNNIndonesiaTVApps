package id.cnn.cnnindonesiatv;

import java.util.List;

public class CheckModel {
    private String message;
    private List<Movie> items;

    public CheckModel(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
