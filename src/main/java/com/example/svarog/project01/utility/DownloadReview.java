package com.example.svarog.project01.utility;

import com.example.svarog.project01.entities.JsonMovieRoot;
import com.example.svarog.project01.entities.JsonReviewRoot;
import com.example.svarog.project01.entities.Review;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by svarog on 15.10.17..
 */

public interface DownloadReview {


    @GET("movie/{movieId}/reviews?api_key=95e488b3478db8e89b6cd80a69913088")
    Call<JsonReviewRoot> getReview(@Path("movieId") int movieId);
}
