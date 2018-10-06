package com.example.abhis.moviedirectory.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.abhis.moviedirectory.Data.MovieRecyclerViewAdapter;
import com.example.abhis.moviedirectory.Model.Movie;
import com.example.abhis.moviedirectory.R;
import com.example.abhis.moviedirectory.Util.Constants;
import com.example.abhis.moviedirectory.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.abhis.moviedirectory.R.layout.activity_main);

        queue= Volley.newRequestQueue(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();

        movieList = new ArrayList<>();

        movieList = getMovies(search);

        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();
    }

    public List<Movie> getMovies(String searchTerm)
    {
        movieList.clear();

        String url = Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT + Constants.API_KEY;
        //String url = "http://www.omdbapi.com/?s=Batman&page=2&apikey=978ebe17";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray moviesArray = response.getJSONArray("Search");
                            for (int i=0; i < moviesArray.length(); i++)
                            {
                                JSONObject moviesObj = moviesArray.getJSONObject(i);

                                Movie movie = new Movie();
                                movie.setTitle(moviesObj.getString("Title"));
                                movie.setYear("Year Released: " + moviesObj.getString("Year"));
                                movie.setMovieType("Type: " + moviesObj.getString("Type"));
                                movie.setPoster(moviesObj.getString("Poster"));
                                movie.setImdbId(moviesObj.getString("imdbID"));

                                Log.d("Movies", movie.getTitle());

                                movieList.add(movie);
                            }

                            movieRecyclerViewAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("error" , e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);

        return movieList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.new_search)
        {
            showInputDialog();
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showInputDialog()
    {
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        final EditText newSearchEdit = view.findViewById(R.id.searchEdit);
        Button submitButton = view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prefs prefs = new Prefs(MainActivity.this);

                if (!newSearchEdit.getText().toString().isEmpty())
                {
                    String search = newSearchEdit.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();

                    getMovies(search);

                    //movieRecyclerViewAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
    }
}
