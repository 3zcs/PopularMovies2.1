package com.example.azcs.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by azcs on 15/08/16.
 */
public class ListOfResult<T> {
    @SerializedName("results")
    private List<T> List ;

    public void setList(List<T> List){
        this.List = List ;
    }

    public List<T> getList(){
        return List ;
    }

}
