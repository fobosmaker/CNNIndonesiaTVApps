package id.cnn.cnnindonesiatv;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Interface {
    @GET("v1/checkConnection")
    Call<CheckModel> getMessage();

    @GET("v1/playlistItemsAll")
    Call<AllPlaylistItems> getPlaylistItemsAll();
}
