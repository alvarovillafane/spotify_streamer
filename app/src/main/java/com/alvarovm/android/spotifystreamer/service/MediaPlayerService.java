package com.alvarovm.android.spotifystreamer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alvarovm.android.spotifystreamer.model.MyTopTrack;

import java.io.IOException;
import java.util.List;

/**
 * Created by root on 14/08/15.
 */
public class MediaPlayerService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener  {

    private String LOG_TAG = this.getClass().getSimpleName();
    private MediaPlayer mMediaPlayer = null;
    private final int FIRST_ELEMENT = 0;
    Integer trackPositionInArray = 0;
    Integer trackDuration = 0;
    List<MyTopTrack> trackList = null;
    MediaPlayerServiceListener mListener;
    private boolean isPrepared = false;


    private final IBinder mBinder = new MediaPlayerBinder();

    public class MediaPlayerBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    public interface MediaPlayerServiceListener {
        public void finishedPreparing();
        public void trackCompleted();
        public void startPreparing();

    }


    public void setListener(MediaPlayerServiceListener listener) {
        mListener = listener;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if(mMediaPlayer == null) {
            initMediaPlayer();
        }
    }

    public void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();;
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // Media Player Control

    synchronized public void prepareTrackAndPlay() {
        isPrepared = false;
        if (mListener!=null)
            mListener.startPreparing();

        if(isPlaying()){
            mMediaPlayer.stop();
        }

        Uri uri = Uri.parse(getActualTrackPreviewUrl());

        synchronized (this) {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //mMediaPlayer.setLooping(true);
            try {
                mMediaPlayer.setDataSource(getApplicationContext(), uri);
                mMediaPlayer.prepareAsync();
            } catch (IOException IOEx) {
                Log.e(LOG_TAG, IOEx.getMessage());
                mMediaPlayer.reset();
            }
        }
    }




    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        play();
        trackDuration = mp.getDuration();
        if (mListener!=null)
            mListener.finishedPreparing();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(isPlaying()) {
            mMediaPlayer.pause();
        }

        if (mListener!=null)
            mListener.trackCompleted();

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(LOG_TAG, "Error en Media player: " + what + " " + extra);
        mp.reset();
        isPrepared = false;

        return false;
    }




    public void play(){
        if(isPrepared) {
            mMediaPlayer.start();
        }
    }

    public void pause(){
        if(isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public MyTopTrack next(){
       trackPositionInArray++;
       prepareTrackAndPlay();
       return trackList.get(trackPositionInArray);

    }

    public MyTopTrack previous(){
        trackPositionInArray--;
        prepareTrackAndPlay();
        return trackList.get(trackPositionInArray);

    }

    public void stopAndReset(){
        mMediaPlayer.stop();
        mMediaPlayer.reset();
    }

    public void seekTo(int progress){
        if(isPrepared) {
            mMediaPlayer.seekTo(progress);
        }
    }


    //Helpers

    public String getActualTrackPreviewUrl(){
        int listSize = trackList.size();

        if(trackPositionInArray <  listSize && trackPositionInArray >= 0 ) {
            return trackList.get(trackPositionInArray).getPreviewUrl();
        } else if (trackPositionInArray >= listSize){
            trackPositionInArray = FIRST_ELEMENT;
            return trackList.get(trackPositionInArray).getPreviewUrl();
        } else {
            trackPositionInArray = listSize - 1;
            return trackList.get(trackPositionInArray).getPreviewUrl();

        }
    }



    /**
     * Method to check if the song is playing,
     * to replace the built-in method which is asynchronous
     *
     * @return
     *
     */

    public boolean isPlaying() {
        //return isPlaying;
        try {
            return mMediaPlayer.isPlaying();
        } catch (IllegalStateException ex){
            Log.e(LOG_TAG, ex.getMessage());
        }

        return false;

    }


    // Getter and Setters

    public int getTrackPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getTrackPositionInArray(){
        return trackPositionInArray;

    }

    public String getTrackName(){
        return trackList.get(trackPositionInArray).getTrackName();
    }

    public void setTrackPositionInArray(Integer trackPositionInArray) {
        this.trackPositionInArray = trackPositionInArray;
    }

    public List<MyTopTrack> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<MyTopTrack> trackList) {
        this.trackList = trackList;
    }

    public Integer getTrackDuration() {
        return trackDuration;
    }

    public void setTrackDuration(Integer trackDuration) {
        this.trackDuration = trackDuration;
    }

}



