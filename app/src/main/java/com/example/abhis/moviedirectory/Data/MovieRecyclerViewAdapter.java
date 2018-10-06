package com.example.abhis.moviedirectory.Data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhis.moviedirectory.Activities.Movie_Details;
import com.example.abhis.moviedirectory.Model.Movie;
import com.example.abhis.moviedirectory.R;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>{

    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {
        this.context = context;
        movieList = movies;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewAdapter.ViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        String posterLink = movie.getPoster();

        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getMovieType());

        it.sephiroth.android.library.picasso.Picasso.with(context).load(posterLink).placeholder(android.R.drawable.ic_menu_compass).into(holder.poster);

        holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView poster;
        TextView year;
        TextView type;

        public ViewHolder(View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

             title = itemView.findViewById(R.id.movieTitleID);
             poster = itemView.findViewById(R.id.movieImageID);
             year = itemView.findViewById(R.id.movieReleaseID);
             type = itemView.findViewById(R.id.movieCatID);

             itemView.setOnClickListener(new View.OnClickListener(){

                 @Override
                 public void onClick(View view) {

                     Movie movie = movieList.get(getAdapterPosition());

                     Intent intent = new Intent(context, Movie_Details.class);

                     intent.putExtra("movie", movie);

                     ctx.startActivity(intent);
                 }
             });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
