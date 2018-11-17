package com.sparrowpaul.weatherdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView tempTextView, date, city, weatherType; //initializing the views
    private ImageView imageView;
    String url = "http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=6cbee30e3b0163be5df22eda4a538e66&units=metric";
    // the above uel is our weather apt

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();  // finding the views using function

        getWeather(); // getting the weather data using function

    }

    // end of main function our declared function is below

    private void initViews() { // initializing the views using find view by id
        tempTextView = findViewById(R.id.temperatureTextViewID);
        date = findViewById(R.id.dateTextViewID);
        city = findViewById(R.id.cityTextViewID);
        weatherType = findViewById(R.id.weatherTypeTextViewID);
        imageView = findViewById(R.id.imageView);
    }

    private void getWeather() { // function to get weather data

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main"); // calling it from the open weather json api
                    JSONArray array = response.getJSONArray("weather"); // calling weather elements into an array
                    JSONObject object = array.getJSONObject(0); // getting the first element of the weather array
                    String temp = String.valueOf(main_object.getDouble("temp")); //converting the double to a string
                    String discription = object.getString("description");//getting the description
                    String city2 = response.getString("name"); //getting the name of the city

                    // setting them to their respective views
                    city.setText(city2);
                    weatherType.setText(discription);
                    tempTextView.setText(temp);

                    // creating an instance of the current date and formatting it
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
                    String formatted_date = sdf.format(calendar.getTime());

                    date.setText(formatted_date);

                } catch (JSONException e) { //catching unexpected error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        //adding json obj to the request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuRefreshID){
            getWeather();
            Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menuExitID){
            this.finish();
            System.exit(1);
        }

        return super.onOptionsItemSelected(item);
    }
}
