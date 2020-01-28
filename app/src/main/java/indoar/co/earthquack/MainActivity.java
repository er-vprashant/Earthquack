package indoar.co.earthquack;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<earthquake>>, SharedPreferences.OnSharedPreferenceChangeListener {

    //initialization
    private static String TAG =MainActivity.class.getSimpleName();
    private earthquakeAdapter mAdapter;
    private ArrayList<earthquake> earthquakeArrayList = new ArrayList<>();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquacklist);

        //list view for earthquake data
        ListView earthquakeListView = (ListView)findViewById(R.id.list);

        //view when list is empty
        TextView mEmptyStateTextView=(TextView)findViewById(R.id.empty_text_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        //shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        //onclick for earthquake list view
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                earthquake currentItem = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentItem.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,earthquakeUri);
                startActivity(webIntent);
            }
        });

        //checking for internet connectivity

        ConnectivityManager connectivityManager= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
        {
            android.app.LoaderManager loaderManager =getLoaderManager();
            loaderManager.initLoader(1,null,this);
        }
        else
        {
            View loaderIndicator = findViewById(R.id.loader_indicator);
            loaderIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText("No internet connection");
        }


        String jsonResp ="{\"type\":\"FeatureCollection\",\"metadata\":{\"generated\":1579752816000,\"url\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=50\",\"title\":\"USGS Earthquakes\",\"status\":200,\"api\":\"1.8.1\",\"limit\":50,\"offset\":1,\"count\":12},\"features\":[{\"type\":\"Feature\",\"properties\":{\"mag\":7.2000000000000002,\"place\":\"88km N of Yelizovo, Russia\",\"time\":1454124312220,\"updated\":1539812932814,\"tz\":720,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us20004vvx\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us20004vvx&format=geojson\",\"felt\":3,\"cdi\":3.3999999999999999,\"mmi\":5.7999999999999998,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":799,\"net\":\"us\",\"code\":\"20004vvx\",\"ids\":\",gcmt20160130032510,at00o1qxho,pt16030050,us20004vvx,gcmt20160130032512,atlas20160130032512,\",\"sources\":\",gcmt,at,pt,us,gcmt,atlas,\",\"types\":\",associate,cap,dyfi,finite-fault,general-text,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":0.95799999999999996,\"rms\":1.1899999999999999,\"gap\":17,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 7.2 - 88km N of Yelizovo, Russia\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[158.5463,53.977600000000002,177]},\"id\":\"us20004vvx\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.0999999999999996,\"place\":\"94km SSE of Taron, Papua New Guinea\",\"time\":1453777820750,\"updated\":1478815803221,\"tz\":600,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us20004uks\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us20004uks&format=geojson\",\"felt\":null,\"cdi\":null,\"mmi\":4.0999999999999996,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":572,\"net\":\"us\",\"code\":\"20004uks\",\"ids\":\",gcmt20160126031023,us20004uks,gcmt20160126031020,\",\"sources\":\",gcmt,us,gcmt,\",\"types\":\",associate,cap,geoserve,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":1.5369999999999999,\"rms\":0.73999999999999999,\"gap\":25,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.1 - 94km SSE of Taron, Papua New Guinea\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[153.24539999999999,-5.2952000000000004,26]},\"id\":\"us20004uks\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.2999999999999998,\"place\":\"50km NNE of Al Hoceima, Morocco\",\"time\":1453695722730,\"updated\":1507756883299,\"tz\":0,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004gy9\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004gy9&format=geojson\",\"felt\":117,\"cdi\":7.2000000000000002,\"mmi\":5.2999999999999998,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":695,\"net\":\"us\",\"code\":\"10004gy9\",\"ids\":\",gcmt20160125042203,us10004gy9,gcmt20160125042202,atlas20160125042202,\",\"sources\":\",gcmt,us,gcmt,atlas,\",\"types\":\",associate,cap,dyfi,geoserve,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":2.2010000000000001,\"rms\":0.92000000000000004,\"gap\":20,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.3 - 50km NNE of Al Hoceima, Morocco\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-3.6818,35.649299999999997,12]},\"id\":\"us10004gy9\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":7.0999999999999996,\"place\":\"86km E of Old Iliamna, Alaska\",\"time\":1453631430230,\"updated\":1558419847696,\"tz\":-540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004gqp\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004gqp&format=geojson\",\"felt\":1816,\"cdi\":7.2000000000000002,\"mmi\":6.5,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":1496,\"net\":\"us\",\"code\":\"10004gqp\",\"ids\":\",ak12496371,at00o1gd6r,us10004gqp,ak01613v15nv,atlas20160124103030,atlas20160124103029,\",\"sources\":\",ak,at,us,ak,atlas,atlas,\",\"types\":\",associate,cap,dyfi,finite-fault,general-text,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,trump-origin,\",\"nst\":null,\"dmin\":0.71999999999999997,\"rms\":2.1099999999999999,\"gap\":19,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 7.1 - 86km E of Old Iliamna, Alaska\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-153.4051,59.636299999999999,129]},\"id\":\"us10004gqp\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.5999999999999996,\"place\":\"215km SW of Tomatlan, Mexico\",\"time\":1453399617650,\"updated\":1478815764127,\"tz\":-420,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004g4l\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004g4l&format=geojson\",\"felt\":11,\"cdi\":2.7000000000000002,\"mmi\":3.9199999999999999,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":673,\"net\":\"us\",\"code\":\"10004g4l\",\"ids\":\",gcmt20160121180659,at00o1bebo,pt16021050,us10004g4l,gcmt20160121180657,\",\"sources\":\",gcmt,at,pt,us,gcmt,\",\"types\":\",associate,cap,dyfi,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":2.4129999999999998,\"rms\":0.97999999999999998,\"gap\":74,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.6 - 215km SW of Tomatlan, Mexico\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-106.9337,18.823899999999998,10]},\"id\":\"us10004g4l\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.7000000000000002,\"place\":\"52km SE of Shizunai, Japan\",\"time\":1452741933640,\"updated\":1579648251551,\"tz\":540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004ebx\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004ebx&format=geojson\",\"felt\":51,\"cdi\":5.7999999999999998,\"mmi\":6.0199999999999996,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":720,\"net\":\"us\",\"code\":\"10004ebx\",\"ids\":\",us10004ebx,gcmt20160114032534,at00o0xauk,pt16014050,gcmt20160114032533,atlas20160114032533,\",\"sources\":\",us,gcmt,at,pt,gcmt,atlas,\",\"types\":\",associate,cap,dyfi,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":0.28100000000000003,\"rms\":0.97999999999999998,\"gap\":22,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.7 - 52km SE of Shizunai, Japan\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[142.78100000000001,41.972299999999997,46]},\"id\":\"us10004ebx\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.0999999999999996,\"place\":\"12km WNW of Charagua, Bolivia\",\"time\":1452741928270,\"updated\":1478815697357,\"tz\":-240,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004ebw\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004ebw&format=geojson\",\"felt\":3,\"cdi\":2.2000000000000002,\"mmi\":2.21,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":573,\"net\":\"us\",\"code\":\"10004ebw\",\"ids\":\",us10004ebw,gcmt20160114032528,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,dyfi,geoserve,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":5.492,\"rms\":1.04,\"gap\":16,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.1 - 12km WNW of Charagua, Bolivia\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-63.328800000000001,-19.759699999999999,582.55999999999995]},\"id\":\"us10004ebw\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.2000000000000002,\"place\":\"74km NW of Rumoi, Japan\",\"time\":1452532083920,\"updated\":1578008366978,\"tz\":540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004djn\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004djn&format=geojson\",\"felt\":8,\"cdi\":3.3999999999999999,\"mmi\":3.7970000000000002,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":594,\"net\":\"us\",\"code\":\"10004djn\",\"ids\":\",us10004djn,gcmt20160111170803,\",\"sources\":\",us,gcmt,\",\"types\":\",cap,dyfi,geoserve,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":1.139,\"rms\":0.95999999999999996,\"gap\":33,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.2 - 74km NW of Rumoi, Japan\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[141.08670000000001,44.476100000000002,238.81]},\"id\":\"us10004djn\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.5,\"place\":\"227km SE of Sarangani, Philippines\",\"time\":1452530285900,\"updated\":1507756804527,\"tz\":480,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004dj5\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004dj5&format=geojson\",\"felt\":1,\"cdi\":2.7000000000000002,\"mmi\":7.5,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":1,\"sig\":650,\"net\":\"us\",\"code\":\"10004dj5\",\"ids\":\",gcmt20160111163807,at00o0srjp,pt16011050,us10004dj5,gcmt20160111163805,atlas20160111163805,\",\"sources\":\",gcmt,at,pt,us,gcmt,atlas,\",\"types\":\",associate,cap,dyfi,geoserve,impact-link,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":3.1440000000000001,\"rms\":0.71999999999999997,\"gap\":22,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.5 - 227km SE of Sarangani, Philippines\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[126.8621,3.8965000000000001,13]},\"id\":\"us10004dj5\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6,\"place\":\"Pacific-Antarctic Ridge\",\"time\":1451986454620,\"updated\":1478815631921,\"tz\":-540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004bgk\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004bgk&format=geojson\",\"felt\":0,\"cdi\":1,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":554,\"net\":\"us\",\"code\":\"10004bgk\",\"ids\":\",gcmt20160105093415,us10004bgk,gcmt20160105093414,\",\"sources\":\",gcmt,us,gcmt,\",\"types\":\",associate,cap,dyfi,geoserve,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":30.75,\"rms\":0.67000000000000004,\"gap\":71,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.0 - Pacific-Antarctic Ridge\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[-136.2603,-54.290599999999998,10]},\"id\":\"us10004bgk\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.7000000000000002,\"place\":\"30km W of Imphal, India\",\"time\":1451862322270,\"updated\":1562693393711,\"tz\":330,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004b2n\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004b2n&format=geojson\",\"felt\":974,\"cdi\":7.5999999999999996,\"mmi\":6.7999999999999998,\"alert\":\"orange\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":1740,\"net\":\"us\",\"code\":\"10004b2n\",\"ids\":\",us10004b2n,gcmt20160103230522,atlas20160103230522,\",\"sources\":\",us,gcmt,atlas,\",\"types\":\",cap,dyfi,general-text,geoserve,impact-text,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,tectonic-summary,\",\"nst\":null,\"dmin\":1.794,\"rms\":1.01,\"gap\":16,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.7 - 30km W of Imphal, India\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[93.650499999999994,24.803599999999999,55]},\"id\":\"us10004b2n\"},\n" +
                "{\"type\":\"Feature\",\"properties\":{\"mag\":6.2999999999999998,\"place\":\"Western Indian-Antarctic Ridge\",\"time\":1451613639950,\"updated\":1478815605862,\"tz\":540,\"url\":\"https://earthquake.usgs.gov/earthquakes/eventpage/us10004ant\",\"detail\":\"https://earthquake.usgs.gov/fdsnws/event/1/query?eventid=us10004ant&format=geojson\",\"felt\":0,\"cdi\":1,\"mmi\":0,\"alert\":\"green\",\"status\":\"reviewed\",\"tsunami\":0,\"sig\":611,\"net\":\"us\",\"code\":\"10004ant\",\"ids\":\",gcmt20160101020039,us10004ant,gcmt20160101020040,\",\"sources\":\",gcmt,us,gcmt,\",\"types\":\",associate,cap,dyfi,geoserve,losspager,moment-tensor,nearby-cities,origin,phase-data,shakemap,\",\"nst\":null,\"dmin\":9.3610000000000007,\"rms\":0.88,\"gap\":19,\"magType\":\"mww\",\"type\":\"earthquake\",\"title\":\"M 6.3 - Western Indian-Antarctic Ridge\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[139.44890000000001,-50.557499999999997,10]},\"id\":\"us10004ant\"}],\"bbox\":[-153.4051,-54.2906,10,158.5463,59.6363,582.56]}";
        try {
            JSONObject root = new JSONObject(jsonResp);
            JSONArray features = root.getJSONArray("features");

            double mag;
            String location;
            long time;
            String url;

            for(int i =0;i<features.length();i++)
            {
                JSONObject currentItem = features.getJSONObject(i);
                JSONObject properties = currentItem.getJSONObject("properties");
                double mMagnitude = properties.getDouble("mag");
                String mLocation = properties.getString("place");
                long mTime = properties.getLong("time");
                String mUrl  = properties.getString("url");
                earthquakeArrayList.add(new earthquake(mMagnitude,mLocation,mTime,mUrl));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter = new earthquakeAdapter(this, earthquakeArrayList);
        earthquakeListView.setAdapter(mAdapter);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public Loader<ArrayList<earthquake>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<earthquake>> loader, ArrayList<earthquake> data) {

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<earthquake>> loader) {

    }
}