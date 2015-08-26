package com.alvarovm.android.spotifystreamer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.alvarovm.android.spotifystreamer.R;
import com.alvarovm.android.spotifystreamer.fragment.DetailActivityFragment;
import com.alvarovm.android.spotifystreamer.fragment.PlayerActivityFragment;

;
;

public class PlayerActivity extends ActionBarActivity {

    boolean mIsLargeLayout;
    private final String PLAYERFRAGMENT_TAG = "PFTAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (savedInstanceState == null) {
            Intent intentMain = this.getIntent();
            Bundle args = new Bundle();

            PlayerActivityFragment playerFragment = new PlayerActivityFragment();

            args.putInt(DetailActivityFragment.TRACK_POSITION,
                    intentMain.getIntExtra(DetailActivityFragment.TRACK_POSITION, 0));

            args.putParcelableArrayList(DetailActivityFragment.TOP_TRACK_LIST,
                    intentMain.getParcelableArrayListExtra(DetailActivityFragment.TOP_TRACK_LIST));

            playerFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(android.R.id.content, playerFragment).commit();
                    //.addToBackStack(null).commit();
        }
    }


}
