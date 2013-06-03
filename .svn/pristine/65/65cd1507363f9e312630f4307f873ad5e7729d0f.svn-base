package org.thingswithworth.metamusic.fileio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 4/19/13
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySQLiteHelper  extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME="thingswithworth.db";
    private static final int DATABASE_VERSION=1;

    public static final String TABLE_SONGS="songs";

    public static final String COLUMN_ID="_id";
    public static final String COLUMN_NAME="name";
    public static final String COLUMN_ARTIST="artist";
    public static final String COLUMN_ALBUM="album";
    public static final String COLUMN_GENRE="genre";
    public static final String COLUMN_LASTPLAYED="lastplayed";
    public static final String COLUMN_TRACKNO="trackno";
    public static final String COLUMN_RATING="rating";
    public static final String COLUMN_BPM="bpm";
    public static final String COLUMN_COMPOSER="composer";
    public static final String COLUMN_YEAR="year";
    public static final String COLUMN_PLAYCOUNT="playcount";

    public static final String DATABASE_MAKE="create table "+TABLE_SONGS+"("+
            COLUMN_ID+" integer primary key autoincrement, "+
            COLUMN_NAME+" text not null, "+
            COLUMN_ARTIST+" text not null, "+
            COLUMN_ALBUM+" text not null, "+
            COLUMN_GENRE+" text not null, "+
            COLUMN_LASTPLAYED+" integer not null, "+
            COLUMN_TRACKNO+" integer not null, "+
            COLUMN_RATING+" integer not null, "+
            COLUMN_BPM+" integer not null, "+
            COLUMN_COMPOSER+" text not null, "+
            COLUMN_YEAR+" integer not null, "+
            COLUMN_PLAYCOUNT+" text);";


    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(DATABASE_MAKE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_SONGS);
        onCreate(sqLiteDatabase);
    }


}
