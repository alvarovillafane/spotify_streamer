/**
 *  Copyright (C) 2015 AlvaroVM.com
 */

package com.alvarovm.android.spotifystreamer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.alvarovm.android.spotifystreamer.R;
import com.alvarovm.android.spotifystreamer.fragment.DetailActivityFragment;
import com.alvarovm.android.spotifystreamer.model.MyArtist;

import java.util.ArrayList;


public class DetailActivity extends ActionBarActivity implements DetailActivityFragment.Callback{

    private boolean mIsLargeLayout = false;
    private final String FRAGMENT_PLAYER = "fragment_player";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        if (savedInstanceState == null) {
            Intent intentMain = this.getIntent();
            Bundle args = new Bundle();

            args.putString(MyArtist.ID,intentMain.getStringExtra(Intent.EXTRA_TEXT));
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);


            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_tracks_container, fragment)
                    .commit();
        }

    }



    @Override
    public void onItemSelected(ArrayList topTrackList, int pos) {
        Bundle args = new Bundle();
        args.putInt(DetailActivityFragment.TRACK_POSITION, pos);
        args.putParcelableArrayList(DetailActivityFragment.TOP_TRACK_LIST, topTrackList);

        Intent playerIntent = new Intent(this, PlayerActivity.class)
                    .putParcelableArrayListExtra(DetailActivityFragment.TOP_TRACK_LIST, topTrackList)
                    .putExtra(DetailActivityFragment.TRACK_POSITION,pos);
            startActivity(playerIntent);

    }

}
