package com.example.azcs.popularmovies;

import com.example.azcs.popularmovies.model.ListOfResult;
import com.example.azcs.popularmovies.model.Movie;
import com.example.azcs.popularmovies.model.Review;
import com.example.azcs.popularmovies.model.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by azcs on 04/08/16.
 */
public interface MoviesAPI {
    //most popular or highest rated
    @GET ("/3/movie/{typeOfSearch}")
    Call <ListOfResult<Movie>> getMovies(@Path("typeOfSearch") String E, @Query("api_key") String Key);
    //review
    @GET ("3/movie/{id}/reviews")
    Call <ListOfResult<Review>> getReviews(@Path("id") int id, @Query("api_key") String Key);
    //video
    @GET ("3/movie/{id}/videos")
    Call <ListOfResult<Video>> getVideo(@Path("id") int id, @Query("api_key") String Key);

}
