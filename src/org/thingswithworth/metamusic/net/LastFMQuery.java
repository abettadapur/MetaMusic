package org.thingswithworth.metamusic.net;

import android.util.Log;
import org.thingswithworth.metamusic.model.Album;
import org.thingswithworth.metamusic.model.Artist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class providing scraping functionality for Last.fm
 *
 * @author Alexander Ikenomodis
 */
public class LastFMQuery {
    private static boolean DEBUG =false;

    public static Artist getArtistMeta(String ArtistName){
        debug("LASTFM", "Starting request...");
        URL url , small = null, medium = null, large = null;
        String bio=null;
        try {
            url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist="+ArtistName.replaceAll(" ", "%20")+"&api_key=08dd2c001a8176908e4feb6bd51391a1&format=json");
            HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String[] lines = in.readLine().split("#text");
            for (String line: lines) {
                if (line.contains("small") && small == null){
                    small = new URL(line.substring(line.indexOf("http:"), line.indexOf("\",\"size")).replaceAll("\\\\", ""));
                    debug("Response", "Found small at "+small.toString());
                }
                if (line.contains("medium") && medium == null){
                    medium = new URL(line.substring(line.indexOf("http:"), line.indexOf("\",\"size")).replaceAll("\\\\", ""));
                    debug("Response", "Found medium at "+medium.toString());
                }
                if (line.contains("large") && large == null){
                    large = new URL(line.substring(line.indexOf("http:"), line.indexOf("\",\"size")).replaceAll("\\\\", ""));
                    debug("Response", "Found large at "+large.toString());
                }
                if (line.contains("summary\":") && line.contains("\\n\\n")){
                    bio = line.substring(line.indexOf("summary\":")+12, line.indexOf("\\n\\n")).trim();
                    bio = bio.replaceAll("<a .*?>", "");
                    bio = bio.replaceAll("<\\\\/a>", "");
                    bio = bio.replaceAll("&quot;","\"") ;
                    bio = bio.replaceAll("&amp;","&");
                    bio = bio.replaceAll("\\\\/","/");
                    bio = bio.replaceAll("\\\\u2019","\'");
                    bio = bio.replaceAll("\\\\u201d","\"");
                    bio = bio.replaceAll("\\\\u201c","\"");
                    bio = bio.replaceAll("&lt;","<");
                    bio = bio.replaceAll("&gt;",">");
                    debug("Response", "Found bio: "+bio);
                }
                //DEBUG("Response", line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        // Making HTTP Request
        debug("LASTFM", "Done.");
        return new Artist(ArtistName,bio,small,medium,large);
    }


    public static Album getAlbumtMeta(Album album){
        debug("LASTFM", "Starting request...");
        URL url , small = null, medium = null, large = null;
        String bio;
        try {
            url = new URL("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&"+
                   "artist="+album.getName().replaceAll(" ", "%20")+
                    "album="+album.getArtist().getName().replaceAll(" ", "%20")+
                    "&api_key=08dd2c001a8176908e4feb6bd51391a1&format=json");
            HttpURLConnection urlConnection =(HttpURLConnection)url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String[] lines = in.readLine().split("#text");
            for (String line: lines) {
                if (line.contains("small") && small == null){
                    small = new URL(line.substring(line.indexOf("http:"), line.indexOf("\",\"size")).replaceAll("\\\\", ""));
                    debug("Response", "Found small at "+small.toString());
                }
                if (line.contains("medium") && medium == null){
                    medium = new URL(line.substring(line.indexOf("http:"), line.indexOf("\",\"size")).replaceAll("\\\\", ""));
                    debug("Response", "Found medium at "+medium.toString());
                }
                if (line.contains("large") && large == null){
                    large = new URL(line.substring(line.indexOf("http:"), line.indexOf("\",\"size")).replaceAll("\\\\", ""));
                    debug("Response", "Found large at "+large.toString());
                }
                if (line.contains("summary\":") && line.contains("\\n\\n")){
                    bio = line.substring(line.indexOf("summary\":")+12, line.indexOf("\\n\\n")).trim();
                    bio = bio.replaceAll("<a .*?>", "");
                    bio = bio.replaceAll("<\\\\/a>", "");
                    bio = bio.replaceAll("&quot;","\"") ;
                    bio = bio.replaceAll("&amp;","&");
                    bio = bio.replaceAll("\\\\/","/");
                    bio = bio.replaceAll("\\\\u2019","\'");
                    bio = bio.replaceAll("\\\\u201d","\"");
                    bio = bio.replaceAll("\\\\u201c","\"");
                    bio = bio.replaceAll("&lt;","<");
                    bio = bio.replaceAll("&gt;",">");
                    debug("Response", "Found bio: "+bio);
                }
                //DEBUG("Response", line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        // Making HTTP Request
        debug("LASTFM", "Done.");
        album.setImgLarge(large);
        album.setImgMedium(medium);
        album.setImgSmall(small);
        return album;
    }
    public static Artist getArtistMeta(Artist artist){
        Artist metaInfo= getArtistMeta(artist.getName());
        artist.setSummary(metaInfo.getSummary());
        artist.setImgSmall(metaInfo.getImgSmall());
        artist.setImgMedium(metaInfo.getImgMedium());
        artist.setImgLarge(metaInfo.getImgLarge());
        return artist;
    }

    private static void debug(String tag,String content){
        if(DEBUG)
            Log.d(tag,content);
    }
}
