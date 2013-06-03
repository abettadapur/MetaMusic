package org.thingswithworth.metamusic.activities.adapters;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.gm.api.model.Song;
import org.thingswithworth.metamusic.Callback;
import org.thingswithworth.metamusic.R;
import org.thingswithworth.metamusic.activities.SongPlayActivity;
import org.thingswithworth.metamusic.fileio.LocalImageTask;
import org.thingswithworth.metamusic.media.MusicPlayer;
import org.thingswithworth.metamusic.model.Album;
import org.thingswithworth.metamusic.net.NetImageTask;

import java.util.ArrayList;

/**
 * This is the adapter for the album list seen in the Artist activity. Each album is an expandable parent that has its songs as children
 * @author: Andrey Kurenkov
 */
public class AlbumListAdapter extends BaseExpandableListAdapter{
    private LayoutInflater inflater;
    private ArrayList<Album> albums;
    private Activity context;
    private MusicPlayer player;

    /**
     * Basic constructor that sets up the inflater and instance variables.
     * @param context SearchActivity within which this is used, needed to enable Android functionality
     * @param parent The Album for which to create a list
     */
    public AlbumListAdapter(Activity context, ArrayList<Album> parent){
        albums = parent;
        inflater = LayoutInflater.from(context);
        this.context=context;
        player=MusicPlayer.getPlayer();
    }


    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return albums.size();
    }

    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return albums.get(i).getSongs().size();
    }

    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return albums.get(i).getName();
    }

    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return albums.get(i).getSongs().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return albums.get(i).getName().hashCode();
    }

    @Override
    public long getChildId(int i, int i1) {
        return albums.get(i).getSongs().get(i1).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //TODO probably inefficient to not reuse-views if possible
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.list_album, viewGroup,false);
        }
        final Album album=albums.get(i);
        TextView textView = (TextView) view.findViewById(R.id.listAlbumTextView);
        //"i" is the position of the parent/group in the list
        textView.setText(album.getName());

        ImageView imageView=(ImageView)view.findViewById(R.id.albumImageView);
        Callback.ParamCallback<Bitmap> callback=new Callback.ParamCallback<Bitmap>(){
            public void onCompletion(Bitmap... param){
                album.setCachedArt(param[0]);
            }
        };
        if(album.getImagePath()!=null){
            new LocalImageTask(imageView,callback).execute(album.getImagePath());
        }else if(album.getImageUrl()!=null){
            new NetImageTask(imageView,callback).execute(album.getImageUrl());
        }

        ImageButton playButton=(ImageButton)view.findViewById(R.id.albumPlay);
        playButton.setFocusable(false);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                for(Song song:album.getSongs()){
                    player.queueSong(new MusicPlayer.PlaylistSong(album.getArtist(),album,song),player.getPlayIndex()+(count++));
                }
                player.playCurrentSong();
                Intent intent = new Intent(context, SongPlayActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        });

        ImageButton addButton=(ImageButton)view.findViewById(R.id.albumAdd);
        addButton.setFocusable(false);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Song song:album.getSongs()){
                    MusicPlayer.getPlayer().queueSong(new MusicPlayer.PlaylistSong(album.getArtist(),album,song),true);
                }
                Toast toast= Toast.makeText(context,album.getName()+" added to current queue.",Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        return view;
    }

    //TODO probably inefficient to not reuse-views if possible
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.list_album_song, viewGroup,false);
        }
        final Album album=albums.get(i);
        final Song song=album.getSongs().get(i1);
        TextView textView = (TextView) view.findViewById(R.id.listAlbumSongTextView);
        //"i" is the position of the parent/group in the list and
        //"i1" is the position of the child
        textView.setText(song.getName());
        ImageButton playButton=(ImageButton)view.findViewById(R.id.songPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.queueSong(new MusicPlayer.PlaylistSong(album.getArtist(),album,song),player.getPlayIndex());
                player.playCurrentSong();
                Intent intent = new Intent(context, SongPlayActivity.class);
                context.startActivity(intent);
            }
        });

        ImageButton addButton=(ImageButton)view.findViewById(R.id.songAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer.getPlayer().getPlaylist().add(new MusicPlayer.PlaylistSong(album.getArtist(),album,song));
                Toast toast= Toast.makeText(context,song.getTitle()+" added to current queue.",Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}