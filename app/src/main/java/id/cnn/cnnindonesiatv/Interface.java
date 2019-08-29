package id.cnn.cnnindonesiatv;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Interface {
    @GET("v1/playlistItemsAll/{channelId}")
    Call<AllPlaylistItems> getPlaylistItemsAll(@Path("channelId") String channelId);

    @GET("v1/playlistItems/{playlistId}/{channelId}")
    Call<AllPlaylistItems> getPlaylistItemsCategory(@Path("playlistId") String playlistId, @Path("channelId") String channelId);
}
