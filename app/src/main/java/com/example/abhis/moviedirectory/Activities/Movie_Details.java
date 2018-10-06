package com.example.abhis.moviedirectory.Activities;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abhis.moviedirectory.Model.Movie;
import com.example.abhis.moviedirectory.R;
import com.example.abhis.moviedirectory.Util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.sephiroth.android.library.picasso.Picasso;

public class Movie_Details extends AppCompatActivity {

    private Movie movie;
    private TextView movieTitle;
    private ImageView movieImage;
    private TextView movieYear;
    private TextView director;
    private TextView actors;
    private TextView category;
    private TextView rating;
    private TextView writers;
    private TextView plot;
    private TextView boxOffice;
    private TextView runTime;

    private RequestQueue queue;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);

        queue = Volley.newRequestQueue(this);

        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieId = movie.getImdbId();

        //setUpUI();
        getMovieDetails(movieId);
    }

    private void setUpUI()
    {
        movieTitle = findViewById(R.id.movieTitleIDDets);
        movieImage = findViewById(R.id.movieImageIDDets);
        movieYear = findViewById(R.id.movieReleaseIDDets);
        director = findViewById(R.id.directedByDets);
        category = findViewById(R.id.movieCatIDDets);
        rating = findViewById(R.id.movieRatingIDDets);
        plot = findViewById(R.id.plotDets);
        boxOffice = findViewById(R.id.boxOfficeDets);
        runTime = findViewById(R.id.runtimeDets);
        actors = findViewById(R.id.actorsDets);
    }

    private void getMovieDetails(String id)
    {
        movieTitle = findViewById(R.id.movieTitleIDDets);
        movieImage = findViewById(R.id.movieImageIDDets);
        movieYear = findViewById(R.id.movieReleaseIDDets);
        director = findViewById(R.id.directedByDets);
        category = findViewById(R.id.movieCatIDDets);
        rating = findViewById(R.id.movieRatingIDDets);
        plot = findViewById(R.id.plotDets);
        boxOffice = findViewById(R.id.boxOfficeDets);
        runTime = findViewById(R.id.runtimeDets);
        actors = findViewById(R.id.actorsDets);

        String URL = Constants.URL + id + Constants.API_KEY;
        Log.d("url", URL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.has("Ratings"))
                            {
                                JSONArray ratings = response.getJSONArray("Ratings");

                                String source = null;
                                String value = null;

                                if(ratings.length() > 0)
                                {
                                    int x = ratings.length();
                                    JSONObject mRatings = ratings.getJSONObject(x = 1);
                                    source = mRatings.getString("Source");
                                    value = mRatings.getString("Value");

                                    rating.setText(source + ": " + value);
                                }
                                else {
                                    rating.setText("Rating: N/A");
                                }


                            }
                            it.sephiroth.android.library.picasso.Picasso.with(getApplicationContext()).load(response.getString("Poster")).into(movieImage);

                            movieTitle.setText(response.getString("Title"));
                            movieYear.setText(response.getString("Year"));
                            director.setText(response.getString("Director"));
                            writers.setText(response.getString("Writers"));
                            plot.setText(response.getString("Plot"));
                            runTime.setText(response.getString("Runtime"));
                            actors.setText(response.getString("Actors"));
                            boxOffice.setText("Box Office:" + response.getString("BoxOffice"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d("Error" + error.getMessage());

            }
        });

        queue.add(jsonObjectRequest);
    }
}
