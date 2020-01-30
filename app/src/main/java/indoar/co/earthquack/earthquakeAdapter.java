package indoar.co.earthquack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class earthquakeAdapter extends ArrayAdapter<earthquake> {

        private static String TAG =earthquakeAdapter.class.getSimpleName();
    // variables
    private static String LOCATION_SEPERATOR="of";

    //constructor
    public earthquakeAdapter(Context context, ArrayList<earthquake> resource) {
        super(context, 0,resource);
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View ItemListView = convertView;

        if (ItemListView == null) {
            ItemListView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_adapter, parent, false);
        }

        earthquake currentEarthquake = getItem(position);

        //magnitude textVeiw
        TextView magnitudeView = (TextView) ItemListView.findViewById(R.id.magnitude);
        magnitudeView.setText(magnitudeFormator(currentEarthquake.getmMagnitude()));


        //TODO: color is not changing according to the magnitude
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor =  getMagnitudeColor(currentEarthquake.getmMagnitude());

        magnitudeCircle.setColor(magnitudeColor);



        //location View
        String originalLocation = currentEarthquake.getmLocation();
        String locationOffset;
        String primaryLocation;

        if(originalLocation.contains(LOCATION_SEPERATOR))
        {
            String parts[] = originalLocation.split(LOCATION_SEPERATOR);
            locationOffset=parts[0]+LOCATION_SEPERATOR;
            primaryLocation= parts[1];
        }
        else{
            locationOffset="Near the";
            primaryLocation=originalLocation;
        }
        TextView primaryLocationView = (TextView) ItemListView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);
        TextView locationOffsetView = (TextView) ItemListView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);

        //dateView
        Date dateObject = new Date(currentEarthquake.getmTimeInMilliSec());

        TextView dateView = (TextView) ItemListView.findViewById(R.id.date);
        dateView.setText(dateFormat(dateObject));
        TextView timeView = (TextView) ItemListView.findViewById(R.id.time);
        timeView.setText(timeFormat(dateObject));


        return ItemListView;
    }


    private String magnitudeFormator(double magnitude)
    {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(magnitude);
    }

    private String  dateFormat(Date dateObject)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("LLL dd, yyyy");
        return sdf.format(dateObject);
    }
    private String  timeFormat(Date dateObject)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        return sdf.format(dateObject);
    }

    private int getMagnitudeColor(double magnitude)
    {
        int colorResourceId=0;
        int mag= (int) Math.floor(magnitude);

        switch (mag){
            case 1:
                colorResourceId= R.color.magnitude1;
                break;
            case 2:
                colorResourceId= R.color.magnitude2;
                break;
            case 3:
                colorResourceId= R.color.magnitude3;
                break;
            case 4:
                colorResourceId= R.color.magnitude4;
                break;
            case 5:
                colorResourceId= R.color.magnitude5;
                break;
            case 6:
                colorResourceId= R.color.magnitude6;
                break;
            case 7:
                colorResourceId= R.color.magnitude7;
                break;
            case 8:
                colorResourceId= R.color.magnitude8;
                break;
            case 9:
                colorResourceId= R.color.magnitude9;
                break;
            case 10:
                colorResourceId= R.color.magnitude10plus;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mag);
        }
        return ContextCompat.getColor(getContext(), colorResourceId);
    }
}
