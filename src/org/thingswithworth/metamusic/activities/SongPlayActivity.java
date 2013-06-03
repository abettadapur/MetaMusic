package org.thingswithworth.metamusic.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.thingswithworth.metamusic.Callback;
import org.thingswithworth.metamusic.MusicApplication;
import org.thingswithworth.metamusic.R;
import org.thingswithworth.metamusic.activities.adapters.PlaylistSongsAdapter;
import org.thingswithworth.metamusic.activities.factories.GradientBitmapMaker;
import org.thingswithworth.metamusic.fileio.LocalImageTask;
import org.thingswithworth.metamusic.media.MusicPlayer;
import org.thingswithworth.metamusic.model.Album;
import org.thingswithworth.metamusic.net.NetImageTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Activity for the song player of the app.
 * @author Andrey Kurenkov, Alex Bettadapur
 */
public class SongPlayActivity extends Activity{
    private Timer timer;
    private TextView titleView, albumView, artistView;
    private SeekBar seekBar;
    private MusicPlayer player;
    private ImageButton prev,playPause,next;
    private LinearLayout imageLayout;
    private ImageView image;
    private PlaylistSongsAdapter adapter;
    /**
     * Gives values to instance variables and sets up view of Activity.
     * Sets up timer and list adapter, sets the context of the Music Player, other setup.
     * @param savedInstanceState not used
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_play_activity);
        image = (ImageView)findViewById(R.id.listArtistImageView);
        imageLayout=(LinearLayout) findViewById(R.id.playingImageBack);
        timer = new Timer();
        timer.schedule(new ProgressTask(), 0l, 500l);
        titleView=(TextView)findViewById(R.id.titleView);
        albumView=(TextView)findViewById(R.id.albumView);
        artistView=(TextView)findViewById(R.id.artistView);
        seekBar = (SeekBar)findViewById(R.id.musicProgress);
        playPause=((ImageButton)findViewById(R.id.playlistPlayButton));
        player=MusicPlayer.getPlayer();
        seekBar.setOnSeekBarChangeListener(player);

        updateSongView();

        ListView songList=(ListView)     findViewById(R.id.PlaylistView);
        ImageView emptyView=new ImageView(this);
        emptyView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        songList.setEmptyView(emptyView);
        adapter=new PlaylistSongsAdapter(this,player);

        songList.setAdapter(adapter);
        timer = new Timer();
        timer.schedule(new ProgressTask(), 0l, 1000);
        player.setContext(this);
    }

    /**
     * Just sets context of MusicPlayer to null.
     */
    @Override
    public void onStop(){
        super.onStop();
        player.setContext(null);
    }

    /**
     * Updates multiple aspects of view to correspond to the currently playing song (updating the shown image, progress and more).
     * Sends notification about currently playing song.
     */
    public void updateSongView(){
        seekBar.setMax(player.getDuration());
        int currentPosition=player.getCurrentPosition();
        seekBar.setProgress(currentPosition);

        if(player.isPlaying()) {
            playPause.setImageResource(android.R.drawable.ic_media_pause);
        }
        else{
            playPause.setImageResource(android.R.drawable.ic_media_play);
        }

        MusicPlayer.PlaylistSong playing=player.getNowPlaying();
        if(playing==null){
            titleView.setText("No songs in queue");
            artistView.setText("No artist");
            albumView.setText("No album");
            imageLayout.setBackground(null);
            image.setImageDrawable(null);
            image.setMinimumHeight(0);
            image.setMinimumWidth(0);
        } else if(!playing.song.getName().equals(titleView.getText().toString())){
            titleView.setText(playing.song.getName());
            artistView.setText(playing.artist.getName());
            albumView.setText(playing.album.getName());
            drawAlbumArt();

            Intent intent = new Intent(MusicApplication.getAppContext(), SongPlayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            PendingIntent pendingIntent = PendingIntent.getActivity(MusicApplication.getAppContext(),0,intent,0);
            Notification nowPlaying = new Notification.Builder(MusicApplication.getAppContext())
                    .setContentTitle(playing.song.getTitle())
                    .setContentText(playing.song.getArtist())
                    .setSmallIcon(android.R.drawable.ic_media_play)
                    .setLargeIcon(playing.album.getCachedArt())
                    .addAction(R.drawable.mmicon, "Go To Player", pendingIntent)
                    .setOngoing(true)
                    .build();
            NotificationManager manager = (NotificationManager) MusicApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0,nowPlaying);

        }
    }

    /**
     * Draws the album art of the album, handles finding it locally or online. Has helper method for doing gradient.
     */
    private void drawAlbumArt(){
        MusicPlayer.PlaylistSong playing=player.getNowPlaying();
        Bitmap bitmap= playing.album.getCachedArt();

        if(bitmap!=null){
            drawAlbumBitmap(bitmap);
        }   else{
            String image = player.getNowPlaying().song.getAlbumArtUrl();
            if(image!=null){
                Callback.ParamCallback<Bitmap> call=new Callback.ParamCallback<Bitmap>() {

                    @Override
                    public void onCompletion(Bitmap... param) {
                        drawAlbumBitmap(param[0]);
                    }
                };
                if(playing.song.isLocal()){
                    new LocalImageTask(call).execute(image);
                }   else{
                    image=image.replace("//","http://");
                    try
                    {
                        URL url = new URL(image);
                        new NetImageTask(call).execute(url);
                    }
                    catch(MalformedURLException muex)
                    {
                        Log.e(this.getClass().getName(),"Malformed URL");
                    }
                }
            }
        }
    }

    /**
     * Additional method for drawing of album art that handles drawing gradient
     * @param bmp the bitmap for the album
     */
    private void drawAlbumBitmap(Bitmap bmp){
        final Album album=player.getNowPlaying().album;
        if(!album.isFindingArt()){
            Callback.ParamCallback<Bitmap> callback=new Callback.ParamCallback<Bitmap>() {
                @Override
                public void onCompletion(Bitmap... param) {
                    album.setCachedArt(param[0]);
                    album.setCachedArtBackground(param[1]);
                    album.setFindingArt(false);
                }
            };
            album.setFindingArt(true);
            GradientBitmapMaker maker=new GradientBitmapMaker(callback,image, bmp, imageLayout, SongPlayActivity.this);
            maker.execute(bmp, album.getCachedArtBackground());
        }
    }

    /**
     * OnClickListener for the play.pause button. Updates icon and MusicPlayer state.
     * @param view not used
     */
    public void playPause(View view)
    {
        if(player.isPlaying()) {
            player.pause();
            playPause.setImageResource(android.R.drawable.ic_media_play);
        }
        else if(player.hasSongs()){
            player.play();
            playPause.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    /**
     * OnClickListener for next button.
     * @param view not used
     */
    public void playNext(View view){
        boolean worked=player.playNext();
        if(worked){
            adapter.notifyDataSetChanged();
            updateSongView();
        }
    }

    /**
     * OnClickListener for previous button.
     * @param view not used
     */
    public void playPrev(View view){
        boolean worked=player.playPrev();
        if(worked){
            adapter.notifyDataSetChanged();
            updateSongView();
        }
    }

    /**
     * Times task to periodically update the view with current progress and other info.
     */
    private  class ProgressTask extends TimerTask
    {
        @Override
        public void run() {
            SongPlayActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run()
                {
                    updateSongView();
                }
            });
        }
    }

    /**
     * Cancels the update timer and finished activity;
     */
    @Override
    public void onBackPressed()
    {
        timer.cancel();
        this.finish();
    }

}
