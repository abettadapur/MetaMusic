package org.thingswithworth.metamusic.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.view.Window;
import org.thingswithworth.metamusic.MusicApplication;
import org.thingswithworth.metamusic.R;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 6/3/13
 * Time: 9:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class SplashActivity extends Activity {
    boolean loadingFinished;
    long startTime;
    private final int STOPSPLASH=0;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        startTime = Calendar.getInstance().getTimeInMillis();
        new LocalMusicAsync().execute(new Void[0]);

    }
    private class LocalMusicAsync extends AsyncTask<Void, Void, Void>
    {

        protected Void doInBackground(Void...  voids)
        {
            MusicApplication.loadCollection();
            return null;
        }
        protected void onPostExecute(Void result)
        {
            long endTime = Calendar.getInstance().getTimeInMillis();
            if(endTime-startTime<5000)
            {
                Message msg = new Message();
                msg.what=STOPSPLASH;
                splashHandler.sendMessageDelayed(msg, 5000-(endTime-startTime));
            }
            else
            {
               Message msg = new Message();
               msg.what = STOPSPLASH;
               splashHandler.sendMessage(msg);
            }
        }


    }
    private Handler splashHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case STOPSPLASH:
                    Intent intent = new Intent(SplashActivity.this, BrowseChartsActivity.class );
                    startActivity(intent);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}