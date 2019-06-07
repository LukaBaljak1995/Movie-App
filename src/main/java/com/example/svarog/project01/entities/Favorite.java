package com.example.svarog.project01.entities;

import android.arch.persistence.room.PrimaryKey;


public class Favorite {

    public Favorite() {
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movieId;
    private boolean isFavorite = false;

    public Favorite(int id, int movieId, boolean isFavorite) {
        this.id = id;
        this.movieId = movieId;
        this.isFavorite = isFavorite;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int id) {
        this.movieId = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
