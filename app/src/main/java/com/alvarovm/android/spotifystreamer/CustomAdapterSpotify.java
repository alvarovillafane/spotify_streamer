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

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;

/**
 *
 *  ArrayAdapter for Artists Lists
 */
public class CustomAdapterSpotify extends ArrayAdapter<Artist> {
    private final Activity context;
    private final List<Artist> artistsList;
    private final int IMAGE_SIZE = 0;
    private final int IMAGE_HEIGHT = 200;
    private final int IMAGE_WIDTH = 200;


    public CustomAdapterSpotify(Activity context, List<Artist> artistsList) {
        super(context, R.layout.list_item_artists, artistsList);
        this.context = context;
        this.artistsList = artistsList;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_item_artists, null, true);

        // Set Artist Name
        TextView txtTitle = (TextView) rowView.findViewById(R.id.name_artist);
        txtTitle.setText(artistsList.get(pos).name);

        //Set Image of artist
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image_artist);
        List<Image> artistImages= artistsList.get(pos).images;
        String urlImages = null;

        if( ! artistImages.isEmpty()) {
            urlImages = artistImages.get(IMAGE_SIZE).url;
        }

        Picasso.with(context)
               .load(urlImages)
               .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
               .centerCrop()
               .placeholder(R.drawable.placeholder_image)
               .error(R.drawable.placeholder_image)
               .into(imageView);

        return rowView;
    }
}
