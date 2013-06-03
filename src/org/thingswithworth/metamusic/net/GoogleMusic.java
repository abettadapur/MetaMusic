package org.thingswithworth.metamusic.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.android.gm.api.GoogleMusicApi;
import com.android.gm.api.model.Song;
import org.thingswithworth.metamusic.Callback;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: andrey
 * Date: 4/19/13
 * Time: 7:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMusic {
    private static final boolean DEBUG=false;
    private static String errorLog;
    private static boolean error;
    private static boolean asyncComplete;
    private static ArrayList<Song> songsLastRetrieved;
    private static AsyncTask getSongTask;

    /**
     *  This starts an async task to retrieve songs. isLastTaskCompleted() should be used to verify retrieval worked, and getSongsLastRetrieved should return non-null arraylist.
     *
     */
    public static void retrieveAllSongs(final Context context, final String username,final String password, final Callback onComplete){
        asyncComplete=false;
       getSongTask =new AsyncTask<Void, Void, ArrayList<Song>>() {

            @Override
            protected  ArrayList<Song> doInBackground(Void... params) {
                try{
                    debug("Async","Starting async song retrieve");
                    error=false;
                    GoogleMusicApi.createInstance(context);
                    debug("Async","async logged in");
                    boolean loggedIn=GoogleMusicApi.login(context,username,password);
                    if(!loggedIn)
                        throw new IllegalArgumentException("This login information is invalid");
                    debug("Async","Querying");
                    ArrayList<Song> songs=GoogleMusicApi.getAllSongs(context);
                    debug("Async","Async Done");
                    return songs;
                }catch(JSONException exception){
                    error=true;
                    errorLog=exception.getMessage();
                }catch(IllegalArgumentException exception){
                    error=true;
                    errorLog="The input login information is invalid.";
                }catch(NullPointerException nuex)
                {
                    error=true;
                    errorLog = "We could not find any google music songs on this account";
                }

                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Song> songs) {
                songsLastRetrieved=songs;
                asyncComplete=true;

                onComplete.onCompletion();
                debug("Async","Async complete");
            }


        };
        getSongTask.execute(new Void[0]);
    }

    public static boolean getHadError(){
        return error;
    }

    public static String getErrorLog(){
        return errorLog;
    }

    public static boolean isLastTaskCompleted(){
        return asyncComplete;
    }

    public static ArrayList<Song> getSongsLastRetrieved(){
        return songsLastRetrieved;
    }

    private static void debug(String key, String content){
        if(DEBUG)
            Log.d(key,content);
    }

    public static void stopQuery()
    {
        getSongTask.cancel(true);
    }
}
