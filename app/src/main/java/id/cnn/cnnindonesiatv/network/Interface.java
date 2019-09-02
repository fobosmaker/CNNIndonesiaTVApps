package id.cnn.cnnindonesiatv.network;

import id.cnn.cnnindonesiatv.model.AllPlaylistItems;
import id.cnn.cnnindonesiatv.model.CategoryItems;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Interface {
    @GET("v1/playlistItemsAll/{channelId}")
    Call<AllPlaylistItems> getPlaylistItemsAll(@Path("channelId") String channelId);

    @GET("v1/playlistItems/{playlistId}/{channelId}")
    Call<CategoryItems> getPlaylistItemsCategory(@Path("playlistId") String playlistId, @Path("channelId") String channelId);
}
