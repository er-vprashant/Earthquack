package indoar.co.earthquack;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static int EARTHQUAKE_ID=1;

    private static String URL_API = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=50";
    private static String TAG = MainActivity.class.getSimpleName();

    ArrayList<earthquack> earthquakeList = new ArrayList<>();
    earthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquacklist);

        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();

            new jsonConnect().execute(URL_API);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mAdapter = new earthquakeAdapter(this, earthquakeList);

        earthquakeListView.setAdapter(mAdapter);
    }

    private final class jsonConnect extends AsyncTask<String,Void,ArrayList<earthquack>> {


        private jsonConnect() {
        }

        @Override
        protected ArrayList<earthquack> doInBackground(String... strings) {
            Log.i(TAG, "doInBackground: "+strings[0]);
            return fetchEarthquakeData(strings[0]);
        }


        public  ArrayList<earthquack> fetchEarthquakeData(String url){
            URL requestUrl =null;
            String jsonresp=null;
            requestUrl=createURL(url);
            try {
                jsonresp = makeHttpConnection(requestUrl);
            } catch (IOException e) {
                Log.e(TAG, "fetchEarthquakeData: ", e);
            }
            return extractDataFromJson(jsonresp);
        }


        private  URL createURL(String url){

            URL requestUrl =null;

            try
            {
                requestUrl= new URL(url);
                Log.i(TAG, "createURL: "+requestUrl);
            }
            catch (Exception e)
            {
                Log.e(TAG, "createURL: ",e );
            }

            return requestUrl;

        }

        private String makeHttpConnection(URL url) throws IOException{

            String jsonResponse="";

            Log.i(TAG, "makeHttpConnection: "+url);

            URL test = new URL("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=50");
            HttpURLConnection urlConnection=null;
            InputStream inputStream= null;
            try
            {
                urlConnection=(HttpURLConnection)test.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setReadTimeout(3000);
                urlConnection.setConnectTimeout(3000);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                Log.i(TAG, "makeHttpConnection: connection successful");

                    inputStream=urlConnection.getInputStream();
                    jsonResponse= readFromInputStream(inputStream);
                    Log.i(TAG, "makeHttpConnection: "+urlConnection.getResponseCode());
            }
            catch (Exception e)
            {
                Log.e(TAG, "makeHttpConnection: ", e);
            }
            finally {
                if(urlConnection!=null)
                {
                    urlConnection.disconnect();
                }
                if(inputStream!=null)
                {
                    inputStream.close();
                }

            }

            return jsonResponse;

        }

        private  String readFromInputStream(InputStream inputStream) throws IOException
        {
            StringBuilder output= new StringBuilder();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);

            String line = br.readLine();
            while (line!=null)
            {
                output.append(line);
                line=br.readLine();
            }

            return output.toString();
        }

        private  ArrayList<earthquack> extractDataFromJson(String stringJson)
        {
            Log.i(TAG, "extractDataFromJson: json string returns null" + stringJson);
            if(stringJson==null){
                Log.i(TAG, "extractDataFromJson: json string returns null");
                return null;
            }
            ArrayList<earthquack> earthquacksList = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(stringJson);
                JSONArray features = root.getJSONArray("features");
                Log.i(TAG, Integer.toString(features.length()));
                for (int i =0;i<features.length();i++)
                {
                    JSONObject properties = features.getJSONObject(i);
                    JSONObject data = properties.getJSONObject("properties");
                    Double rc = data.getDouble("mag");
                    Log.i("chech json",Double.toString(rc));
                    String pos =data.getString("place");
                    int time = data.getInt("time");
                    String url = data.getString("url");
                    earthquacksList.add(new earthquack(rc,pos,time));
                }


            } catch (JSONException e) {
                Log.e(TAG, "extractDataFromJson: ", e);
            }
            Log.i(TAG, "extractDataFromJson: "+earthquacksList);
            return earthquacksList;
        }
    }

}