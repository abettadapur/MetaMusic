package org.thingswithworth.metamusic.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.*;
import org.thingswithworth.metamusic.Callback;
import org.thingswithworth.metamusic.MusicApplication;
import org.thingswithworth.metamusic.R;
import org.thingswithworth.metamusic.activities.adapters.AlbumListAdapter;
import org.thingswithworth.metamusic.activities.factories.GradientBitmapMaker;
import org.thingswithworth.metamusic.model.Album;
import org.thingswithworth.metamusic.model.Artist;
import org.thingswithworth.metamusic.net.LastFMQuery;
import org.thingswithworth.metamusic.net.NetImageTask;

/**
 * This activity displays info about an artist in the collection of the user. It also displays their albums and allows those to be played.
 * @author: Andrey Kurenkov
 * //TODO: generate generic art for artists/album with no art or no wifi (probably)
 */
public class ArtistActivity extends AppActivity {
    private String artistName;
    private Artist artist;
    private LinearLayout imageLayout;
    private ImageView image;
    private TextView bioView;
    private ExpandableListView albumList;
    private boolean  showedToast;
    /**
     * Gives values to instance variables and sets up view of Activity.
     * Also shows Toast with hint on how to use. The intent stores the artist name.
     * @param savedInstanceState not used
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        artistName = getIntent().getStringExtra("artist");
        artist= MusicApplication.getGlobalCollection().getArtist(artistName);

        setContentView(R.layout.artist_activity);
        image = (ImageView)findViewById(R.id.listArtistImageView);
        bioView = (TextView)findViewById(R.id.bioView);
        bioView.setMovementMethod(new ScrollingMovementMethod());
        albumList=(ExpandableListView)findViewById(R.id.artistAlbumList);
        imageLayout=(LinearLayout)findViewById(R.id.imageLayout);

        TextView name= (TextView)findViewById(R.id.artistName);
        name.setText(artistName);

        TextView header=new TextView(this);
        header.setText("Albums");
        header.setGravity(Gravity.CENTER);
        header.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
        albumList.setHeaderDividersEnabled(true);
        albumList.addHeaderView(header);

        //sets the adapter that provides data to the list.
        albumList.setAdapter(new AlbumListAdapter(this,artist.getAlbums()));

        if(!showedToast){
            showedToast=true;
            Toast.makeText(this, "Click on any artist for more information and albums.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Static method to have a standard interface for other activities to launch this
     * @param context where an intent is launched from
     * @param artist the artist for which to launch this
     */
    public static void launchActivityFor(Context context,String artist) {
        Intent intent = new Intent(context, ArtistActivity.class);
        intent.putExtra("artist", artist);
        context.startActivity(intent);
    }

    /**
     * Loads in values for artist bio and images.
     * @see android.app.Activity#onStart() ()
     */
    @Override
    public void onStart()
    {
        super.onStart();
        if(artist.getSummary()==null || artist.getImgLarge()==null)
            loadArtist();
        else
            populateView();
    }

    /**
     * Start a LoadArtistTask, which finds info if possible and update text and images.
     */
    private void loadArtist()
    {
        new LoadArtistTask().execute(artistName);
    }

    /**
     * Finds images for albums if not cached, else just invalidates list.
     */
    private void drawAlbums()
    {
        for(final Album album:artist.getAlbums()){
            if(album.getCachedBitmapLarge()==null){
                Callback.ParamCallback<Bitmap> callback=new Callback.ParamCallback<Bitmap>() {
                    @Override
                    public void onCompletion(Bitmap... param) {
                        album.setCachedBitmapLarge(param[0]);
                    }
                };
                new NetImageTask(callback).execute(album.getImgLarge());
            }
        }
        albumList.invalidate();
    }

    /**
     * Handles populating all the views within the activity aside from the list. Accounts for cached images and handles gradient generation.
     */
    private void populateView()
    {
        String summary=artist.getSummary();
        if(summary!=null){
            bioView.setText(summary);
            if(artist.getCachedBitmapLarge()==null){
                Callback.ParamCallback<Bitmap> callback=new Callback.ParamCallback<Bitmap>() {
                    @Override
                    public void onCompletion(Bitmap... param) {
                        artist.setCachedBitmapLarge(param[0]);
                        drawArtistBitmap();
                    }
                };
                new NetImageTask(callback).execute(artist.getImgLarge());
            }
            else
                drawArtistBitmap();

        }else if(!MusicApplication.isConnectedToWifi()){
            bioView.setText("No internet connection - unable to retrieve data.");
            imageLayout.setBackground(new ColorDrawable(Color.BLACK));
            image.setImageResource(R.drawable.nointernet);
        }else{
            bioView.setText("Unable to find information.");
            imageLayout.setBackground(new ColorDrawable(Color.BLACK));
            image.setImageDrawable(null);
            image.setMinimumHeight(0);
            image.setMinimumWidth(0);
            image.setMaxHeight(0);
            image.setMaxWidth(0);
        }
    }

    /**
     * Simple async call to LastFMQuery.getArtistMeta that calls populateView on completion.
     * @see org.thingswithworth.metamusic.net.LastFMQuery#getArtistMeta(String)
     */
    private class LoadArtistTask extends AsyncTask<String, Void, Artist>
    {

        @Override
        protected Artist doInBackground(String... strings)
        {
            return LastFMQuery.getArtistMeta(artist);
        }
        protected void onPostExecute(Artist result)
        {
            populateView();
        }
    }

    /**
     * Simple async call to LastFMQuery.getAlbumMeta that calls drawAlbums on completion.
     * @see org.thingswithworth.metamusic.net.LastFMQuery#getAlbumtMeta(Album)
     */
    private class LoadAlbumsTask extends AsyncTask<Artist, Void, Artist>
    {

        @Override
        protected Artist doInBackground(Artist... artist)
        {
            for(Album album:artist[0].getAlbums()){
                if(album.getImgLarge()==null)
                    LastFMQuery.getAlbumtMeta(album);
            }
            return null;
        }
        protected void onPostExecute(Artist result)
        {
            drawAlbums();
        }
    }

    /**
     * Draws the stored bitmap in an Artist, and generates gradient if necessary.
     */
    private void drawArtistBitmap(){
        Bitmap bmp=artist.getCachedBitmapLarge();
        Callback.ParamCallback<Bitmap> callback=new Callback.ParamCallback<Bitmap>() {
            @Override
            public void onCompletion(Bitmap... param) {
                artist.setCachedBitmapLarge(param[0]);
                artist.setCachecBitmapBackground(param[1]);
            }
        };
        GradientBitmapMaker maker=new GradientBitmapMaker(callback,image, bmp, imageLayout, ArtistActivity.this);
        maker.execute(bmp,artist.getCachecBitmapBackground());
    }



}
