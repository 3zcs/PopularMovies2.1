package com.example.azcs.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by azcs on 05/08/16.
 */
public class Video implements Parcelable{
    @SerializedName("key")
    private String key ;
    @SerializedName("name")
    private String name ;

    public Video(String key, String name) {
        this.key = key;
        this.name = name;
    }

    protected Video(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
    }
}
