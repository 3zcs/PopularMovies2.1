package com.example.azcs.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.azcs.popularmovies.model.Video;
import java.util.List;


/**
 * Created by azcs on 11/08/16.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder>{
    private List<Video> mVideoList ;
    private Context mContext ;
    private String url ;

    public TrailerAdapter(List<Video> videoList, Context context) {
        mVideoList = videoList ;
        mContext = context ;
        if(mContext != null)
        url = mContext.getString(R.string.youtube_url);

    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list , parent ,false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        holder.name.setText(mVideoList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.name_of_trailer)
        TextView name ;
        public TrailerHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name_of_trailer);
//            ButterKnife.bind(mContext , name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url+mVideoList.get(getAdapterPosition()).getKey())));
                }
            });
        }
    }
}
