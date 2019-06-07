package com.example.svarog.project01.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.svarog.project01.entities.Movie;
import com.example.svarog.project01.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by svarog on 27.9.17..
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG ="RC_ADAPTER" ;
    private final ListItemClickListener listener;
    private int numberOfelements;
    private List<Movie> movies;
    private Context context;
    private int mWidth;
    private int mHeight;



    public MovieAdapter(int numberOfelements, ListItemClickListener listener, int mWidth, int mHeight) {
        this.numberOfelements = numberOfelements;
        this.listener = listener;
        this.mHeight = mHeight;
        this.mWidth = mWidth;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_movie, parent, false);
        MovieViewHolder holder = new MovieViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
//TODO implement to download image acording to position
        Log.i(TAG, position+"");
        holder.bind(movies.get(position));
//        Log.i(TAG,"id : " + movies.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return numberOfelements;
    }


    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public void setItems(List<Movie> items) {
        this.movies = items;
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movieImage;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
//            Log.i(TAG, "Bind pics :" + movie.getPosterPath());
            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w185/"+movie.getPosterPath())
                    .resize(mWidth/2, mHeight/2)
                    .centerCrop()
                    .into(movieImage);

        }

        @Override
        public void onClick(View view) {
//            Log.i("Adapter", "click");
            listener.onListItemClick(getAdapterPosition());
        }
    }

}
