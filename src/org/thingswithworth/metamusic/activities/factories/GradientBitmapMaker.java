package org.thingswithworth.metamusic.activities.factories;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.thingswithworth.metamusic.Callback;

/**
 * This AsyncTask creates the stylized gradient for Album and Artists bitmaps in the app.
 * @author: Andrey Kurenkov
 */
public class GradientBitmapMaker extends AsyncTask<Bitmap, Bitmap, Bitmap>
{
    private Activity activity;
    private ImageView image;
    private int windowHeight,windowWidth, width, height;
    private LinearLayout imageLayout;
    private Callback.ParamCallback<Bitmap> callback;
    private Bitmap bmp, map;
    private boolean horizontal;
    /**
     * Constructor to set all the needed instance data. It decides to create horizontally if the height is less than the width (which is faster for horizontal gradient creation).
     * @param callback callback with the completed images. The processed past in bitmap will be the first Bitmap in onCompletion, and the new background bitmap will be the second.
     * @param image the ImageView within which the bitmap is shown
     * @param bmp The image for which to do the gradient and gradient background
     * @param imageLayout LinearLayour inside of which to show the background gradient
     * @param activity activity for which the gradient is being created (used for sizing images)
     */
    public GradientBitmapMaker(Callback.ParamCallback<Bitmap> callback,ImageView image, Bitmap bmp, LinearLayout imageLayout,Activity activity) {
        this(callback, image, bmp, bmp.getHeight()<=bmp.getWidth(),imageLayout, activity);
    }

    /**
     * Constructor to set all the needed instance data.
     * @param callback callback with the completed images. The processed past in bitmap will be the first Bitmap in onCompletion, and the new background bitmap will be the second.
     * @param image the ImageView within which the bitmap is shown
     * @param bmp The image for which to do the gradient and gradient background
     * @param horizontal whether to create a horizontal or vertical gradeint (only matters in the animation, result is same)
     * @param imageLayout LinearLayour inside of which to show the background gradient
     * @param activity activity for which the gradient is being created (used for sizing images)
     */
    public GradientBitmapMaker(Callback.ParamCallback<Bitmap> callback,ImageView image, Bitmap bmp, boolean horizontal, LinearLayout imageLayout,Activity activity) {
        this.activity=activity;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        windowWidth= dm.widthPixels;
        windowHeight=dm.heightPixels;
        this.horizontal=horizontal;

        int orientation=activity.getResources().getConfiguration().orientation;
        boolean portrait=orientation== Configuration.ORIENTATION_PORTRAIT;

        double wVsH=(double)bmp.getWidth()/bmp.getHeight();
        if(portrait){
            height = (int)(windowHeight*3/10.0);
            width= (int)(height*wVsH);

        }else{
            height = (int)(windowHeight*3/10.0);
            width= (int)(height*wVsH);
        }
        image.setMinimumWidth(width);
        image.setMinimumHeight(height);
        image.setMaxHeight(height);
        image.setMaxWidth(width);

        this.callback = callback;
        this.image = image;
        this.imageLayout = imageLayout;
    }

    /**
     * Generates the bitmaps. The bitmap for which to create a gradient needs to be the first parameter, and a second parameter of the gradient background can also be passed in.
     * If the second parameter is passed in and is not null, the images are merely scaled and gradients are not recomputed.
     * @param bitmaps The image bitmap as the first parameter, and optionally the background bitmap as the second
     * @return the final background bitmap
     */
    protected Bitmap doInBackground(Bitmap... bitmaps) {
        bmp=bitmaps[0];
        if(bitmaps.length>0)
            map=bitmaps[1];
        if(!bmp.isMutable())
            bmp=bmp.copy(Bitmap.Config.ARGB_8888,true);
        if(bmp!=null){
            if(map==null){
                map=Bitmap.createBitmap(windowWidth,height, Bitmap.Config.RGB_565);
                if(horizontal)
                    doHorizontalGradient();
                else
                    doVerticalGradient();
            }
        }
        return map;
    }

