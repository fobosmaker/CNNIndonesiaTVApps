package id.cnn.cnnindonesiatv;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeIntents;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends android.support.v17.leanback.app.SearchFragment implements android.support.v17.leanback.app.SearchFragment.SearchResultProvider{
    private static final String TAG = "SearchFragment";
    private ArrayObjectAdapter mRowsAdapter;
    private List<PlaylistItems> data = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Utils.hasPermission(getActivity(),Manifest.permission.RECORD_AUDIO)){ setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() { Log.d(TAG, "recognizeSpeech: "); }
            }); }
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);
        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle != null){
            data = (List<PlaylistItems>) getActivity().getIntent().getSerializableExtra("movie");
            loadRows();
            setOnItemViewClickedListener(new ItemViewClickedListener());
        } else {
            Toast.makeText(getActivity(), "Data is empty...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
    }
    public void loadRows(){
        for (int i = 0; i < data.size(); i++) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            for (int j = 0; j < data.get(i).getItems().size(); j++) {
                Movie movie = data.get(i).getItems().get(j);
                listRowAdapter.add(new Movie(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getBackgroundImageUrl(), movie.getCardImageUrl(), movie.getVideoUrl(), new TimeDifference().executeDateTimeDifference(movie.getStudio())));
            }
            mRowsAdapter.add(new ListRow(new HeaderItem(i, data.get(i).getKategori()), listRowAdapter));
        }
    }

    @Override
    public ObjectAdapter getResultsAdapter() { return mRowsAdapter; }

    @Override
    public boolean onQueryTextChange(String s) {
        if(s.length() > 0) return searchProses(s);
        else {
            loadRows();
            return true;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) { return searchProses(s); }

    boolean searchProses(String query){
        mRowsAdapter.clear();
        if(query.length() > 0){
            for (int i = 0; i < data.size(); i++) {
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
                for (int j = 0; j < data.get(i).getItems().size(); j++) {
                    Movie movie = data.get(i).getItems().get(j);
                    if(movie.getTitle().toLowerCase().contains(query.toLowerCase())) listRowAdapter.add(new Movie(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getBackgroundImageUrl(), movie.getCardImageUrl(), movie.getVideoUrl(), new TimeDifference().executeDateTimeDifference(movie.getStudio())));
                }
                if(listRowAdapter.size() > 0) mRowsAdapter.add(new ListRow(new HeaderItem(i, data.get(i).getKategori()), listRowAdapter));
            }
            if (mRowsAdapter.size() == 0) mRowsAdapter.add(new ListRow(new HeaderItem(0, "No result found on "+query), new ArrayObjectAdapter()));
        } else loadRows();
        return true;
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
}
