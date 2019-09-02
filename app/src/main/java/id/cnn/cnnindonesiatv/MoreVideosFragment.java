package id.cnn.cnnindonesiatv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeIntents;

import java.util.ArrayList;
import java.util.List;

import id.cnn.cnnindonesiatv.model.CategoryItems;
import id.cnn.cnnindonesiatv.model.Movie;
import id.cnn.cnnindonesiatv.network.API;
import id.cnn.cnnindonesiatv.network.Interface;
import id.cnn.cnnindonesiatv.utility.CardPresenter;
import id.cnn.cnnindonesiatv.utility.TimeDifference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoreVideosFragment extends android.support.v17.leanback.app.VerticalGridFragment {
    private ArrayObjectAdapter mAdapter;
    private List<Movie> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null){
            VerticalGridPresenter verticalGridPresenter = new VerticalGridPresenter();
            verticalGridPresenter.setNumberOfColumns(5);
            setGridPresenter(verticalGridPresenter);
            mAdapter = new ArrayObjectAdapter(new CardPresenter());
            setTitle(getActivity().getIntent().getStringExtra("cat_title"));
            Interface mInterface = API.getAPI().create(Interface.class);
            Call<CategoryItems> call = mInterface.getPlaylistItemsCategory(getActivity().getIntent().getStringExtra("cat_videoId"),BuildConfig.DEFAULT_CHANNEL_ID);
            call.enqueue(new Callback<CategoryItems>() {
                @Override
                public void onResponse(Call<CategoryItems> call, Response<CategoryItems> response) {
                    if(response.isSuccessful()){
                        data = response.body().getItems();
                        loadRows();
                        setupListener();
                    } else {
                        Toast.makeText(getActivity(), "Response failed: " + response.errorBody().toString(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), BrowseErrorActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<CategoryItems> call, Throwable t) {
                    Toast.makeText(getActivity(), "Response failed: "+t.getMessage(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), BrowseErrorActivity.class));
                }
            });
        } else {
            Toast.makeText(getActivity(), "Data is empty...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }

    private void loadRows() {
        for (int i = 0; i < data.size(); i++){
            Movie mov = data.get(i);
            mAdapter.add(new Movie(mov.getId(), mov.getTitle(), mov.getDescription(), mov.getBackgroundImageUrl(), mov.getCardImageUrl(), mov.getVideoUrl(), new TimeDifference().executeDateTimeDifference(mov.getStudio())));
        }
        setAdapter(mAdapter);
    }

    private void setupListener(){
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Intent ytVid = YouTubeIntents.createPlayVideoIntentWithOptions(getActivity(), movie.getVideoUrl(), true, true);
                ytVid.putExtra("force_fullscreen",true);
                ytVid.putExtra("finish_on_ended",true);
                getActivity().startActivity(ytVid);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
        }
    }

}
