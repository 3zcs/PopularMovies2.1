package com.example.azcs.popularmovies;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import com.example.azcs.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {
    private boolean mTwoPane ;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    DetailsFragment mDetails =  null; ;
    MoviesFragment mMovies = new MoviesFragment();
    static Movie mMovie = null ;
    static View v ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mMovies = new MoviesFragment();
//        mDetails = new DetailsFragment();
        determinePaneLayout(savedInstanceState);
      }



    private void determinePaneLayout(Bundle bundle) {

            if (findViewById(R.id.detail_container) != null) {
                mTwoPane = true;
                if (bundle != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, mMovies);
                    if (mMovie != null) {
                        Bundle args = new Bundle();
                        args.putParcelable(DetailsFragment.ARGS_POSITION, (Parcelable) mMovie);
                        mDetails = new DetailsFragment();
                        mDetails.setArguments(args);
                        transaction.replace(R.id.detail_container, mDetails);
                    }else {

                    }

                    transaction.commit();
                    return;
                }else {
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.add(R.id.fragment_container, mMovies).commit();
                    }




                } else {
                mTwoPane = false;
                if (bundle != null) {
                    return;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, mMovies).commit();
            }

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("not null"  , 1);
    }

    @Override
    public void onMoveSelected(Movie movie) {
        if(App.NetworkState(getApplicationContext())) {
            if (mTwoPane) {
                if (mMovie == null) {
                    mMovie = movie;
                    DetailsFragment fragment1 = new DetailsFragment();
                    Bundle args = new Bundle();
                    args.putParcelable(DetailsFragment.ARGS_POSITION, (Parcelable) movie);
                    fragment1.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.detail_container, fragment1);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return;
                }
                mMovie = movie;
                mDetails.updateMovie(movie);

            } else {
                mMovie = movie;
                DetailsFragment fragment = new DetailsFragment();
                Bundle args = new Bundle();
                args.putParcelable(DetailsFragment.ARGS_POSITION, (Parcelable) movie);
                fragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }else {
            App.msg(getApplicationContext() , getString(R.string.check_network));
        }
    }



}

