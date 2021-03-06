package org.thingswithworth.metamusic;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import org.thingswithworth.metamusic.activities.factories.DialogFactory;
import org.thingswithworth.metamusic.fileio.LocalMedia;
import org.thingswithworth.metamusic.media.MusicPlayer;
import org.thingswithworth.metamusic.model.MusicCollection;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/19/13
 * Time: 11:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MusicApplication extends Application
{
    private static MusicCollection appRuntimeCollection;
    private static Context context;
    public static SharedPreferences preferences;
    @Override
    public void onCreate()
    {
        super.onCreate();
        preferences=PreferenceManager.getDefaultSharedPreferences(this);
        context = getApplicationContext();
        //TODO: create dummy Actiivty for start, make it display a progress dialog for this (for large music collection)

    }
    public static void loadCollection()
    {
        appRuntimeCollection=new MusicCollection();
        appRuntimeCollection.addSongs(LocalMedia.getLocalSongs(context));
        appRuntimeCollection.cleanGenreCount();
    }

    public static MusicCollection getGlobalCollection(){
        return appRuntimeCollection;
    }

    public static Context getAppContext()
    {
       return MusicApplication.context;
    }

    public static boolean isConnectedToWifi(){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();
    }

    @Override
    public void onTerminate(){
        MusicPlayer.getPlayer().cleanup();
    }

}
