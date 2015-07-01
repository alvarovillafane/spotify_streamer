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
import com.alvarovm.android.spotifystreamer.model.MyArtist;
import com.alvarovm.android.spotifystreamer.util.Helper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 *
 *  ArrayAdapter for Artists Lists
 */
public class CustomAdapterSpotify extends ArrayAdapter<MyArtist> {
    private final Activity context;
    private final List<MyArtist> artistsList;
    private final int IMAGE_SIZE = 0;
    private final int IMAGE_HEIGHT = 200;
    private final int IMAGE_WIDTH = 200;

    static class ViewHolder {
        public  TextView txtTitle;
        public ImageView imageView;
    }


    public CustomAdapterSpotify(Activity context, List<MyArtist> artistsList) {
        super(context, R.layout.list_item_artists, artistsList);
        this.context = context;
        this.artistsList = artistsList;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_artists, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.name_artist);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_artist);
            convertView.setTag(viewHolder);
        }else{
          viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtTitle.setText(artistsList.get(pos).getNameArtist());

        String urlImage= artistsList.get(pos).getUrlImageArtist();

        if ( Helper.isValidURL(urlImage) ) {
            Picasso.with(context)
                    .load(urlImage)
                    .resize(IMAGE_WIDTH, IMAGE_HEIGHT)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(viewHolder.imageView);
        }else{
            viewHolder.imageView.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.placeholder_image));
        }

        return convertView;
    }
}
