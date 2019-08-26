package id.cnn.cnnindonesiatv;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Interface {
    @GET("v1/checkConnection")
    Call<CheckModel> getMessage();
}
