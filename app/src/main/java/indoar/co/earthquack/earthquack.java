package indoar.co.earthquack;

public class earthquack {

    private double magnitude;
    private String location;
    private long time;

    public earthquack(double magnitude, String location, long time) {
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
    }

//TODO: add getters


    public String getMagnitude() {
        return Double.toString(magnitude);
    }

    public String getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }
}
