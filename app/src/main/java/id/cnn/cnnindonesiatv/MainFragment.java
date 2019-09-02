/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package id.cnn.cnnindonesiatv;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.youtube.player.YouTubeIntents;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.cnn.cnnindonesiatv.model.AllPlaylistItems;
import id.cnn.cnnindonesiatv.model.Movie;
import id.cnn.cnnindonesiatv.model.PlaylistItems;
import id.cnn.cnnindonesiatv.network.API;
import id.cnn.cnnindonesiatv.network.Interface;
import id.cnn.cnnindonesiatv.utility.CardPresenter;
import id.cnn.cnnindonesiatv.utility.TimeDifference;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    private List<PlaylistItems> data = new ArrayList<>();
    private ArrayObjectAdapter rowsAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);
        prepareBackgroundManager();
        setupUIElements();
        setupEventListeners();
        Interface mInterface = API.getAPI().create(Interface.class);
        Call<AllPlaylistItems> call = mInterface.getPlaylistItemsAll(BuildConfig.DEFAULT_CHANNEL_ID);
        call.enqueue(new Callback<AllPlaylistItems>() {
            @Override
            public void onResponse(Call<AllPlaylistItems> call, Response<AllPlaylistItems> response) {
                if(response.isSuccessful()){
                    data = response.body().getResult();
                    loadRows();
                } else {
                    Toast.makeText(getActivity(), "Response failed: " + response.errorBody().toString(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(), BrowseErrorActivity.class));
                }
            }

            @Override
            public void onFailure(Call<AllPlaylistItems> call, Throwable t) { startActivity(new Intent(getActivity(), BrowseErrorActivity.class)); }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();
        int i;
        for (i = 0; i < data.size(); i++) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (int j = 0; j < data.get(i).getItems().size(); j++) {
                Movie mov = data.get(i).getItems().get(j);
                listRowAdapter.add(new Movie(mov.getId(), mov.getTitle(), mov.getDescription(), mov.getBackgroundImageUrl(), mov.getCardImageUrl(), mov.getVideoUrl(), new TimeDifference().executeDateTimeDifference(mov.getStudio())));
            }
            //add itemview more on each playlist
            listRowAdapter.add(new Movie("","",data.get(i).getKategori(),"","",data.get(i).getPlaylistId(),"View More"));
            rowsAdapter.add(new ListRow(new HeaderItem(i, data.get(i).getKategori()), listRowAdapter));
        }
        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        //setTitle("CNN Indonesia VOD"); // Badge, when set, takes precedent
        setBadgeDrawable(getResources().getDrawable(R.drawable.logo_cnn));
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.cnn_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(data.size() > 0){
                    Intent searchIntent = new Intent(getActivity(),SearchActivity.class);
                    searchIntent.putExtra("movie", (Serializable) data);
                    startActivity(searchIntent);
                }
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                if(!movie.getId().equalsIgnoreCase("")){
                    Intent ytVid = YouTubeIntents.createPlayVideoIntentWithOptions(getActivity(), movie.getVideoUrl(), true, true);
                    ytVid.putExtra("force_fullscreen",true);
                    ytVid.putExtra("finish_on_ended",true);
                    getActivity().startActivity(ytVid);
                } else {
                    Intent moreVid = new Intent(getActivity(), MoreVideosActivity.class);
                    moreVid.putExtra("cat_title",movie.getDescription());
                    moreVid.putExtra("cat_videoId",movie.getVideoUrl());
                    startActivity(moreVid);
                }
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
            if (item instanceof Movie) {
                //mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
                //startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }

    private void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }
}
