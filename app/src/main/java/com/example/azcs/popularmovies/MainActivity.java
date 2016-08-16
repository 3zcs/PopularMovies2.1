package com.example.azcs.popularmovies;

import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.azcs.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {
    private boolean mTwoPane;
    private String mLocation;

    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true ;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container ,new DetailsFragment() , DETAILFRAGMENT_TAG).commit();
        }else
            mTwoPane = false;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container ,new MoviesFragment()).commit();
        App.msg(getApplicationContext() , "onCreate");
    }



    @Override
    public void onMoveSelected(Movie movie) {

        if(mTwoPane){
            //two pain-fragment
            DetailsFragment fragment = (DetailsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.detail_container);
            fragment.updateMovie(movie);
        }else{
            DetailsFragment detailsFragment = new DetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable( DetailsFragment.ARGS_POSITION , (Parcelable) movie);
            detailsFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container ,detailsFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

}

