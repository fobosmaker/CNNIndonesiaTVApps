package id.cnn.cnnindonesiatv;

import android.Manifest;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends android.support.v17.leanback.app.SearchFragment implements android.support.v17.leanback.app.SearchFragment.SearchResultProvider{
    private static final String TAG = "SearchFragment";
    private static final int REQUEST_SPEECH = 0x00000010;
    private ArrayObjectAdapter mRowsAdapter;
    private List<Movie> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = MovieList.getList();
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);
        if(!Utils.hasPermission(getActivity(),Manifest.permission.RECORD_AUDIO)){
            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {
                    Log.d(TAG, "recognizeSpeech: ");
                }
            });
        }

    }

    @Override
    public ObjectAdapter getResultsAdapter() {

        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.d(TAG, "onQueryTextChange: "+s);
        return searchProses(s);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.d(TAG, "onQueryTextSubmit: "+s);
        return searchProses(s);
    }

    boolean searchProses(String query){
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        for (int j = 0; j < list.size(); j++) {
            if(query.equalsIgnoreCase(list.get(j).getTitle().toLowerCase()))
                Log.d(TAG, "ketemu di j="+j);
            listRowAdapter.add(list.get(j));
        }
        HeaderItem header = new HeaderItem("Search Result");
        mRowsAdapter.add(new ListRow(header, listRowAdapter));
        return true;
    }
}
