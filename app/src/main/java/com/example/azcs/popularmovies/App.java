package com.example.azcs.popularmovies;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by azcs on 07/08/16.
 */
public class App extends Application {

    public static boolean NetworkState(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        return info != null
                && info.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }



    public static final String ENDPOINT = "http://api.themoviedb.org" ;
    public static Retrofit retrofit = null ;
    public static Retrofit getClient(){
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofit ;
    }

    public static void msg (Context context , String s){
        Toast.makeText(context , s , Toast.LENGTH_SHORT).show();
    }
}
