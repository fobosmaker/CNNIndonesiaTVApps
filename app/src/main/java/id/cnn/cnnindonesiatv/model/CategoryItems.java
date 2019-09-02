package id.cnn.cnnindonesiatv.model;

import java.io.Serializable;
import java.util.List;

public class CategoryItems implements Serializable {
    private List<Movie> items;
    public CategoryItems(List<Movie> items){ this.items = items; }
    public List<Movie> getItems() { return items; }
}
