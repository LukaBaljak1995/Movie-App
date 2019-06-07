package com.example.svarog.project01.utility;

import com.example.svarog.project01.entities.JsonMovieRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by svarog on 28.9.17..
 */

public interface DownloadMovieList {

    @GET("movie/{movieList}?api_key=95e488b3478db8e89b6cd80a69913088")
    Call<JsonMovieRoot> getMovies(@Path("movieList") String movieList);
}
