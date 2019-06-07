package com.example.svarog.project01.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.svarog.project01.R;
import com.example.svarog.project01.db.MovieContract;
import com.example.svarog.project01.entities.JsonMovieRoot;
import com.example.svarog.project01.entities.Movie;
import com.example.svarog.project01.utility.DownloadMovieList;
import com.example.svarog.project01.utility.ListItemClickListener;
import com.example.svarog.project01.utility.MovieAdapter;
import com.example.svarog.project01.utility.MovieListClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    private static final String TAG = "MainActivity";
    private final static String MOST_POPULAR = "popular";
    private final static String TOP_RATED = "top_rated";
    private final static String FAVORITE = "favorite";
    private RecyclerView mMovieList;
    private MovieAdapter mAdapter;
    private JsonMovieRoot mJsonMovieRoot;
    private ProgressBar mProgressBar;
    private int mWidth, mHeight;
    private ImageView mNoInternet;
    private GridLayoutManager manager;
    private String optionItem;
    private Parcelable layoutManager;
    private List<Movie> movies;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_activity_title));

        SharedPreferences pref = getApplicationContext().getSharedPreferences("theApp", MODE_PRIVATE);

        restorePositions(savedInstanceState);
        Log.e(TAG, "shared pref contains last state : " + pref.contains("option("));
        if (pref.contains("option")) {

            optionItem = pref.getString("option", null);
        } else {
            Log.e(TAG, "shared pref notfound : " + optionItem);
        }

        mMovieList = (RecyclerView) findViewById(R.id.movie_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mNoInternet = (ImageView) findViewById(R.id.no_internet);

        dispayMetrics();

        mProgressBar.setVisibility(View.VISIBLE);
        mMovieList.setVisibility(View.INVISIBLE);

        manager = new GridLayoutManager(this, 2);
        mMovieList.setLayoutManager(manager);
        if (isNetworkConnected()) {
            if (optionItem.equals(MOST_POPULAR) || optionItem.equals(TOP_RATED))
                downloadMovies(optionItem);
            else {
                getFavoriteMovies();
            }
        } else {

            mNoInternet.setVisibility(View.VISIBLE);
        }





    }



    private void restorePositions(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            Parcelable parcelable = savedInstanceState.getParcelable("manager");

            layoutManager = parcelable;


        } else {
            Log.e(TAG, "instance state is null ? " + (savedInstanceState == null));
            optionItem = MOST_POPULAR;
        }
    }

    private void updateOptionSelected(String optionItem) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("theApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("option", optionItem);
        editor.commit();
    }

    private void downloadMovies(String path) {
        DownloadMovieList movieService = MovieListClient.getClient().create(DownloadMovieList.class);
        Call<JsonMovieRoot> call = movieService.getMovies(path);
        call.enqueue(new Callback<JsonMovieRoot>() {
            @Override
            public void onResponse(Call<JsonMovieRoot> call, Response<JsonMovieRoot> response) {

                mProgressBar.setVisibility(View.INVISIBLE);
                mMovieList.setVisibility(View.VISIBLE);

                if (response == null) {
//                    Log.i(TAG, "no response");
                    Log.e(TAG, "NO RESONSE ");
                } else {
                    if (response.isSuccessful()) {
                        mJsonMovieRoot = response.body();
                        movies = mJsonMovieRoot.getResults();
                        mAdapter = new MovieAdapter(movies.size(), MainActivity.this, mWidth, mHeight);

                        mMovieList.setAdapter(mAdapter);
//                setItems(mJsonMovieRoot.getResults());
                        mAdapter.setMovies(response.body().getResults());
                        mAdapter.notifyDataSetChanged();

                        if (layoutManager != null) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mMovieList.getLayoutManager().onRestoreInstanceState(layoutManager);
                                }
                            }, 300);
                        } else {
                            Toast.makeText(MainActivity.this, "Response " + response.isSuccessful(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonMovieRoot> call, Throwable t) {
                Log.e(TAG, "ERROR DOWNLOADING");
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isNetworkConnected()) {

            mNoInternet.setVisibility(View.INVISIBLE);

            switch (item.getItemId()) {

                case R.id.most_popular:

                    getMostPopularMovies();

                    break;

                case R.id.top_rated:

                    getTopRatedMovies();

                    break;

                case R.id.fav_movies:

                    getFavoriteMovies();

                    break;
            }
            updateOptionSelected(optionItem);
            return true;
        }
        return false;
    }


    private void getTopRatedMovies() {
        optionItem = TOP_RATED;
        downloadMovies(optionItem);
    }

    private void getMostPopularMovies() {
        optionItem = MOST_POPULAR;
        downloadMovies(optionItem);
    }

    public void getFavoriteMovies() {
        new AsyncTask<Void, Void, Cursor>() {

            @Override
            protected Cursor doInBackground(Void... voids) {
                String[] projection = new String[]{
                        MovieContract.MovieEntry.MOVIE_ID,
                        MovieContract.MovieEntry.POSTER_PATH,
                        MovieContract.MovieEntry.TITLE,
                        MovieContract.MovieEntry.YEAR,
                        MovieContract.MovieEntry.DESCRIPTION,
                        MovieContract.MovieEntry.VOTE_AVERAGE
                };
                String selectionClause = null;
                String[] selectionArgs = null;
                String sortOrder = null;


                Cursor cursor = getContentResolver().query(
                        MovieContract.MovieEntry.CONTENT_URI,
                        projection,
                        selectionClause,
                        selectionArgs,
                        sortOrder);

                return cursor;
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                Log.i(TAG, "Cursor " + cursor);
                Log.i(TAG, "cursor move to next : " + cursor.moveToNext());

                List<Movie> dbMovies = new ArrayList<>();
            while (cursor.moveToNext()){
                    Log.i(TAG, "IN WHILE");

                    int id = cursor.getInt(cursor.getColumnIndex("movieId"));
                    String path = cursor.getString(cursor.getColumnIndex("posterPath"));

//                    YEAR
                    String year = cursor.getString(cursor.getColumnIndex("year"));
//                            VOTE_AVERAGE
                    double vote = cursor.getDouble(cursor.getColumnIndex("voteAverage"));

//                    TITLE
                    String title = cursor.getString(cursor.getColumnIndex("title"));
//                            DESCRIPTION
                    String desc = cursor.getString(cursor.getColumnIndex("description"));

                    Movie m = new Movie(id, vote, title, path, desc, year);

                    dbMovies.add(m);
                }
                Log.i(TAG, "list size : " + dbMovies.size());
                if (dbMovies.size() > 0) {
                    Log.i(TAG, "Adapter " + (mAdapter == null));
                    mAdapter = new MovieAdapter(dbMovies.size(), MainActivity.this, mWidth, mHeight);
                    mMovieList.setAdapter(mAdapter);
                    mAdapter.setMovies(dbMovies);
                    movies = dbMovies;
                    mAdapter.notifyDataSetChanged();
                    mMovieList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    if (layoutManager != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mMovieList.getLayoutManager().onRestoreInstanceState(layoutManager);
                            }
                        }, 300);
                    }
                }

                mProgressBar.setVisibility(View.INVISIBLE);
                mMovieList.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        if (isNetworkConnected()) {
            Intent intent = new Intent(this, DetailsActivity.class);
            Movie selectedMovie = movies.get(clickedItemIndex);
            Log.i(TAG, selectedMovie.getId()+"");
            intent.putExtra("movie", selectedMovie);
            startActivity(intent);
        } else {
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("manager", mMovieList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(optionItem.equals(FAVORITE)){
            getFavoriteMovies();
        }
    }


}
