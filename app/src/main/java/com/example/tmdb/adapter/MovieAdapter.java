package com.example.tmdb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tmdb.R;
import com.example.tmdb.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static List<Movie> movies;
    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        TextView movieTitle;
        TextView data;
        TextView movieDescription;
        TextView rating;
        TextView language;
        ImageView poster;

        public MovieViewHolder(final View v) {
            super(v);
            moviesLayout = (LinearLayout) v.findViewById(R.id.movies_layout);
            movieTitle = (TextView) v.findViewById(R.id.title);
            data = (TextView) v.findViewById(R.id.releaseDate);
            movieDescription = (TextView) v.findViewById(R.id.description);
            rating = (TextView) v.findViewById(R.id.rating);
            language = (TextView) v.findViewById(R.id.language);
            poster = (ImageView) v.findViewById(R.id.poster);
        }
    }

    public MovieAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.data.setText(movies.get(position).getReleaseDate());
        holder.movieDescription.setText(movies.get(position).getOverview());
        holder.rating.setText(movies.get(position).getVoteAverage().toString());
        holder.language.setText(movies.get(position).getOriginalLanguage().toString().toUpperCase());

        Glide.with(context).load("http://image.tmdb.org/t/p/w185/"+movies.get(position).getPosterPath())
             .apply(new RequestOptions()
             .fitCenter()
             .override(600,400)).into(holder.poster);

        ((MovieViewHolder)holder).moviesLayout.setOnClickListener(getMovieClickListener(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    View.OnClickListener getMovieClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MovieInfo.class);
                Log.d("movie_clicked_id", movies.get(position).getId().toString());
                intent.putExtra("Movie_Id", movies.get(position).getId().toString());
                intent.putExtra("title", movies.get(position).getTitle());
                view.getContext().startActivity(intent);
            }
        };
    }

}