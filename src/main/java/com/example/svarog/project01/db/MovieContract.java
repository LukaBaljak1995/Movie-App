package com.example.svarog.project01.db;

import android.net.Uri;
import android.provider.BaseColumns;


public final class MovieContract {

    public static final String AUTHORITY = "com.example.svarog.project01";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favMovie";

    public static class MovieEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE = "favMovie";

        public static final String MOVIE_ID = "movieId";

        public static final String POSTER_PATH = "posterPath";

        public static final String YEAR = "year";

        public static final String VOTE_AVERAGE = "voteAverage";

        public static final String TITLE = "title";

        public static final String DESCRIPTION = "description";



    }
}
