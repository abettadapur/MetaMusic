package org.thingswithworth.metamusic.model;

import android.graphics.Bitmap;
import com.android.gm.api.model.Song;

import java.net.URL;
import java.util.ArrayList;

public class Album implements Comparable
{
    private String title;
    private URL imageUrl;
    private String imagePath;
    private ArrayList<Song> songs;
    private Bitmap cachedArt;
    private Artist artist;
    private Bitmap cachedArtBackground;
    private boolean findingArt;
    private Bitmap cachedBitmapSmall,cachedBitmapMedium,cachedBitmapLarge;
    private URL imgSmall, imgMedium, imgLarge;

    public Album(String title, String imagePath)
    {

        this.title = title;
        this.imagePath=imagePath;
        songs=new ArrayList<Song>();
    }

    public Album(String title, URL imageUrl)
    {
        this.title = title;
        this.imageUrl=imageUrl;
        songs=new ArrayList<Song>();
    }

    public ArrayList<Song> getSongs(){
        return songs;
    }

    public void addSong(Song song){
        songs.add(song);
    }

    public void addSong(Song song, int at){
        if(songs.size()==0)
            songs.add(song);
        else
            for(int addAt=0;addAt<songs.size();addAt++){
                if(songs.get(addAt).getTrack()>at || addAt==songs.size()-1) {
                    songs.add(addAt,song);
                    addAt=songs.size();
                }
            }
    }

    public String getName()
    {
        return title;
    }

    public Bitmap getCachedArt() {
        return cachedArt;
    }

    public void setCachedArt(Bitmap cachedArt) {
        if(cachedArt!=null){
            if(!cachedArt.isMutable())
                this.cachedArt = cachedArt.copy(Bitmap.Config.ARGB_8888, true);
            else
                this.cachedArt = cachedArt;
        }
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public URL getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object object)
    {
        if(object instanceof Album)
        {
            Album album = (Album)object;
            return album.getName().equalsIgnoreCase(title);
        }
        return false;
    }

    @Override
    public int compareTo(Object o)
    {
        if(o instanceof Album)
        {
            Album album = (Album)o;
            return title.compareTo(album.getName());
        }
        return -1;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setCachedArtBackground(Bitmap cachedArtBackground) {
        this.cachedArtBackground = cachedArtBackground;
    }

    public Bitmap getCachedArtBackground() {
        return cachedArtBackground;
    }

    public boolean isFindingArt() {
        return findingArt;
    }

    public void setFindingArt(boolean findingArt) {
        this.findingArt = findingArt;
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
}
