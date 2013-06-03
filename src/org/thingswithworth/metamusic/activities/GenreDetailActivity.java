package org.thingswithworth.metamusic.activities;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.gm.api.model.Song;
import org.thingswithworth.metamusic.MusicApplication;
import org.thingswithworth.metamusic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Simple class to display the artist of a given genre
 * @author Alex Bettadapur
 */
public class GenreDetailActivity extends AppActivity  implements AdapterView.OnItemClickListener
{
    private ListView view;
    /**
     * Gives values to instance variables and sets up view of Activity.
     * Basically just sets up list.
     * @param savedInstanceState not used
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre_detail_activity);
        view = (ListView)findViewById(R.id.listView);
        String genre = getIntent().getStringExtra("Genre");
        view.setOnItemClickListener(this);
        populateList(genre);
    }

    /**
     * Finds the artists for the activity and sets up the adapter for the list
     * @param genre the genre of this activity
     */
    private void populateList(String genre)
    {
        ArrayList<Song> songs= MusicApplication.getGlobalCollection().getSongGenres().get(genre);
        HashSet<String> filteredArtists = new HashSet<String>();
        for(Song s: songs)
        {
            filteredArtists.add(s.getArtist());
        }
        String[] stringarr = Arrays.copyOf(filteredArtists.toArray(), filteredArtists.size(), String[].class);
        Arrays.sort(stringarr);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarr);
        adapter.notifyDataSetChanged();
        TextView header=new TextView(this);
        header.setText(genre + " Artists");
        header.setClickable(false);
        header.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
        header.setGravity(Gravity.CENTER);
        view.addHeaderView(header, null, false);
        view.setAdapter(adapter);
    }

    /**
     * Launches activity for the artist and finishsed this activity.
     * @see AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
            String artist = (String)adapterView.getItemAtPosition(i);
            ArtistActivity.launchActivityFor(this,artist);
            finish();
    }
}