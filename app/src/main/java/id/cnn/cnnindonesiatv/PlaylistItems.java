package id.cnn.cnnindonesiatv;

import java.util.List;

public class PlaylistItems {
    private String kategori;
    private String playlistId;
    private List<Movie> items;

    public PlaylistItems(String kategori, String playlistId ,List<Movie> items){
        this.kategori = kategori;
        this.playlistId = playlistId;
        this.items = items;
    }

    public String getKategori() { return kategori; }

    public List<Movie> getItems() { return items; }

    public String getPlaylistId() { return playlistId; }
}
