package indoar.co.earthquack;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class earthquakeHomepage extends AppCompatActivity
{

    //Initializations
    private static String TAG = earthquakeHomepage.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;

    private ProgressDialog ps;
    private earthquakeAdapter mAdapter;

    private ArrayList<earthquake> earthquakeArrayList = new ArrayList<>();
    ListView earthquakeListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_homepage);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //running progress dialog

        ps = new ProgressDialog(this);
        ps.setMessage("fetching data");
        ps.show();

        earthquakeData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.earthquake_homepage, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void earthquakeData() {

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
                        } finally {
                            ps.hide();
                            mAdapter = new earthquakeAdapter(earthquakeHomepage.this, earthquakeArrayList);
                            earthquakeListView = (ListView)findViewById(R.id.list_item_view);
                            earthquakeListView.setAdapter(mAdapter);

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

    }
}


