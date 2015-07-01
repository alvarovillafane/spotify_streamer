/**
 *  Copyright (C) 2015 AlvaroVM.com
 */

package com.alvarovm.android.spotifystreamer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alvarovm.android.spotifystreamer.R;
import com.alvarovm.android.spotifystreamer.model.MyTopTrack;
import com.alvarovm.android.spotifystreamer.util.Helper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *   ArrayAdapter for Top Tracks Lists
 */
public class CustomAdapterTopTracks extends ArrayAdapter<MyTopTrack> {
    private final Activity context;
    private final List<MyTopTrack> myTopTracksList;
    private final int IMAGE_HEIGHT = 200;
    private final int IMAGE_WIDTH = 200;

    static class ViewHolder {
        public   TextView txtAlbum;
        public TextView txtTrack;
        ImageView imageAlbum;
    }


    public CustomAdapterTopTracks(Activity context, List<MyTopTrack> myTopTracksList) {
        super(context, R.layout.list_item_top_tracks, myTopTracksList);
        this.context = context;
        this.myTopTracksList = myTopTracksList;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_top_tracks, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtAlbum = (TextView) convertView.findViewById(R.id.name_album);
            viewHolder.txtTrack = (TextView) convertView.findViewById(R.id.name_track);
            viewHolder.imageAlbum = (ImageView) convertView.findViewById(R.id.image_album);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        MyTopTrack myTopTrack = myTopTracksList.get(pos);
        viewHolder.txtAlbum.setText(myTopTrack.getAlbumName());
        viewHolder.txtTrack.setText(myTopTrack.getTrackName());

        String urlImage = myTopTrack.getUrlImageLarge();

        if ( Helper.isValidURL(urlImage) ) {

            Picasso.with(context)
                    .load(urlImage)
                    .resize(IMAGE_WIDTH, IMAGE_HEIGHT).centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(viewHolder.imageAlbum);
        } else {
            viewHolder.imageAlbum.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.placeholder_image));
        }

        return convertView;
    }
}
