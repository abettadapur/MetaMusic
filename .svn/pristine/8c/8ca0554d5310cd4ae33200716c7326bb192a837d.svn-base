package org.thingswithworth.metamusic.fileio;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import com.android.gm.api.model.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


public class LocalMedia {
    private final static boolean debug = false;
    private final static boolean debugFiles = false;
    private final static MediaMetadataRetriever retriever=new MediaMetadataRetriever();

    public static ArrayList<Song> getLocalSongs(Context activity){
        ArrayList<Song> songs = new ArrayList<Song>();
        final HashMap<String, Song> songIDMap = new HashMap<String, Song>();
        final HashMap<String, String> genreIDMap = getGenreIDMap(activity);
        debug("shit's good");
        String[] proj = {MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID};
        final String select = MediaStore.Audio.Media.IS_MUSIC + "=1";
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;//uri.parse("content://com.google.android.music.MusicContent/playlists");
        final Cursor cursor = activity.getContentResolver().query(uri, proj, select, null, null);
        if (cursor==null){
            debug("Derp is null!");
            return null;
        } else{
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    songs.add(new Song() {
                        {
                            setTitle(cursor.getString(0));
                            setName(cursor.getString(0));
                            setArtist(cursor.getString(1)==null?"Unknown Arist":cursor.getString(1));
                            setAlbumArtist(cursor.getString(1)==null?"Unknown Album Artist":cursor.getString(1));
                            setAlbum(cursor.getString(2)==null?"Unknown Album":cursor.getString(2));
                            setLocal(true);
                            if (cursor.getString(3)!=null) setTrack(Integer.parseInt(cursor.getString(3).trim()));
                            if (cursor.getString(3)!=null) setTrack(Integer.parseInt(cursor.getString(3).trim()));
                            if (cursor.getString(4)!=null) setYear(Integer.parseInt(cursor.getString(4).trim()));
                            setId(cursor.getString(5));
                            if (cursor.getString(6)!=null) setDurationMillis(Long.parseLong(cursor.getString(6)));
                            songIDMap.put(cursor.getString(5), this);
                            setUrl(cursor.getString(7));
                            setAlbumArtUrl(cursor.getString(8));
                        }
                    });
                } while (cursor.moveToNext());
            }
        }

        debug("Derp shit's fucking good");
        cursor.close();

        setSongGenres(activity, songIDMap, genreIDMap);

        HashMap<String,String> arts=getAlbumArts(activity);
        for (Song song:songs)
            song.setAlbumArtUrl(arts.get(song.getAlbumArtUrl()));
        return songs;
    }


    private static void setSongGenres(Context activity, HashMap<String, Song> songIDMap, HashMap<String, String> genreIDMap){
        debug("shit's good");
        Collection<Song> songsToSet=songIDMap.values();
        String genre;
        String[] proj = {MediaStore.Audio.Genres.Members.TITLE, MediaStore.Audio.Genres.Members.GENRE_ID, MediaStore.Audio.Genres.Members.AUDIO_ID};
        for (int i=0; i<genreIDMap.size(); i++){
            final String select = MediaStore.Audio.Media.IS_MUSIC + "=1";
            Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", i);//uri.parse("content://com.google.android.music.MusicContent/playlists");
            Cursor cursor = activity.getContentResolver().query(uri, proj, select, null, null);
            if (cursor==null){
                debug("GenreMemberCursor is null!");
                return;
            } else{
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        if(cursor.getString(2)!=null){
                            try{
                                genre = genreIDMap.get(cursor.getString(1));
                                Song song=songIDMap.get(cursor.getString(2));
                                debug("Setting genre for song",""+song);
                                song.setGenre(genre == null ? "No Genre" : genre);
                                songsToSet.remove(song);
                            }catch(NullPointerException exc){
                                //TODO: google analyitics stuff
                                debug("Null in getGenres",""+exc);
                            }
                        }else{
                            debug("TITLE: "+cursor.getString(0));
                            debug(".GENRE_ID:"+cursor.getString(1));
                            debug(".SONG_ID:"+cursor.getString(2));
                        }
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            for(Song song:songsToSet)
                song.setGenre("No Genre");
        }
        debug("Genre Member shit's fucking good");
    }


    private static HashMap<String,String> getAlbumArts(Context activity){
        String[] proj = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART};
        Uri uri=MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;//uri.parse("content://com.google.android.music.MusicContent/playlists");
        HashMap<String,String> toReturn=new HashMap<String, String>();
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor==null){
            debug("GenreMemberCursor is null!");
            return null;
        } else{
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    toReturn.put(cursor.getString(0),cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        return toReturn;
    }

    /**
     * Alternative approach to finding music by just scanning. Should not be used except in cases where Cursor randomly fails - otherwise use getLocalSongs.
     * @return Bunch of songs
     */
    public static ArrayList<Song> scanFileSystem(){
        ArrayList<Song> songs = new ArrayList<Song>();
        File[] files=FileScanner.scanFileSystem(new FileScanner.MusicFilter());
        debugFiles("Found "+files.length +" songs");
        int counter=0;
        for(File file:files){
            Song song=extractSongFromFile(file);
            debugFiles("At "+counter++);
            if(counter==files.length-1)
                break;
            if(song!=null)
                songs.add(song);
        }
        return songs;
    }

    /**
     * Get the Song representation of a music file.
     * @param file  the file to read.
     * @return Song based on file.
     * @author Andrey
     */
    public static Song extractSongFromFile(final File file){
        Song song=null;
        try{
            retriever.setDataSource(file.getPath());

            song=new Song() {
                {
                    setTitle(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                    setArtist(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                    setAlbumArtist(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
                    setAlbum(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                    setGenre(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                    setLocal(true);
                    String track=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
                    if(track!=null){
                        int seperator=track.indexOf('/');
                        if(seperator>0)
                            track=track.substring(0,seperator);
                    }
                    String year=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
                    String duration=   retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    if (track!=null) setTrack(Integer.parseInt(track.trim()));
                    if (year!=null) setYear(Integer.parseInt(year.trim()));
                    if (duration!=null) setDurationMillis(Long.parseLong(duration.trim()));
                    setUrl(file.toURI().toURL().toString());
                }
            };
        }catch(Exception e){
            debugFiles("Problem extracting song from file: "+e.getMessage());
        }
        if(song==null || song.getTitle()==null || song.getArtist()==null || song.getGenre()==null)      {
            if(song!=null)
                debugFiles("Song "+song+" "+song.getTitle()+" "+song.getArtist()+" "+song.getGenre());
            return null;
        }
        return song;
    }
    private static HashMap<String, String> getGenreIDMap(Context activity){
        HashMap<String, String> genreIDMap = new HashMap<String, String>();
        debug("shit's good");
        String[] proj = {MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME};
        Uri uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;//uri.parse("content://com.google.android.music.MusicContent/playlists");
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor==null){
            debug("GenreCursor is null!");
            return null;
        } else{
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    genreIDMap.put(cursor.getString(0), cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        debug("Genre shit's fucking good");
        cursor.close();
        return genreIDMap;
    }

    public static HashMap<String, Integer> getGenreCounts(Context context){
        HashMap<String, Integer> genreCounts = new HashMap<String, Integer>();
        debug("shit's good");
        String[] proj = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
        Uri uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor==null){
            debug("GenreCursor is null!");
            return null;
        } else {
            //debug("Count: "+cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    genreCounts.put(cursor.getString(0), getGenreSongCount(context, Integer.parseInt(cursor.getString(1))));
                } while (cursor.moveToNext());
            }
        }
        debug("Genre shit's fucking good");
        cursor.close();
        return genreCounts;
    }

    private static int getGenreSongCount(Context context, long genreID) {
        Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreID);
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);

        if (c == null || c.getCount() == 0)
            return -1;

        int num = c.getCount();
        c.close();

        return num;
    }

    private static void queryAndPrintArtists(Context activity){
        debug("shit's good");
        String[] proj = {MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;//uri.parse("content://com.google.android.music.MusicContent/playlists");
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor==null){
            debug("ArtistCursor is null!");
            return;
        } else{
            debug("Artist count: " + cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    debug("Artist: " + cursor.getString(0));
                    debug(".       Number of tracks(?):" + cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        debug("Artist shit's fucking good");
    }

    private static void queryAndPrintAlbums(Context activity){
        debug("shit's good");
        String[] proj = {MediaStore.Audio.Albums.ALBUM};
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;//uri.parse("content://com.google.android.music.MusicContent/playlists");
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor==null){
            debug("AlbumCursor is null!");
            return;
        } else{
            debug("Count: " + cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    debug("Album: " + cursor.getString(0));
                    //debug(".       Number of tracks(?):"+cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        debug("Album shit's fucking good");
    }

    private static void queryAndPrintTest(Context activity){
        debug("shit's good");
        String[] proj = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM};
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;//uri.parse("content://com.google.android.music.MusicContent/playlists");
        Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (cursor==null){
            debug("Derp is null!");
            return;
        } else{
            debug("Count: " + cursor.getCount());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    debug("Song: " + cursor.getString(0));
                    debug(".     Artist: " + cursor.getString(1));
                    debug(".     Album: " + cursor.getString(2));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        debug("Derp shit's fucking good");
    }


    private static void debug(String tag, Object o, boolean e){
        if (debug) if(e) Log.e(tag, ""+o); else Log.d(tag,""+o);
    }
    private static void debug(String tag, Object o){
        debug(tag,o,false);
    }


    private static void debug(Object o){
        if (debug) Log.e("Derp", ""+o);
    }

    private static void debugFiles(Object o){
        if (debugFiles) Log.e("Derp", ""+o);
    }
}
