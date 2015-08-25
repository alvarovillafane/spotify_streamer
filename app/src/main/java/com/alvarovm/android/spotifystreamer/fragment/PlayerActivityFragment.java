package com.alvarovm.android.spotifystreamer.fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alvarovm.android.spotifystreamer.R;
import com.alvarovm.android.spotifystreamer.model.MyArtist;
import com.alvarovm.android.spotifystreamer.model.MyTopTrack;
import com.alvarovm.android.spotifystreamer.service.MediaPlayerService;
import com.alvarovm.android.spotifystreamer.util.Helper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayerActivityFragment extends DialogFragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {
    Integer myTopTrackPosition;
    ArrayList<MyTopTrack> myTopTrackArrayList;
    MediaPlayerService mService;
    boolean mBound = false;

    TextView mTrackNameText;
    TextView mArtistNameText;
    TextView mAlbumNameText;
    ImageView mImageArtist;
    SeekBar mSeekBar;
    TextView mActualPosition;
    TextView mTrackDuration;
    View rootview;

    Intent intentPlayer = null;
    Integer actualPlayerPos = 0;

    private String LOG_TAG= this.getClass().getSimpleName();

    Handler handler = new Handler();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview =  inflater.inflate(R.layout.fragment_player, container, false);
        Bundle arguments = getArguments();
        String id = null;
        if (arguments != null) {
            id = arguments.getString(MyArtist.ID);
            myTopTrackPosition = arguments.getInt(DetailActivityFragment.TRACK_POSITION);
            myTopTrackArrayList = arguments.getParcelableArrayList(DetailActivityFragment.TOP_TRACK_LIST);

        }


        mTrackNameText = (TextView) rootview.findViewById(R.id.name_track);

        mArtistNameText = (TextView) rootview.findViewById(R.id.name_artist);

        mAlbumNameText = (TextView) rootview.findViewById(R.id.name_album);

        mImageArtist = (ImageView) rootview.findViewById(R.id.image_artist_player);
        mSeekBar = (SeekBar) rootview.findViewById(R.id.seek_bar_player);

        mTrackDuration = (TextView) rootview.findViewById(R.id.track_duration);
        mActualPosition = (TextView) rootview.findViewById(R.id.track_position);

        //Set listeners to buttons.

        ImageButton mPreviousButton = (ImageButton) rootview.findViewById(R.id.previous_track);
        mPreviousButton.setOnClickListener(this);

        ImageButton mPauseButton = (ImageButton) rootview.findViewById(R.id.pause_track);
        mPauseButton.setOnClickListener(this);

        ImageButton mPlayButton = (ImageButton) rootview.findViewById(R.id.play_track);
        mPlayButton.setOnClickListener(this);

        ImageButton mNextButton = (ImageButton) rootview.findViewById(R.id.next_track);
        mNextButton.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setEnabled(false);

        return rootview;

    }

       @Override
    public void onStart() {
        super.onStart();

        intentPlayer = new Intent(getActivity(), MediaPlayerService.class);
        getActivity().bindService(intentPlayer, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableUISeekBar);
         if (mBound) {
            getActivity().unbindService(mConnection);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


    }

    // OnClick Listeners

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.play_track: {
                if(mBound) {
                    mService.play();
                    mSeekBar.setEnabled(true);
                }
                break;
            }

            case  R.id.pause_track: {
                if(mBound){
                    mService.pause();
                }
                break;
            }

            case  R.id.previous_track: {
                if(mBound){
                    resetUI();
                    setupUI(mService.previous());
                }
                break;
            }

            case  R.id.next_track: {
                if(mBound){
                    resetUI();
                    setupUI(mService.next());
                }
            }

        }
    }

    //UI Setup

    private void resetUI(){
        mSeekBar.setEnabled(false);
        mSeekBar.setProgress(0);
        mActualPosition.setText(String.format("%s:%s", "", ""));
        mTrackDuration.setText(String.format("%s:%s", "", ""));
        mService.seekTo(0);
    }

    private void setupUI(MyTopTrack myTopTrack) {

        mTrackNameText.setText(myTopTrack.getTrackName());
        mArtistNameText.setText(myTopTrack.getArtistName());
        mAlbumNameText.setText(myTopTrack.getAlbumName());

        String urlImage= myTopTrack.getUrlImageLarge();

        if (Helper.isValidURL(urlImage) ) {
            Picasso.with(getActivity())
                    .load(urlImage)
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(mImageArtist);

        }else{
            mImageArtist.setImageDrawable(getActivity().getResources()
                    .getDrawable(R.drawable.placeholder_image));
        }

        mSeekBar.setEnabled(false);

    }


    Runnable runnableUISeekBar = new Runnable() {
        public void run() {
            actualPlayerPos = mService.getTrackPosition();
            mActualPosition.setText(String.format("%dm:%ds",
                            TimeUnit.MILLISECONDS.toMinutes((long) actualPlayerPos),
                            TimeUnit.MILLISECONDS.toSeconds((long) actualPlayerPos) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) actualPlayerPos)))
            );

            int trackDuration = mService.getTrackDuration();

            mTrackDuration.setText(String.format("%dm:%ds",
                    TimeUnit.MILLISECONDS.toMinutes((long) trackDuration),
                    TimeUnit.MILLISECONDS.toSeconds((long) trackDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) trackDuration))));

            mSeekBar.setMax(trackDuration);
            mSeekBar.setProgress((int) actualPlayerPos);
            handler.postDelayed(this, 500);
        }
    };



    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            mService = binder.getService();
            mService.setTrackPositionInArray(myTopTrackPosition);
            mService.setTrackList(myTopTrackArrayList);

            mService.setListener(new MediaPlayerService.MediaPlayerServiceListener() {

                @Override
                public void trackCompleted() {
                    resetUI();
                }

                @Override
                public void startPreparing() {
                    Toast.makeText(getActivity(), R.string.song_preparing, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void finishedPreparing() {
                    mSeekBar.setEnabled(true);
                    handler.postDelayed(runnableUISeekBar, 500);

                }

            });

            mService.prepareTrackAndPlay();
            setupUI(myTopTrackArrayList.get(myTopTrackPosition));
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };




    // Seek Bar Listener

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(LOG_TAG, "progress changed " + progress);
        if(fromUser) {
            seekBar.setProgress(progress);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mService.seekTo(seekBar.getProgress());

    }



}
