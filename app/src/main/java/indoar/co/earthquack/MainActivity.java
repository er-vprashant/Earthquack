package indoar.co.earthquack;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.MissingFormatArgumentException;

public class MainActivity extends AppCompatActivity {

    //initialization
    private static String TAG = MainActivity.class.getSimpleName();
    private earthquakeAdapter mAdapter;
    private ArrayList<earthquake> earthquakeArrayList = new ArrayList<>();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private TextView mEmptyStateTextView;
    private ProgressDialog ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquacklist);

        //list view for earthquake data
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);



        //onclick for earthquake list view
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                earthquake currentItem = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentItem.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(webIntent);
            }
        });

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10&minmag=1&limit=30";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (TextUtils.isEmpty(response)) {
                                earthquakeArrayList = null;
                            }
                            try {
                                JSONObject baseJsonResponse = new JSONObject(response);
                                JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
                                for (int i = 0; i < earthquakeArray.length(); i++) {
                                    JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                                    JSONObject properties = currentEarthquake.getJSONObject("properties");
                                    double magnitude = properties.getDouble("mag");
                                    String location = properties.getString("place");
                                    long time = properties.getLong("time");
                                    String url = properties.getString("url");
                                    earthquake earthquakeitem = new earthquake(magnitude, location, time, url);
                                    earthquakeArrayList.add(earthquakeitem);
                                }

                            } catch (JSONException e) {
                                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
                            }
                            finally {
                                ps.hide();
                                mAdapter = new earthquakeAdapter(MainActivity.this, earthquakeArrayList);
                                earthquakeListView.setAdapter(mAdapter);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ps.hide();
                    Log.i(TAG, "onErrorResponse: volly not working");
                }
            });
            queue.add(stringRequest);
            queue.start();
            ps = new ProgressDialog(MainActivity.this);
            ps.setMessage("fetching data");
            ps.show();



    }
}