    private void doHorizontalGradient(){
        int halfWidth=windowWidth/2;
        double scale=(double)height/bmp.getHeight();
        int skip=0;
        for(int w=0;w<=bmp.getWidth()/2;w++){
            for(int h=0;h<bmp.getHeight();h++){
                double ratio=(1-(Math.pow(bmp.getHeight()/2-h,4))/(Math.pow(bmp.getHeight()/2,4)));
                for(int mult=-1;mult<=1;mult+=2){
                    int atW=bmp.getWidth()/2+mult*w;
                    if(atW<bmp.getWidth() && atW>=0){
                        int color=bmp.getPixel(atW,h);
                        int[] rgb=new int[]{Color.red(color),Color.green(color),Color.blue(color)};
                        color=Color.rgb((int)Math.round(rgb[0]*ratio),(int)Math.round(rgb[1]*ratio),(int)Math.round(rgb[2]*ratio));
                        bmp.setPixel(atW,h,color);
                    }
                }
            }
            if(skip%4==0)
                publishProgress(bmp,map);
            skip++;
        }
        for(int w=0;w<halfWidth;w++){
            for(int h=0;h<bmp.getHeight();h++){
                for(int mapH=(int)Math.round(scale*h);mapH<scale*h+scale && mapH<map.getHeight();mapH++){
                    int colorLeft=bmp.getPixel(0,h);
                    int colorRight=bmp.getPixel(bmp.getWidth()-1,h);
                    double ratio=(double)(halfWidth-w)/(halfWidth);
                    //ratio=ratio*(1-(Math.pow(halfHeight-mapH,4))/(Math.pow(halfHeight,4)));
                    int[] rgb=new int[]{Color.red(colorLeft),Color.green(colorLeft),Color.blue(colorLeft)};
                    int color=Color.rgb((int)Math.round(rgb[0]*ratio),(int)Math.round(rgb[1]*ratio),(int)Math.round(rgb[2]*ratio));
                    map.setPixel(map.getWidth()/2-w,mapH,color);

                    //ratio=ratio*(1-(Math.pow(halfHeight-mapH,4))/(Math.pow(halfHeight,4)));
                    rgb=new int[]{Color.red(colorRight),Color.green(colorRight),Color.blue(colorRight)};
                    color=Color.rgb((int)Math.round(rgb[0]*ratio),(int)Math.round(rgb[1]*ratio),(int)Math.round(rgb[2]*ratio));
                    map.setPixel(map.getWidth()/2+w,mapH,color);
                }
            }
        }
    }

    private void doVerticalGradient(){
        int halfWidth=windowWidth/2;
        double scale=(double)height/bmp.getHeight();
        int skip=0;
        for(int h=0;h<bmp.getHeight();h++){
            //ratio=ratio*(1-(Math.pow(halfHeight-mapH,4))/(Math.pow(halfHeight,4)));
            for(int w=0;w<bmp.getWidth();w++){
                double ratio=(1-(Math.pow(bmp.getHeight()/2-h,4))/(Math.pow(bmp.getHeight()/2,4)));
                int color=bmp.getPixel(w,h);
                int[] rgb=new int[]{Color.red(color),Color.green(color),Color.blue(color)};
                color=Color.rgb((int)Math.round(rgb[0]*ratio),(int)Math.round(rgb[1]*ratio),(int)Math.round(rgb[2]*ratio));
                bmp.setPixel(w,h,color);
            }
            int colorLeft=bmp.getPixel(0,h);
            int colorRight=bmp.getPixel(bmp.getWidth()-1,h);
            for(int mapH=(int)Math.round(scale*h);mapH<scale*h+scale && mapH<map.getHeight();mapH++){
                for(int w=0;w<halfWidth;w++){
                    double ratio=(double)w/(halfWidth);
                    //ratio=ratio*(1-(Math.pow(halfHeight-mapH,4))/(Math.pow(halfHeight,4)));
                    int[] rgb=new int[]{Color.red(colorLeft),Color.green(colorLeft),Color.blue(colorLeft)};
                    int color=Color.rgb((int)Math.round(rgb[0]*ratio),(int)Math.round(rgb[1]*ratio),(int)Math.round(rgb[2]*ratio));
                    map.setPixel(w,mapH,color);
                }
                for(int w=halfWidth;w<windowWidth;w++){
                    double ratio=(double)(map.getWidth()-w)/halfWidth;
                    //ratio=ratio*(1-(Math.pow(halfHeight-mapH,4))/(Math.pow(halfHeight,4)));
                    int[] rgb=new int[]{Color.red(colorRight),Color.green(colorRight),Color.blue(colorRight)};
                    int color=Color.rgb((int)Math.round(rgb[0]*ratio),(int)Math.round(rgb[1]*ratio),(int)Math.round(rgb[2]*ratio));
                    map.setPixel(w,mapH,color);
                }
            }
            if(skip%4==0)
                publishProgress(bmp,map);
            skip++;
        }
    }
    boolean once;

    /**
     * Updates the image and background with the current bitmaps
     * @param bitmaps image and background bitmap
     */
    protected void onProgressUpdate (Bitmap... bitmaps){
        if(!once){
            once=true;
            Bitmap bmp=bitmaps[0];
            if(map.getHeight()!=height || map.getWidth()!=windowWidth)
                imageLayout.setBackground(new BitmapDrawable(activity.getResources(),Bitmap.createScaledBitmap(map, windowWidth, height, false)));
            if(bmp!=null)
                image.setImageBitmap(bmp);
        }
        else{
            image.invalidate();
        }
    }

    /**
     * Draws final gradient images
     * @param map  the background bitmap
     */
    protected void onPostExecute(Bitmap map)
    {
        callback.onCompletion(bmp,map);
        if(map.getHeight()!=height || map.getWidth()!=windowWidth)
            imageLayout.setBackground(new BitmapDrawable(activity.getResources(),Bitmap.createScaledBitmap(map, windowWidth, height, false)));
        else
            imageLayout.setBackground(new BitmapDrawable(activity.getResources(),map));
        if(bmp!=null)
            image.setImageBitmap(bmp);
    }

}
