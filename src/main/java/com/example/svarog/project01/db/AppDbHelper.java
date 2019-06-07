package com.example.svarog.project01.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class AppDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";

    private static final int DB_VERSION = 1;

    public AppDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE_QUERY = "CREATE TABLE " + MovieContract.MovieEntry.TABLE +
                "(" + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.MOVIE_ID + " INTEGER NOT NULL," +
                MovieContract.MovieEntry.POSTER_PATH + " VARCHAR(255) NOT NULL," +
                MovieContract.MovieEntry.DESCRIPTION + " VARCHAR(255) NOT NULL," +
                MovieContract.MovieEntry.TITLE + " VARCHAR(255) NOT NULL," +
                MovieContract.MovieEntry.VOTE_AVERAGE + " REAL NOT NULL," +
                MovieContract.MovieEntry.YEAR + " VARCHAR(255) NOT NULL" + ");";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE);
        onCreate(sqLiteDatabase);
    }
}
