package com.example.svarog.project01.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.svarog.project01.R;
import com.example.svarog.project01.entities.Trailer;

import java.util.List;

/**
 * Created by svarog on 3.10.17..
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private final int numberOfElements;
    private Context context;
    private List<Trailer> trailers;
    private ListItemClickListener listener;

    public TrailerAdapter( int numberOfElements,ListItemClickListener listener) {
        this.listener = listener;
        this.numberOfElements = numberOfElements;

    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_trailer, parent, false);
        TrailerViewHolder holder = new TrailerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(trailers.get(position));

    }


    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setTrailers(List<Trailer> trailers){
        this.trailers = trailers;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_trailer;
        private LinearLayout layout;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.trailer_container_layout);
            tv_trailer = itemView.findViewById(R.id.trailer_holder);
            layout.setOnClickListener(this);
        }

        public void bind(Trailer trailerData){
            tv_trailer.setText("Trailer " + (1+getAdapterPosition()));

        }

        @Override
        public void onClick(View view) {
            listener.onListItemClick(getAdapterPosition());
        }
    }

}
