package com.example.svarog.project01.utility;

import com.example.svarog.project01.entities.JsonTrailerRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by svarog on 3.10.17..
 */

public interface DownloadTrailerList {
    @GET("3/movie/{movieId}/videos?api_key=95e488b3478db8e89b6cd80a69913088")
    Call<JsonTrailerRoot> getTrailers(@Path("movieId") String id);
}
