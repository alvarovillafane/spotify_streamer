/**
 *  Copyright (C) 2015 AlvaroVM.com
 */

package com.alvarovm.android.spotifystreamer;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private CustomAdapterTopTracks customAdapterTopTracks;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ListView listViewTopTracks = (ListView) rootView.findViewById(R.id.listview_toptracks);

        //Initialization of Custom Track ArrayAdapter
        customAdapterTopTracks = new CustomAdapterTopTracks(
                                     getActivity(),
                                     new ArrayList<Track>());

        listViewTopTracks.setAdapter(customAdapterTopTracks);

        listViewTopTracks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Track track = customAdapterTopTracks.getItem(position);
                Toast.makeText(getActivity().getApplicationContext(), track.name, Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }

    /**
     *  Initializate Activity and populate with top tracks
     */
    @Override
    public void onStart() {
        super.onStart();
        Intent intentMain = getActivity().getIntent();
        if (intentMain != null && intentMain.hasExtra(Intent.EXTRA_TEXT)) {
            String id= intentMain.getStringExtra(Intent.EXTRA_TEXT);
            updateTopTracks(id);
        }
    }

    private void updateTopTracks(String id) {
        FetchTopTracks fetchTopTracks = new FetchTopTracks();
        fetchTopTracks.execute(id);
    }

    private class FetchTopTracks extends AsyncTask<String, Void, Tracks> {
        Resources res = getActivity().getResources();

        @Override
        protected Tracks doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();;
            SpotifyService spotify = null;
            HashMap<String, Object> countryMap = new HashMap<>();
            countryMap.put(res.getString(R.string.country), res.getString(R.string.country_code));

            //Search for top tracks
            try {
                if(params.length == 0){ return null; }
                spotify = api.getService();
                Log.v(LOG_TAG, "Top tracks from artist " + params[0]);

                return spotify.getArtistTopTrack(params[0],countryMap);

            } catch (RuntimeException ex) {
                Log.e(LOG_TAG, "Error at retrieving top tracks data " + ex.getMessage());
            }

            return null;
        }

        /**
         * Update ArrayAdapter and refresh UI
         * @param listTracks
         */
        @Override
        protected void onPostExecute(Tracks listTracks) {

            if(listTracks != null && !listTracks.tracks.isEmpty()) {
                customAdapterTopTracks.clear();
                for (Track track : listTracks.tracks) {
                    customAdapterTopTracks.add(track);
                }
            }else {
                Toast.makeText(getActivity().getApplicationContext(),
                               res.getString(R.string.no_top_tracks),
                               Toast.LENGTH_SHORT).show();
            }
        }


    }

}
