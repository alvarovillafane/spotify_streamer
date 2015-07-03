/**
 *  Copyright (C) 2015 AlvaroVM.com
 */

package com.alvarovm.android.spotifystreamer.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.alvarovm.android.spotifystreamer.R;
import com.alvarovm.android.spotifystreamer.activity.DetailActivity;
import com.alvarovm.android.spotifystreamer.adapter.CustomAdapterSpotify;
import com.alvarovm.android.spotifystreamer.model.MyArtist;
import com.alvarovm.android.spotifystreamer.util.Helper;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class SpotifyFragment extends Fragment {

    CustomAdapterSpotify customAdapterSpotify;
    private ArrayList<MyArtist> myArtistListQuery;


    //Listener of the search query action.
    final private SearchView.OnQueryTextListener queryListener = new android.widget.SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            updateArtists(query);
            return true;
        }
    };


    public SpotifyFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("myArtistListQuery", myArtistListQuery);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_spotify, container, false);
        ListView listViewArtists = (ListView) rootView.findViewById(R.id.list_artists);
        List<MyArtist> listAdapter;

        if(savedInstanceState == null || !savedInstanceState.containsKey("myArtistListQuery")) {
            listAdapter = new ArrayList<MyArtist>();
        } else {
            listAdapter = savedInstanceState.getParcelableArrayList("myArtistListQuery");
            myArtistListQuery = (ArrayList) listAdapter;
        }

        customAdapterSpotify = new CustomAdapterSpotify(
                                         getActivity(),
                                         listAdapter);

        listViewArtists.setAdapter(customAdapterSpotify);

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyArtist artist = customAdapterSpotify.getItem(position);
                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, artist.getId());
                startActivity(detailIntent);
                }
        });


        return rootView;
    }

    /**
     * Set Search in Action Bar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        SearchView searchView = (SearchView)menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(queryListener);
    }

    private void updateArtists(String artistName) {
        Context context = getActivity().getApplicationContext();
        if( Helper.isOnline(context) ){
            FetchArtistsTask fetchArtistsTask = new FetchArtistsTask();
            fetchArtistsTask.execute(artistName);
        }else {
            Toast.makeText(context, "No network connection available.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Task to search artist
     */
    private class FetchArtistsTask extends AsyncTask<String, Void, ArtistsPager>{
        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();
        Resources res = getActivity().getResources();

        /**
         * Search and retrieve list of artists
         * @param params
         * @return
         */
        @Override
        protected ArtistsPager doInBackground(String... params) {

            SpotifyApi api = new SpotifyApi();;
            SpotifyService spotify = null;

            try {

                if(params.length == 0) { return null;}
                spotify = api.getService();
                Log.v(LOG_TAG, "Artist to search " + params[0]);

                return spotify.searchArtists(params[0]);

            } catch (RetrofitError ex) {
                Log.e(LOG_TAG, "Error at retrieving Artist data " + ex.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        ex.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
           myArtistListQuery = new ArrayList<MyArtist>();
           customAdapterSpotify.clear();

           if(artistsPager != null && !artistsPager.artists.items.isEmpty()) {
               MyArtist myArtist;
               for (Artist artist : artistsPager.artists.items) {
                   
                   if( artist.images.isEmpty() ) {
                       myArtist = new MyArtist(artist.name, artist.id);
                   } else {
                       myArtist = new MyArtist(artist.name, artist.images.get(0).url, artist.id);
                   }
                   //List to save for recovering when resume application
                   myArtistListQuery.add(myArtist);
                   //Add to ArrayAdapter
                   customAdapterSpotify.add(myArtist);
               }
           }else{
               Toast toast = Toast.makeText(getActivity(),res.getString(R.string.no_artist_found), Toast.LENGTH_LONG);
               toast.setGravity(Gravity.CENTER, 0,0);
               toast.show();
           }
      }

    }
}
