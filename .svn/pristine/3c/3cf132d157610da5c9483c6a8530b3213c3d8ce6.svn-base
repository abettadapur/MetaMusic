package org.thingswithworth.metamusic.fileio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.thingswithworth.metamusic.Callback;

/**
 * @author: andrey
 */
public class LocalImageTask extends AsyncTask<String, Void, Bitmap>
{
    private ImageView setTo;
    private Bitmap bmp;
    private Callback.ParamCallback<Bitmap> callback;

    public LocalImageTask(){
        this(null,null);
    }

    public LocalImageTask(Callback.ParamCallback<Bitmap> callback){
        this(null,callback);
    }

    public LocalImageTask(ImageView view, Callback.ParamCallback<Bitmap> callback){
        setTo=view;
        this.callback=callback;
    }

    public Bitmap getBitmap(){
        return bmp;
    }

    @Override
    protected Bitmap doInBackground(String... paths)
    {
        bmp= BitmapFactory.decodeFile(paths[0]);
        return bmp;
    }
    protected void onPostExecute(Bitmap bmp)
    {
        if(setTo!=null)
            setTo.setImageBitmap(bmp);
        if(callback!=null)
            callback.onCompletion(bmp);
    }
}

