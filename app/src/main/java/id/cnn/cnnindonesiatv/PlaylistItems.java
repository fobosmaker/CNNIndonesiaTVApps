package id.cnn.cnnindonesiatv;

import java.util.List;

public class PlaylistItems {
    private String kategori;
    private List<Movie> items;

    public PlaylistItems(String kategori, List<Movie> items){
        this.kategori = kategori;
        this.items = items;
    }

    public String getKategori() { return kategori; }

    public List<Movie> getItems() { return items; }
}
