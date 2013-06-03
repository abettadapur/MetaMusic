package org.thingswithworth.metamusic.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import org.thingswithworth.metamusic.Callback;

import java.io.IOException;
import java.net.URL;

public class NetImageTask extends AsyncTask<URL, Void, Bitmap>
{
    private ImageView setTo;
    private Bitmap bmp;
    private Callback.ParamCallback<Bitmap> callback;


    public NetImageTask(){
        this(null,null);
    }

    public NetImageTask(Callback.ParamCallback<Bitmap> callback){
        this(null,callback);
    }

    public NetImageTask(ImageView view, Callback.ParamCallback<Bitmap> callback){
        setTo=view;
        this.callback=callback;
    }

    public Bitmap getBitmap(){
        return bmp;
    }
    @Override
    protected Bitmap doInBackground(URL... urls)
    {
        try
        {
            bmp = BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            return bmp;
        }
        catch(IOException iex)
        {
            Log.e("Error", "Error loading image");
        }
        return null;
    }
    protected void onPostExecute(Bitmap bmp)
    {
        if(setTo!=null)
            setTo.setImageBitmap(bmp);
        if(callback!=null)
            callback.onCompletion(bmp);
    }
}