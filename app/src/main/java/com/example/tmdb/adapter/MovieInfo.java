package com.example.tmdb.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tmdb.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Locale;

import static com.example.tmdb.MainActivity.API_Key;

public class MovieInfo extends AppCompatActivity {
    private static final String TAG= MovieInfo.class.getSimpleName();
    private RequestQueue rQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.setTitle(getIntent().getStringExtra("title"));
        final ImageView backdrop = findViewById(R.id.infobackdrop);
        final TextView infoDesc = findViewById(R.id.infodescription);
        final TextView runtime = findViewById(R.id.time);
        final TextView releaseDate = findViewById(R.id.releaseDate);
        final TextView rating = findViewById(R.id.rating);
        final TextView lan = findViewById(R.id.infolanguage);
        final TextView genre = findViewById(R.id.genre);
        final TextView budget = findViewById(R.id.budget);
        final TextView revenue = findViewById(R.id.revenue);
        final String movieId = getIntent().getStringExtra("Movie_Id");
        //Toast.makeText(getApplicationContext(), movieId, Toast.LENGTH_LONG).show();
        String callUrl = "http://api.themoviedb.org/3/movie/"+movieId+"?api_key="+API_Key;
        try{
            StringRequest request = new StringRequest(Request.Method.GET, callUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    System.out.println(TAG+"Response " + s);
                    JSONObject obj=null;
                    try {
                        obj = new JSONObject(s);
                        Glide.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+obj.getString("backdrop_path"))
                                .apply(new RequestOptions()
                                        .centerCrop().override(1200,1024)).into(backdrop);
                        infoDesc.setText(obj.getString("overview"));
                        runtime.setText(obj.getString("runtime")+" minutes");
                        releaseDate.setText(obj.getString("release_date"));
                        rating.setText(obj.getString("vote_average"));
                        lan.setText(obj.getString("original_language").toUpperCase());
                        //currency format
                        Locale locale = new Locale("en", "US");
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                        budget.setText(currencyFormatter.format(Long.parseLong(obj.getString("budget"))));
                        revenue.setText(currencyFormatter.format(Long.parseLong(obj.getString("revenue"))));
                        String gen=null;
                        JSONArray jsonarr = obj.getJSONArray("genres");
                        for(int i=0; i<jsonarr.length(); i++){
                            Object jo =(JSONObject) jsonarr.get(i);
                            if(gen==null){
                                gen = ((JSONObject) jo).getString("name");
                            }else{
                                gen = gen+", "+ ((JSONObject) jo).getString("name");
                            }
                        }
                        genre.setText(gen);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG+"Error", volleyError.toString());
                }
            });
            getRequestQueue().add(request);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public RequestQueue getRequestQueue() {
        if (rQueue == null) {
            rQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return rQueue;
    }
}
