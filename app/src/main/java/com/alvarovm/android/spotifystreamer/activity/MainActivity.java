/**
 *  Copyright (C) 2015 AlvaroVM.com
 */

package com.alvarovm.android.spotifystreamer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.alvarovm.android.spotifystreamer.R;
import com.alvarovm.android.spotifystreamer.fragment.DetailActivityFragment;
import com.alvarovm.android.spotifystreamer.fragment.PlayerActivityFragment;
import com.alvarovm.android.spotifystreamer.fragment.SpotifyFragment;
import com.alvarovm.android.spotifystreamer.model.MyArtist;

import java.util.ArrayList;




public class MainActivity extends ActionBarActivity implements SpotifyFragment.Callback,
        DetailActivityFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private final String PLAYERFRAGMENT_TAG = "PFTAG";
    private boolean mIsLargeLayout = false;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.top_tracks_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_tracks_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(String artistId) {

        if(mTwoPane){
            Bundle args = new Bundle();
            args.putString(MyArtist.ID,artistId);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        } else{
                Intent detailIntent = new Intent(this, DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, artistId);
                startActivity(detailIntent);
        }

    }

    @Override
    public void onItemSelected(ArrayList topTrackList, int pos) {
        Bundle args = new Bundle();
        args.putInt(DetailActivityFragment.TRACK_POSITION, pos);
        args.putParcelableArrayList(DetailActivityFragment.TOP_TRACK_LIST, topTrackList);

        if (mTwoPane) {
            PlayerActivityFragment playerFragment = new PlayerActivityFragment();
            playerFragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
            playerFragment.setArguments(args);
            playerFragment.show(getSupportFragmentManager(), PLAYERFRAGMENT_TAG);

        }

    }

    public boolean isTwoPane(){
        return mTwoPane;
    }

}
