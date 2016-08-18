package com.example.azcs.popularmovies;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.azcs.popularmovies.model.ListOfResult;
import com.example.azcs.popularmovies.model.Movie;
import com.example.azcs.popularmovies.model.Review;
import com.example.azcs.popularmovies.model.Video;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    @BindView(R.id.poster)
    ImageView mPoster;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.rate)
    TextView mRate;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.plot)
    TextView mOverview;
    @BindView(R.id.recycler_trailer)
    RecyclerView mTrailers;
    @BindView(R.id.recycler_review)
    RecyclerView mReviews;
    @BindView(R.id.favorite)
    Button favorite;
    @BindView(R.id.wait_review)
    ProgressBar mWaitReview;
    @BindView(R.id.wait_trailer)
    ProgressBar mWaitTrailer;
    static View view ;
    private Context mContext;
    private List<Review> mReviewList = new ArrayList<>();
    private ReviewAdapter mRAdapter;
    private TrailerAdapter mDAdapter;
    private RecyclerView.LayoutManager mLayoutManager, mRLayoutManager;
    public static final String ARGS_POSITION = "POSITION";
    private List<Video> mVideoList = new ArrayList<>();
    private Movie mMovie;
    Movie.MovieDbHelper dbHelper = null;
    private static final String MOVIE = "MOVIE", TRAILERS = "TRAILERS", REVIEWS = "REVIEWS";

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (App.NetworkState(getActivity())) {
                if (getArguments() != null) {
                    mMovie = getArguments().getParcelable(ARGS_POSITION);
                }
            } else {
                App.msg(getActivity(), getString(R.string.check_network));
            }
        } else {
            mVideoList = savedInstanceState.getParcelableArrayList(TRAILERS);
            mReviewList = savedInstanceState.getParcelableArrayList(REVIEWS);
            mMovie = savedInstanceState.getParcelable(MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new Movie.MovieDbHelper(getActivity());
        view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        mTrailers.setVisibility(View.GONE);
        mReviews.setVisibility(View.GONE);

        if (mMovie != null) {
            makeView(mMovie);
        }

        return view;
    }

    //Land scape update
    public void updateMovie(Movie movie) {
        if (movie != null)
            makeView(movie);
    }

    public void makeView(Movie movie) {
        mMovie = movie;
        MoviesAPI api = App.getClient().create(MoviesAPI.class);
        Call<ListOfResult<Video>> videosCall = api.getVideo(movie.getId(), BuildConfig.API_KEY);
        videosCall.enqueue(new Callback<ListOfResult<Video>>() {
            @Override
            public void onResponse(Call<ListOfResult<Video>> call, Response<ListOfResult<Video>> response) {
                mVideoList = response.body().getList();
                mDAdapter = new TrailerAdapter(mVideoList, getActivity());
                mTrailers.setAdapter(mDAdapter);
                mDAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ListOfResult<Video>> call, Throwable t) {

            }
        });
        Call<ListOfResult<Review>> reviewsCall = api.getReviews(movie.getId(), BuildConfig.API_KEY);
        reviewsCall.enqueue(new Callback<ListOfResult<Review>>() {
            @Override
            public void onResponse(Call<ListOfResult<Review>> call, Response<ListOfResult<Review>> response) {
                mReviewList = response.body().getList();
                mRAdapter = new ReviewAdapter(mReviewList, getActivity());
                mReviews.setAdapter(mRAdapter);
                mWaitReview.setVisibility(View.GONE);
                mWaitTrailer.setVisibility(View.GONE);
                mTrailers.setVisibility(View.VISIBLE);
                mReviews.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<ListOfResult<Review>> call, Throwable t) {

            }
        });
        //trailer
        mLayoutManager = new LinearLayoutManager(mContext);
        mDAdapter = new TrailerAdapter(mVideoList, mContext);
        mTrailers.setLayoutManager(mLayoutManager);
        mTrailers.setAdapter(mDAdapter);
        //review
        mRAdapter = new ReviewAdapter(mReviewList, mContext);
        mRLayoutManager = new LinearLayoutManager(mContext);
        mReviews.setLayoutManager(mRLayoutManager);
        mReviews.setAdapter(mRAdapter);

        mDate.setText(mMovie.getReleaseDate());
        mRate.setText(String.valueOf(mMovie.getVoteAverage()));
        mOverview.setText(mMovie.getOverview());
        mTitle.setText(mMovie.getTitle());
        Picasso.with(getActivity())
                .load(getString(R.string.url_of_poster) + mMovie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(mPoster);

        if (dbHelper.movieWasFavorit(mMovie.getId())) {
            favorite.setText(getString(R.string.favored));
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.movieWasFavorit(mMovie.getId())) {
                    dbHelper.reomveData(mMovie.getId());
                    App.msg(getActivity(), getString(R.string.deleteMovie));
                    favorite.setText(getString(R.string.favorite));
                } else {
                    dbHelper.insertData(mMovie);
                    App.msg(getActivity(), getString(R.string.insertMovie));
                    favorite.setText(getString(R.string.favored));
                }
            }

        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE, mMovie);
        outState.putParcelableArrayList(TRAILERS, (ArrayList) mVideoList);
        outState.putParcelableArrayList(REVIEWS, (ArrayList) mReviewList);

    }
}