package com.example.azcs.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.azcs.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

/**
 * Created by azcs on 06/08/16.
 */
public class GridAdapter extends BaseAdapter {
    Context context ;
    List<String> url;
    GridAdapter(Context c , List<String> url){
        context = c ;
        this.url = url;
        Log.v("adapter" , String.valueOf(url.size()));
    }

    public int getCount() {
        return url.size();
    }

    public Object getItem(int position) {
        return url.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context)
                .load(url.get(position))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);

        return imageView;
    }

}