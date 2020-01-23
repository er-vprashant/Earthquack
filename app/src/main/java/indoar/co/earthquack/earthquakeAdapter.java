package indoar.co.earthquack;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class earthquakeAdapter extends ArrayAdapter<earthquack> {

    private static String TAG= earthquakeAdapter.class.getSimpleName();



    public earthquakeAdapter(@NonNull Context context, ArrayList<earthquack> earthquakes) {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listViewItem = convertView;
        if (listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, parent, false);
        }

        earthquack currentEarthquake = getItem(position);

        Log.i(TAG,currentEarthquake.getLocation());

        TextView rcValue = (TextView)listViewItem.findViewById(R.id.magnitude);
        rcValue.setText(currentEarthquake.getMagnitude());



        ArrayList<String> location = formatLocation(currentEarthquake.getLocation());
        TextView locationOffsetValue= (TextView)listViewItem.findViewById(R.id.location_offset);

        TextView primaryLocationValue= (TextView)listViewItem.findViewById(R.id.primary_location);


        if (location.size()!=1) {
            locationOffsetValue.setText(location.get(0) + " of");
            primaryLocationValue.setText(location.get(1));
        }



        TextView timeValue = (TextView)listViewItem.findViewById(R.id.time);
        String time = formatTime(currentEarthquake.getTime());
        timeValue.setText(time);


        TextView dateText = (TextView)listViewItem.findViewById(R.id.date);
        String date = formatDate(currentEarthquake.getTime());
        dateText.setText(date);

        return listViewItem;
    }

    private String formatDate(long date)
    {
        Date dateObj = new Date(date);
        SimpleDateFormat sdf= new SimpleDateFormat("MMM d yyyy");
        String fDate =sdf.format(dateObj);
        Log.i(TAG, "formatDate: "+fDate);
        return fDate;
    }

    private String formatTime(long date)
    {
        Date dateObj = new Date(date);
        SimpleDateFormat sdf= new SimpleDateFormat("h:mm a");
        String fTime =sdf.format(dateObj);
        Log.i(TAG, "formatDate: "+fTime);
        return fTime;
    }

    private  ArrayList<String> formatLocation(String location)
    {
        ArrayList<String> pos = new ArrayList<>();
        String [] str = location.split(" of ");
        for (String a: str)
            pos.add(a);
        return pos;
    }
}
