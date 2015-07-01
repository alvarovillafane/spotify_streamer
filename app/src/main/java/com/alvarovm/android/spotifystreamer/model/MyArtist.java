package com.alvarovm.android.spotifystreamer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AlvaroVM.com
 */
public class MyArtist implements Parcelable{
    private final String nameArtist;
    private final String  urlImageArtist;
    private final String id;

    public MyArtist(String nameArtist, String urlImageArtist, String id) {
        this.nameArtist = nameArtist;
        this.urlImageArtist = urlImageArtist;
        this.id = id;
    }

    public MyArtist(String nameArtist, String id) {
        this.nameArtist = nameArtist;
        this.urlImageArtist = null;
        this.id = id;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public String getUrlImageArtist() {
        return urlImageArtist;
    }

    public String getId() {
        return id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameArtist);
        dest.writeString(this.urlImageArtist);
        dest.writeString(this.id);
    }

    protected MyArtist(Parcel in) {
        this.nameArtist = in.readString();
        this.urlImageArtist = in.readString();
        this.id = in.readString();
    }

    public static final Creator<MyArtist> CREATOR = new Creator<MyArtist>() {
        public MyArtist createFromParcel(Parcel source) {
            return new MyArtist(source);
        }

        public MyArtist[] newArray(int size) {
            return new MyArtist[size];
        }
    };
}
