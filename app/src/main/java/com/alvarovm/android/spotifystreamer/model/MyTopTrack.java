/**
 *  Copyright (C) 2015 AlvaroVM.com
 */

package com.alvarovm.android.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyTopTrack implements Parcelable {

    String trackName;
    String albumName;
    String urlImageLarge;
    String urlImageSmall;
    String previewUrl;
    String artistName;

    public MyTopTrack(String trackName, String albumName, String urlImageLarge, String urlImageSmall, String previewUrl, String artistName) {
        this.trackName = trackName;
        this.albumName = albumName;
        this.urlImageLarge = urlImageLarge;
        this.urlImageSmall = urlImageSmall;
        this.previewUrl = previewUrl;
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getUrlImageLarge() {
        return urlImageLarge;
    }

    public void setUrlImageLarge(String urlImageLarge) {
        this.urlImageLarge = urlImageLarge;
    }

    public String getUrlImageSmall() {
        return urlImageSmall;
    }

    public void setUrlImageSmall(String urlImageSmall) {
        this.urlImageSmall = urlImageSmall;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trackName);
        dest.writeString(this.albumName);
        dest.writeString(this.urlImageLarge);
        dest.writeString(this.urlImageSmall);
        dest.writeString(this.previewUrl);
        dest.writeString(this.artistName);
    }

    protected MyTopTrack(Parcel in) {
        this.trackName = in.readString();
        this.albumName = in.readString();
        this.urlImageLarge = in.readString();
        this.urlImageSmall = in.readString();
        this.previewUrl = in.readString();
        this.artistName = in.readString();
    }

    public static final Creator<MyTopTrack> CREATOR = new Creator<MyTopTrack>() {
        public MyTopTrack createFromParcel(Parcel source) {
            return new MyTopTrack(source);
        }

        public MyTopTrack[] newArray(int size) {
            return new MyTopTrack[size];
        }
    };
}
