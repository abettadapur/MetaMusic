package org.thingswithworth.metamusic.model;

import android.util.Log;
import com.android.gm.api.model.Song;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: andrey
 * Date: 4/19/13
 * Time: 9:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MusicCollection {
    private ArrayList<Song> songs;
    private HashMap<String, ArrayList<Song>> genreSongs;
    private HashMap<Song,Album> songAlbums;
    private HashMap<String,Artist> artists;
    private HashMap<String,Album> albums;
    private HashSet<String> names;
    private int maxInGenre;

    public MusicCollection(){
        songs=new ArrayList<Song>();
        names=new HashSet<String>();
        songs=new ArrayList<Song>();
        artists=new HashMap<String,Artist>();
        albums=new HashMap<String,Album>();
        songAlbums=new HashMap<Song, Album>();
        genreSongs = new HashMap<String, ArrayList<Song>>();
    }

    public ArrayList<Song> getSongs(){
        return songs;
    }

    public void addSongs(ArrayList<Song> newSongs)
    {
        for(Song song:newSongs){
            if(!names.contains(song.getName()+song.getArtist())){
                names.add(song.getName() + song.getArtist());
                songs.add(song);

                String artistName=song.getArtist();
                Artist artist=new Artist(artistName);
                if(!artists.containsKey(artistName))
                    artists.put(artistName,artist);
                else
                    artist=artists.get(artistName);

                Album album=new Album(song.getAlbum(),song.getAlbumArtUrl());
                if(!albums.containsKey(artistName+" "+song.getAlbum()))
                    albums.put(song.getArtist()+" "+song.getAlbum(),album);
                else
                    album=getAlbum(artistName,song.getAlbum());
                album.addSong(song,song.getTrack());

                if(!artist.getAlbums().contains(album))
                    artist.addAlbum(album);

                album.setArtist(artist);

                songAlbums.put(song,album);

                if(genreSongs.containsKey(song.getGenre()))
                {
                    genreSongs.get(song.getGenre()).add(song);
                    int count=genreSongs.get(song.getGenre()).size();
                    if(maxInGenre<count)
                        maxInGenre=count;
                }
                else
                {
                    ArrayList<Song> genreList=new ArrayList<Song>();
                    genreList.add(song);
                    genreSongs.put(song.getGenre(),genreList);
                }
            }
        }
    }

    public int getMaxInGenre(){
        return maxInGenre;
    }

    private String[][] same = new String[][]{{"Hip-Hop", "Hip Hop", "HipHop", "West Coast Hip Hop", "Hip-Hop/Rap"}};
    public void cleanGenreCount(){
        String start=null;
        HashMap<String, ArrayList<Song>> addMap = new HashMap<String,  ArrayList<Song>>();
        ArrayList<String> removes = new ArrayList<String>();
        for (String s: genreSongs.keySet()){
            start = null;
            if (s.contains("/")){
                start = s.replaceAll("/.*", "");
                start = start.toUpperCase().charAt(0)+start.toLowerCase().substring(1);
                if (!addMap.containsKey(start)) {
                    addMap.put(start, genreSongs.get(s));
                } else {
                    addMap.get(start).addAll(genreSongs.get(s));
                }
                removes.add(s);
            }
            if (s.length() <=2 || s.equalsIgnoreCase("other") || s.equalsIgnoreCase("unknown")){
                removes.add(s);
            }
        }
        for(String s: addMap.keySet()){
            Log.e("CLEANUP", "Adding " +s);
            genreSongs.put(s, addMap.get(s));
        }
        for (int i=0; i<same.length; i++){
            for (String s: genreSongs.keySet()){
                for (int j = 0; j<same[i].length; j++){
                    if (start==null && s.equalsIgnoreCase(same[i][j])){
                        start = same[i][j];
                    } else if (s.equalsIgnoreCase(same[i][j])){
                        if(genreSongs.containsKey(start))
                            genreSongs.get(start).addAll(genreSongs.get(s));
                        else
                            genreSongs.put(start,genreSongs.get(s));
                        removes.add(s);
                    }
                }
            }
        }
        for(String s: removes){
            genreSongs.remove(s);
        }
    }

    public HashMap<String, ArrayList<Song>> getSongGenres()
    {
        return genreSongs;
    }

    public Artist getArtist(String artistName){
        return artists.get(artistName);
    }

    public Album getAlbum(String artistName, String albumName){
        return albums.get(artistName+" "+albumName);
    }

    public Collection<Artist> getArtists(){
        return artists.values();
    }

    public Collection<String> getArtistNames(){
        return artists.keySet();
    }

    public Collection<Album> getAlbums(){
        return albums.values();
    }

    public Collection<String> getAlbumNames(){
        return albums.keySet();
    }

    public Album getSongAlbum(Song song){
        return songAlbums.get(song);
    }
}
