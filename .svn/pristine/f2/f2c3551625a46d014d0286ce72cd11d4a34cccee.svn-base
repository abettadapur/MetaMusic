package org.thingswithworth.metamusic.model;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/19/13
 * Time: 11:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class Artist {
    private String artistName, summary;
    private URL imgSmall, imgMedium, imgLarge;
    private ArrayList<Album> albums;
    private Bitmap cachedBitmapSmall,cachedBitmapMedium,cachedBitmapLarge,cachecBitmapBackground;

    public Artist(String artistName) {
        this.artistName = artistName;
        albums=new ArrayList<Album>();
    }

    public Artist(String artistName, String summary, URL imgSmall, URL imgMedium, URL imgLarge) {
        this.artistName = artistName;
        this.summary = summary;
        this.imgSmall = imgSmall;
        this.imgMedium = imgMedium;
        this.imgLarge = imgLarge;
        albums=new ArrayList<Album>();
    }

    public void addAlbum(Album album){
        albums.add(album);
    }

    public Album getAlbum(String name){
       for(Album album:albums)
           if(album.getName().equals(name))
               return album;
        return null;
    }

    public ArrayList<Album> getAlbums(){
        return albums;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setImgMedium(URL imgMedium) {
        this.imgMedium = imgMedium;
    }

    public void setImgSmall(URL imgSmall) {
        this.imgSmall = imgSmall;
    }

    public void setImgLarge(URL imgLarge) {
        this.imgLarge = imgLarge;
    }

    public String getName() {
        return artistName;
    }

    public URL getImgSmall() {
        return imgSmall;
    }

    public URL getImgMedium() {
        return imgMedium;
    }

    public URL getImgLarge() {
        return imgLarge;
    }

    public Bitmap getCachedBitmapSmall() {
        return cachedBitmapSmall;
    }

    public void setCachedBitmapSmall(Bitmap cachedBitmapSmall) {
        this.cachedBitmapSmall = cachedBitmapSmall;
    }

    public Bitmap getCachedBitmapMedium() {
        return cachedBitmapMedium;
    }

    public void setCachedBitmapMedium(Bitmap cachedBitmapMedium) {
        this.cachedBitmapMedium = cachedBitmapMedium;
    }

    public Bitmap getCachedBitmapLarge() {
        return cachedBitmapLarge;
    }

    public void setCachedBitmapLarge(Bitmap cachedBitmapLarge) {
        this.cachedBitmapLarge = cachedBitmapLarge;
    }

    public Bitmap getCachecBitmapBackground() {
        return cachecBitmapBackground;
    }

    public void setCachecBitmapBackground(Bitmap cachecBitmapBackground) {
        this.cachecBitmapBackground = cachecBitmapBackground;
    }
}
