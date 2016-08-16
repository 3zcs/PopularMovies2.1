package com.example.azcs.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.azcs.popularmovies.model.Review;

import java.util.List;

/**
 * Created by azcs on 12/08/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{
    List<Review> mReviewList ;
    Context mContext ;

    public ReviewAdapter(List<Review> mReviewList, Context mContext) {
        this.mReviewList = mReviewList;
        this.mContext = mContext;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list , parent , false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
            holder.mName.setText(mReviewList.get(position).getAuthor());
            holder.mComment.setText(mReviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        TextView mName , mComment ;
        public ReviewHolder(View itemView) {
            super(itemView);
            mName=(TextView)itemView.findViewById(R.id.name_of_commenter);
            mComment = (TextView) itemView.findViewById(R.id.comment);
        }
    }
}
