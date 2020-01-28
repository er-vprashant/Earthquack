package indoar.co.earthquack;

public class earthquake {


    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliSec;
    private String mUrl;


    public earthquake(double mMagnitude, String mLocation, long mTimeInMilliSec, String mUrl) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mTimeInMilliSec = mTimeInMilliSec;
        this.mUrl = mUrl;
    }


    //getter for earthquake datatype

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public long getmTimeInMilliSec() {
        return mTimeInMilliSec;
    }

    public String getmUrl() {
        return mUrl;
    }


    //setter for earthquake datatype
    public void setmMagnitude(double mMagnitude) {
        this.mMagnitude = mMagnitude;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmTimeInMilliSec(long mTimeInMilliSec) {
        this.mTimeInMilliSec = mTimeInMilliSec;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
