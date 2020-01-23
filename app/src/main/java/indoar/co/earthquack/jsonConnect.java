package indoar.co.earthquack;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public final class jsonConnect extends AsyncTask<URL,String,ArrayList<earthquack>> {

    private static String TAG= jsonConnect.class.getSimpleName();

    private jsonConnect() {
    }

    @Override
    protected ArrayList<earthquack> doInBackground(URL... urls) {
        return fetchEarthquakeData(urls[0]);
    }

    public  ArrayList<earthquack> fetchEarthquakeData(URL url){

        String jsonresp=null;
        try {
            jsonresp = makeHttpConnection(url);
        } catch (IOException e) {
            Log.e(TAG, "fetchEarthquakeData: ", e);
        }
        return extractDataFromJson(jsonresp);
    }


    private static URL createURL(String url){

        URL requestUrl =null;

        try
        {
            requestUrl= new URL(url);
        }
        catch (Exception e)
        {
            Log.e(TAG, "createURL: ",e );
        }

        return requestUrl;

    }

    private static String makeHttpConnection(URL url) throws IOException{

        String jsonResponse="";

        Log.i(TAG, "makeHttpConnection: "+url);

        if(url==null)
        {
            return jsonResponse;
        }

        HttpURLConnection urlConnection=null;
        InputStream inputStream= null;
        try
        {
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            Log.i(TAG, "makeHttpConnection: connection successful");

            if(urlConnection.getResponseCode()==200)
            {
                inputStream=urlConnection.getInputStream();
                jsonResponse= readFromInputStream(inputStream);

            }
            else
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

    private static String readFromInputStream(InputStream inputStream) throws IOException
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

    private static ArrayList<earthquack> extractDataFromJson(String stringJson)
    {

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
