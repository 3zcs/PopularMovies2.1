package com.example.azcs.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.azcs.popularmovies.model.ListOfResult;
import com.example.azcs.popularmovies.model.Movie;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoviesFragment extends Fragment {
    @BindView(R.id.gridview) GridView mGridView ;
    @BindView(R.id.txt_network) TextView mNetwork ;
    @BindView(R.id.wait_progress) ProgressBar mWaitProgress ;
    static List<Movie> movies = new ArrayList<>();
    static List<String> url = new ArrayList<>();
    GridAdapter adapter;
    public final String popular = "popular"
            , topRated = "top_rated" ;
    public static final String STATE = "STATE" ;
    OnMovieSelectedListener mCallBack ;
    Movie.MovieDbHelper dbHelper = null ;
    public MoviesFragment(){
    }

    @Override
    public void onAttach(Context context) {
        try {
            mCallBack = (OnMovieSelectedListener) context;
        }catch (ClassCastException e) {
        throw new ClassCastException(context.toString()
                + " must implement OnMovieSelectedListener");
    }

    super.onAttach(context);
    }

    public interface OnMovieSelectedListener {
        public void onMoveSelected(Movie movie);
    }

    public static MoviesFragment getInstance(String hi) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle bundel = new Bundle();
        bundel.putString("tag",hi);
        fragment.setArguments(bundel);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            movies = (ArrayList<Movie>) savedInstanceState.get(STATE);
            makeGrid(movies);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE , (ArrayList) movies);
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this,view);
            if (savedInstanceState != null) {
                movies = (ArrayList<Movie>) savedInstanceState.get(STATE);
                makeGrid(movies);
            }else {
                if (App.NetworkState(getActivity()) == false) {
                    mNetwork.setText(getString(R.string.check_network));
                    mGridView.setVisibility(View.GONE);
                    mWaitProgress.setVisibility(View.GONE);
                }
                makeGrid(popular);
            }

        adapter = new GridAdapter(getActivity(),url);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //pass data
                //save move number "position"
                mCallBack.onMoveSelected(movies.get(position));
            }
        });
        return view ;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_type_menu , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (App.NetworkState(getActivity())) {
            int id = item.getItemId();
            mNetwork.setVisibility(View.GONE);
            switch (id) {
                case R.id.highest_rate:
                    makeGrid(topRated);
                    break;
                case R.id.must_popular:
                    makeGrid(popular);
                    break;
                case R.id.favored :
                    dbHelper = new Movie.MovieDbHelper(getActivity());
                    movies = dbHelper.getAllMovies();
                    url.clear();
                    int i = 0;
                    while (i < movies.size()) {
                        url.add(getString(R.string.url_of_poster) + movies.get(i).getPosterPath());
                        i++;
                    }
                    adapter.notifyDataSetChanged();
                    mWaitProgress.setVisibility(View.GONE);
                    mGridView.setVisibility(View.VISIBLE);
                    break;
            }
        }else
            App.msg(getActivity() , getString(R.string.check_network) );
        return super.onOptionsItemSelected(item);
    }

    public void makeGrid (String typeOfSearch){
        mGridView.setVisibility(View.GONE);
        mWaitProgress.setVisibility(View.VISIBLE);
        MoviesAPI api = App.getClient().create(MoviesAPI.class);
        final Call<ListOfResult<Movie>> moviesCall = api.getMovies(typeOfSearch, BuildConfig.API_KEY);
        moviesCall.enqueue(new Callback<ListOfResult<Movie>>() {
            @Override
            public void onResponse(Call<ListOfResult<Movie>> call, Response<ListOfResult<Movie>> response) {
                movies = response.body().getList();
                url.clear();
                int i = 0;
                while (i < movies.size()) {
                    url.add(getString(R.string.url_of_poster) + movies.get(i).getPosterPath());
                    i++;
                }
                adapter.notifyDataSetChanged();
                mWaitProgress.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ListOfResult<Movie>> call, Throwable t) {

            }
        });
    }

    public void makeGrid (List<Movie> movieList){
        url.clear();
        int i = 0;
        while (i < movies.size()) {
            url.add(getString(R.string.url_of_poster) + movieList.get(i).getPosterPath());
            i++;
        }
        //adapter.notifyDataSetChanged();
        App.msg(getActivity() , String.valueOf(url.size()));
        //mWaitProgress.setVisibility(View.GONE);
        //mGridView.setVisibility(View.VISIBLE);
    }

}

