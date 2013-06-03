package org.thingswithworth.metamusic.fileio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.android.gm.api.model.Song;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/19/13
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SongsDataSource
{
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_PLAYCOUNT, MySQLiteHelper.COLUMN_YEAR,
            MySQLiteHelper.COLUMN_ALBUM, MySQLiteHelper.COLUMN_ARTIST, MySQLiteHelper.COLUMN_BPM,
            MySQLiteHelper.COLUMN_COMPOSER, MySQLiteHelper.COLUMN_GENRE, MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_RATING, MySQLiteHelper.COLUMN_TRACKNO,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_LASTPLAYED};

    public SongsDataSource(Context context)
    {
        dbHelper = new MySQLiteHelper(context);

    }
    public void open() throws SQLiteException
    {
        database = dbHelper.getWritableDatabase();
    }
    public void close()
    {
        dbHelper.close();
    }

    public void addSong(Song song)
    {

        if(!isDupliate(song))
        {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, song.getName());
        values.put(MySQLiteHelper.COLUMN_ARTIST, song.getArtist());
        values.put(MySQLiteHelper.COLUMN_ALBUM, song.getAlbum());
        values.put(MySQLiteHelper.COLUMN_GENRE, song.getGenre());
        values.put(MySQLiteHelper.COLUMN_YEAR, song.getYear());
        values.put(MySQLiteHelper.COLUMN_COMPOSER, song.getComposer());
        values.put(MySQLiteHelper.COLUMN_BPM, song.getBeatsPerMinute());
        values.put(MySQLiteHelper.COLUMN_TRACKNO, song.getTrack());
        values.put(MySQLiteHelper.COLUMN_RATING, song.getRating());
        values.put(MySQLiteHelper.COLUMN_LASTPLAYED, song.getLastPlayed());

        database.insert(MySQLiteHelper.TABLE_SONGS, null, values);
        }


    }
    public boolean isDupliate(Song song)
    {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SONGS, new String[]{MySQLiteHelper.COLUMN_ARTIST, MySQLiteHelper.COLUMN_NAME},MySQLiteHelper.COLUMN_ARTIST+"=? and "+MySQLiteHelper.COLUMN_NAME+"=?", new String[]{song.getArtist(), song.getName()}, null,null,null);

        return cursor.getCount()==0;
    }

    public void clear()
    {
        dbHelper.onUpgrade(database,0,0);
    }


}
