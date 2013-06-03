package org.thingswithworth.metamusic.activities.adapters;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.gm.api.model.Song;
import org.thingswithworth.metamusic.MusicApplication;
import org.thingswithworth.metamusic.R;
import org.thingswithworth.metamusic.activities.SearchActivity;
import org.thingswithworth.metamusic.activities.ArtistActivity;
import org.thingswithworth.metamusic.activities.SongPlayActivity;
import org.thingswithworth.metamusic.media.MusicPlayer;
import org.thingswithworth.metamusic.model.Album;
import org.thingswithworth.metamusic.model.Artist;

import java.util.ArrayList;

/**
 * The adapter for the view of search results
 * @author: andrey
 */
public class SearchListAdapter extends BaseExpandableListAdapter implements TextWatcher {
    private String lastSearch;
    private ArrayList<Artist> artists;
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;
    private LayoutInflater inflater;
    private Activity context;
    private String[] categories;
    /**
     * Basic constructor that sets up the inflater and instance variables. Initially the list will contain all songs, albums, and artists in the global collection.
     * @param context SearchActivity within which this is used, needed to enable Android functionality
     * @param search The TextView from which to get input (can be a TextEdit)
     */
    public SearchListAdapter(Activity context,TextView search){
        lastSearch="";
        inflater = LayoutInflater.from(context);
        artists=new ArrayList<Artist>();
        albums=new ArrayList<Album>();
        songs=new ArrayList<Song>();
        categories=new String[]{"Artists","Albums","Songs"};
        search.addTextChangedListener(this);
        this.context=context;
        songs.addAll(MusicApplication.getGlobalCollection().getSongs());
        albums.addAll(MusicApplication.getGlobalCollection().getAlbums());
        artists.addAll(MusicApplication.getGlobalCollection().getArtists());
    }


    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return 3;
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        if(i==0)
            return artists.size();
        if(i==1)
            return albums.size();
        return songs.size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return categories[i];
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        if(i==0)
            return artists.get(i1).getName();
        if(i==1)
            return albums.get(i1).getName();
        return songs.get(i1).getName();
    }

    @Override
    public long getGroupId(int i) {
        return categories[i].hashCode();
    }

    @Override
    public long getChildId(int i, int i1) {
        return getChild(i,i1).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.search_group, viewGroup,false);
        }

        TextView textView = (TextView) view.findViewById(R.id.searchResultName);
        textView.setText(categories[i]);

        textView = (TextView) view.findViewById(R.id.searchResultDetail);
        textView.setText(getChildrenCount(i)+" Results Found");
        //return the entire view
        return view;
    }

    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_album_song, viewGroup,false);
        }
        final MusicPlayer player=MusicPlayer.getPlayer();
        final TextView textView = (TextView) view.findViewById(R.id.listAlbumSongTextView);
        String name=null;
        final ArrayList<Song> songsToAdd=new ArrayList<Song>();
        if(i==0){
            final Artist artist=artists.get(i1);
            for(Album album: artist.getAlbums()){
                songsToAdd.addAll(album.getSongs());
            }
            name=artist.getName();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArtistActivity.launchActivityFor(context,artist.getName());
                }
            });
        }
        if(i==1){
            Album album=albums.get(i1);
            songsToAdd.addAll(album.getSongs());
            name=album.getName();
        }
        if(i==2){
            Song song=songs.get(i1);
            name=song.getTitle();
            songsToAdd.add(song);
        }

        //"i" is the position of the parent/group in the list and
        //"i1" is the position of the child
        textView.setText(name);
        ImageButton playButton=(ImageButton)view.findViewById(R.id.songPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(Song song:songsToAdd){
                    Album album=MusicApplication.getGlobalCollection().getSongAlbum(song);
                    player.queueSong(new MusicPlayer.PlaylistSong(album.getArtist(),album,song),player.getPlayIndex()+(count++));
                }
                player.playCurrentSong();
                Intent intent = new Intent(context, SongPlayActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });

        ImageButton addButton=(ImageButton)view.findViewById(R.id.songAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Song song:songsToAdd){
                    Album album=MusicApplication.getGlobalCollection().getSongAlbum(song);
                    MusicPlayer.getPlayer().queueSong(new MusicPlayer.PlaylistSong(album.getArtist(),album,song),true);
                }
                Toast toast= Toast.makeText(context,textView.getText()+" added to current queue.",Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        //return the entire view
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    private AsyncTask<Void,Void,Void> search;
    @Override
    public void onTextChanged(final CharSequence s, int start, int before, int count) {
        if(search!=null && !search.getStatus().equals(AsyncTask.Status.FINISHED)){
            search.cancel(true);
        }
        search=new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String search=s.toString().toLowerCase();
                if(search.startsWith(lastSearch)){
                    ArrayList<Integer> artistRemove=new ArrayList<Integer>();
                    ArrayList<Integer> albumRemove=new ArrayList<Integer>();
                    ArrayList<Integer> songsRemove=new ArrayList<Integer>();
                    for(int i=0;i<artists.size();i++){
                        if(!artists.get(i).getName().toLowerCase().startsWith(search)){
                            artistRemove.add(i);
                        }
                    }
                    for(int i=0;i<albums.size();i++){
                        if(!albums.get(i).getName().toLowerCase().startsWith(search)){
                            albumRemove.add(i);
                        }
                    }
                    for(int i=0;i<songs.size();i++){
                        if(!songs.get(i).getName().toLowerCase().startsWith(search)){
                            songsRemove.add(i);
                        }
                    }
                    for(int r=0;r<artistRemove.size();r++)
                        artists.remove(artistRemove.get(r).intValue()-r);
                    for(int r=0;r<albumRemove.size();r++)
                        albums.remove(albumRemove.get(r).intValue()-r);
                    for(int r=0;r<songsRemove.size();r++)
                        songs.remove(songsRemove.get(r).intValue()-r);


                } else{
                    for(Artist artist:MusicApplication.getGlobalCollection().getArtists()){
                        if(artist.getName().toLowerCase().startsWith(search)){
                            artists.add(artist);
                        }
                    }
                    for(Album album:MusicApplication.getGlobalCollection().getAlbums()){
                        if(album.getName().toLowerCase().startsWith(search)){
                            albums.add(album);
                        }
                    }
                    for(Song song:MusicApplication.getGlobalCollection().getSongs()){
                        if(song.getName().toLowerCase().startsWith(search)){
                            songs.add(song);
                        }
                    }
                }
                lastSearch=search;
                return null;
            }
            @Override
            protected void onPostExecute(Void v){
                notifyDataSetChanged();
            }
        };
        search.execute();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}