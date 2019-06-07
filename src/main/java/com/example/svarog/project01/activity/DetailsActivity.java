package com.example.svarog.project01.activity;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.svarog.project01.R;
import com.example.svarog.project01.db.AppDbHelper;
import com.example.svarog.project01.db.MovieContract;
import com.example.svarog.project01.entities.JsonReviewRoot;
import com.example.svarog.project01.entities.JsonTrailerRoot;
import com.example.svarog.project01.entities.Movie;
import com.example.svarog.project01.entities.Review;
import com.example.svarog.project01.entities.Trailer;
import com.example.svarog.project01.utility.DownloadReview;
import com.example.svarog.project01.utility.DownloadTrailerList;
import com.example.svarog.project01.utility.ListItemClickListener;
import com.example.svarog.project01.utility.MovieReviewClient;
import com.example.svarog.project01.utility.TrailerAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity implements ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    private final String TAG = "DetailsAcitivty";
    private TextView mMessage;
    private ImageView mPoster;
    private int mHeight;
    private int mWidth;
    private double mAverage;
    private ImageButton mFavorite;
    private TextView mVoteAverage;
    private TextView mYear;
    private String year;
    public static String title;
    private TextView mTitle;
    private int movieId;
    private Response<JsonTrailerRoot> response;
    private RecyclerView mTrailerList;
    private TrailerAdapter mAdapter;
    private JsonTrailerRoot mJsonTrailerRoot;
    private List<Trailer> mTrailers;
    private TextView mDescription;
    private AppDbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private boolean isFavorite;
    private String mPosterUrl;
    private TextView mReview;
    private Parcelable layoutManager;
    private ScrollView mScrollView;
    private int[] position;
    private Movie movie;

    Button buybtn;
    Button buy2btn;
    Button buy3btn;
    Button buy4btn;

    public static int hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle(getString(R.string.details_activity_title));


        mDbHelper = new AppDbHelper(this);
        mDb = mDbHelper.getWritableDatabase();

        dispayMetrics();


        mPoster = (ImageView) findViewById(R.id.movie_poster);
        mVoteAverage = (TextView) findViewById(R.id.movie_radting);
        mYear = (TextView) findViewById(R.id.movie_year);
        mTitle = (TextView) findViewById(R.id.movie_title);
        mTrailerList = (RecyclerView) findViewById(R.id.trailer_list);
        mDescription = (TextView) findViewById(R.id.description);
        mFavorite = (ImageButton) findViewById(R.id.favorite_button);
        mReview = (TextView) findViewById(R.id.reviews);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        buybtn = (Button) findViewById(R.id.buybtn);
        buy2btn = (Button) findViewById(R.id.buy2btn);
        buy3btn = (Button) findViewById(R.id.buy3btn);
        buy4btn = (Button) findViewById(R.id.buy4btn);
        getDataFromIntent();

        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, GeneratorActivity.class);
                hour = 16;
                startActivity(intent);
            }
        });
        buy2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, GeneratorActivity.class);
                hour = 18;
                startActivity(intent);
            }
        });
        buy3btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, GeneratorActivity.class);
                hour = 20;
                startActivity(intent);
            }
        });
        buy4btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, GeneratorActivity.class);
                hour = 22;
                startActivity(intent);
            }
        });


        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isFavorite) {
                    isFavorite = true;
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.MOVIE_ID, movie.getId());
                    values.put(MovieContract.MovieEntry.POSTER_PATH, movie.getPosterPath());
                    values.put(MovieContract.MovieEntry.YEAR, movie.getReleaseDate());
                    values.put(MovieContract.MovieEntry.VOTE_AVERAGE, movie.getVoteAverage());
                    values.put(MovieContract.MovieEntry.TITLE, movie.getTitle());
                    values.put(MovieContract.MovieEntry.DESCRIPTION, movie.getOverview());

                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

                    if (isFavorite) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.if_heart, null));
                        } else {
                            mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.if_heart));
                        }
                    }

                } else {


                    isFavorite = false;
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.MOVIE_ID, movieId);

                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;


                    getContentResolver().delete(uri, values.toString(), null);
                    getSupportLoaderManager().restartLoader(1, null, DetailsActivity.this);


                    if (!isFavorite) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.ic_favorite_black_48dp, null));
                        } else {
                            mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
                        }
                    }
                }

            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(manager);




        getMovieReview();

        new AsyncTask<Integer, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mFavorite.setVisibility(View.INVISIBLE);
            }

            @Override
            protected Boolean doInBackground(Integer... ineIntegers) {

                String[] projection = {MovieContract.MovieEntry._ID, MovieContract.MovieEntry.MOVIE_ID};

                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, projection, MovieContract.MovieEntry.MOVIE_ID + "=?", new String[]{movieId + ""}, null);

                String id = "notFound";
                while (cursor.moveToNext()) {
                    id = cursor.getString(cursor.getColumnIndex("movieId"));
                }
                if (id.equals(movieId + "")) {

                    return true;
                } else {

                    return false;
                }
            }


            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                isFavorite = aBoolean;
                if (isFavorite) {
                    mFavorite.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.if_heart, null));
                    } else {
                        mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.if_heart));
                    }
                } else {
                    mFavorite.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.ic_favorite_black_48dp, null));
                    } else {
                        mFavorite.setImageDrawable(DetailsActivity.this.getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
                    }
                }

                if (position != null)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.scrollTo(position[0], position[1]);
                        }
                    }, 500);
            }


        }.execute(movieId);
        restorePositions(savedInstanceState);
    }

    private void getMovieReview() {
        DownloadReview reviewService = MovieReviewClient.getClient().create(DownloadReview.class);
        Call<JsonReviewRoot> call = reviewService.getReview(movieId);
        call.enqueue(new Callback<JsonReviewRoot>() {
            @Override
            public void onResponse(Call<JsonReviewRoot> call, Response<JsonReviewRoot> response) {
                if (response == null) {

                } else {

                    JsonReviewRoot root = response.body();
                    String rev = getString(R.string.no_reviews);
                    Log.i(TAG, root.getResults().size() + "");
                    if (root.getResults().size() != 0) {
                        for (Review review : root.getResults()) {
                            Log.i(TAG, review.getContent());
                            rev += review.getContent() + "\n" + "-------" + "\n";
                        }
                        mReview.setText(root.getResults().get(0).getContent());
                    } else {
                        mReview.setText(rev);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonReviewRoot> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getDataFromIntent() {

        movie = getIntent().getExtras().getParcelable("movie");
        movieId = movie.getId();
            mDescription.setText(movie.getOverview());


            if (movieId != -1) {
                FetchTrailerData fetchTrailerData = new FetchTrailerData();
                fetchTrailerData.execute(movieId);
            }

            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w185/" + movie.getPosterPath())
                    .resize(mWidth / 3, mHeight / 3)
                    .centerCrop()
                    .into(mPoster);
            mPosterUrl = movie.getPosterPath();


            mAverage = movie.getVoteAverage();
            mVoteAverage.setText(mAverage + "/10.0");
        Log.i(TAG, movie.getId()+"");


            year = movie.getReleaseDate();
            mYear.setText(year.substring(0, 4));


            title = movie.getTitle();
            mTitle.setText(title.toString());
        }


    public void dispayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mHeight = height;
        mWidth = width;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mTrailers.get(clickedItemIndex).getKey())));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor cursor = null;


            @Override
            protected void onStartLoading() {
                if (cursor != null) {
                    deliverResult(cursor);
                } else {

                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                String[] projection = {MovieContract.MovieEntry._ID, MovieContract.MovieEntry.MOVIE_ID};
                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, projection, MovieContract.MovieEntry.MOVIE_ID + "=?", new String[]{movieId + ""}, null);

                return cursor;
            }

            public void deliverResult(Cursor data) {
                cursor = data;

                super.deliverResult(data);
            }

        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        String id = "notFound";
        while (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("movieId"));
        }

        if (id.equals(movieId)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mFavorite.setImageDrawable(this.getResources().getDrawable(R.drawable.if_heart, null));
            } else {
                mFavorite.setImageDrawable(this.getResources().getDrawable(R.drawable.if_heart));
            }
        } else if (id.equals("notFound")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mFavorite.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_favorite_black_48dp, null));
            } else {
                mFavorite.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_favorite_black_48dp));
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        getSupportLoaderManager().restartLoader(1, null, this);
    }

    private void restorePositions(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            position = savedInstanceState.getIntArray("manager");

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("manager", new int[]{mScrollView.getScrollX(), mScrollView.getScrollY()});
        Log.i(TAG, "X: " + mScrollView.getScaleX() + "| Y: " + mScrollView.getScaleY());

    }

    public class FetchTrailerData extends AsyncTask<Integer, Void, JsonTrailerRoot> {
        private static final String API_BASE_URL = "https://api.themoviedb.org/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JsonTrailerRoot doInBackground(Integer... integers) {

            Integer id = integers[0];

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(API_BASE_URL)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            );

            Retrofit retrofit = builder.client(httpClient.build()).build();
            DownloadTrailerList downloadMovieList = retrofit.create(DownloadTrailerList.class);

            Call<JsonTrailerRoot> request = downloadMovieList.getTrailers(id.toString());


            try {
                response = request.execute();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null && response.isSuccessful()) {
                    return response.body();
                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(JsonTrailerRoot jsonTrailerRoot) {
            mJsonTrailerRoot = jsonTrailerRoot;
            mAdapter = new TrailerAdapter(mJsonTrailerRoot.getResults().size(), DetailsActivity.this);
            mAdapter.setTrailers(mJsonTrailerRoot.getResults());
            mTrailerList.setAdapter(mAdapter);
            mTrailers = mJsonTrailerRoot.getResults();
            mAdapter.setTrailers(mTrailers);
            mAdapter.notifyDataSetChanged();
            mTrailerList.getLayoutManager().onRestoreInstanceState(layoutManager);
        }
    }
}
