/**
 *  Copyright (C) 2015 AlvaroVM.com
 */

package com.alvarovm.android.spotifystreamer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 *   ArrayAdapter for Top Tracks Lists
 */
public class CustomAdapterTopTracks extends ArrayAdapter<Track> {
    private final Activity context;
    private final List<Track> tracksList;
    private final int IMAGE_SIZE = 0;
    private final int IMAGE_HEIGHT = 200;
    private final int IMAGE_WIDTH = 200;


    public CustomAdapterTopTracks(Activity context, List<Track> tracksList) {
        super(context, R.layout.list_item_top_tracks, tracksList);
        this.context = context;
        this.tracksList = tracksList;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.list_item_top_tracks, null, true);

        Track track = tracksList.get(pos);
        //Set Album Name
        TextView txtAlbum = (TextView) rowView.findViewById(R.id.name_album);
        txtAlbum.setText(track.album.name);

        //Set track name
        TextView txtTrack = (TextView) rowView.findViewById(R.id.name_track);
        txtTrack.setText(track.name);

        //Set Image album
        ImageView imageAlbum = (ImageView) rowView.findViewById(R.id.image_album);
        List<Image> listAlbumImages= track.album.images;
        String url =  null;

        if( ! listAlbumImages.isEmpty()){
            url = listAlbumImages.get(IMAGE_SIZE).url;
        }

        Picasso.with(context)
                .load(url)
                .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(imageAlbum);

        return rowView;
    }

}
