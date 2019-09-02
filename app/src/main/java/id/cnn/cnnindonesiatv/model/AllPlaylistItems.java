package id.cnn.cnnindonesiatv.model;

import java.util.List;

public class AllPlaylistItems {
    private List<PlaylistItems> result;

    public AllPlaylistItems(List<PlaylistItems> result){
        this.result = result;
    }
    public List<PlaylistItems> getResult() {
        return result;
    }
}
