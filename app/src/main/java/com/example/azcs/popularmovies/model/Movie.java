package com.example.azcs.popularmovies.model;

/**
 * Created by azcs on 05/08/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Movie implements Parcelable{
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private Double voteAverage;

    public Movie(String posterPath, String overview, String releaseDate, Integer id, String title,
                  Double voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readInt();
        title = in.readString();
        voteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

   public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeDouble(voteAverage);
    }


    public static class  MovieDbHelper extends SQLiteOpenHelper{

        private static final String TEXT_TYPE = " TEXT " ;
        private static final String DOUBLE_TYPE = " DOUBLE " ;
        private static final String COMMA_SEP = ",";
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "MOVIES.db" ;
        private static final String TABLE_NAME = "MOVIE" ;

        private static final String COLUMN_NAME_POSTER_PATH = "posterPath";
        private static final String COLUMN_NAME_OVERVIEW = "overview";
        private static final String COLUMN_NAME_RELEASE_DATE = "releaseDate";
        private static final String COLUMN_NAME_ID = "id";
        private static final String COLUMN_NAME_TITLE = "title";
        private static final String COLUMN_NAME_VOTE_AVERAGE = "voteaverage";

        private static final String SQL_CREATE_MOVIES = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                COLUMN_NAME_POSTER_PATH + TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
                COLUMN_NAME_OVERVIEW  + TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
                COLUMN_NAME_RELEASE_DATE +  TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
                COLUMN_NAME_TITLE +  TEXT_TYPE + " NOT NULL"+ COMMA_SEP +
                COLUMN_NAME_VOTE_AVERAGE +  DOUBLE_TYPE + " NOT NULL )" ;

        private static final String SQL_DROP_MOVIES = "DROP TABLE IF EXIST " + TABLE_NAME ;

        public MovieDbHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_MOVIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DROP_MOVIES);
            onCreate(db);
        }

        public boolean insertData (Movie movie){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_ID , movie.id);
            values.put(COLUMN_NAME_POSTER_PATH,movie.posterPath);
            values.put(COLUMN_NAME_OVERVIEW ,movie.overview);
            values.put(COLUMN_NAME_RELEASE_DATE ,movie.releaseDate);
            values.put(COLUMN_NAME_TITLE ,movie.title);
            values.put(COLUMN_NAME_VOTE_AVERAGE ,movie.voteAverage);
            long result = db.insert(TABLE_NAME , null , values);
            return (result != -1) ? true : false ;

        }

        public boolean reomveData (int id){
            SQLiteDatabase db = this.getWritableDatabase();
            String selection = COLUMN_NAME_ID + " LIKE ?" ;
            String selectionArgs[] = {String.valueOf(id)} ;
            long result = db.delete(TABLE_NAME , selection ,selectionArgs);
            return (result != 0) ? true :false ;
        }

        public boolean movieWasFavorit(int id){
            SQLiteDatabase db = this.getReadableDatabase();
            String Args []={String.valueOf(id)} ;
            final String selection = "SELECT 1 FROM "+TABLE_NAME +" WHERE "+COLUMN_NAME_ID +" = "+id ;
            Cursor c = db.rawQuery(selection ,null);
            return (c.getCount() > 0) ? true : false ;
        }

        public List<Movie> getAllMovies(){
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " ;
            Cursor res = db.rawQuery(query+TABLE_NAME,null);
            res.moveToFirst();
            List<Movie> movieList = new ArrayList<>();
            while (res.moveToNext()){
                Movie movie = new Movie(
                        res.getString(1),
                        res.getString(2) ,
                        res.getString(3),
                        res.getInt(0) ,
                        res.getString(4),
                        res.getDouble(5));
                movieList.add(movie);
            }
            return movieList;
        }
    }
}
