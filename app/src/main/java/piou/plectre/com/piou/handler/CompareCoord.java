package piou.plectre.com.piou.handler;

import piou.plectre.com.piou.request.ApiRequestIdS;

/**
 * Created by El Di@blo on 20/05/2017.
 */

public class CompareCoord {

    private double clientLatitude = 44.0020;
    private double clientLongitude = -0.2323;
    private double piouLatitude;
    private double piouLongitude;
    private double distance;


    public CompareCoord() {

        ApiRequestIdS api = new ApiRequestIdS();
        this.piouLatitude = api.getLatitude();
        this.piouLongitude = api.getLongitude();

    }

    public void recupCoorPiou(double latitude, double longitude) {

        this.piouLatitude = latitude;
        this.piouLongitude = longitude;

    }

    public void recupCoorClient(double latitude, double longitude) {

        this.clientLatitude = latitude;
        this.clientLongitude = longitude;

    }

    private double convertRad(double input) {

        return (Math.PI * input / 180);
    }

    public int Compare() {
        double R = 637800;

        // convertion en radiants
        double lat_2 = convertRad(clientLatitude);
        double lon_2 = convertRad(clientLongitude);
        double lat_1 = convertRad(piouLatitude);
        double lon_1 = convertRad(piouLongitude);

       double d = R * (Math.PI / 2 - Math.asin(Math.sin(lat_2) * Math.sin(lat_1) +
                Math.cos(lon_2 - lon_1) *
                        Math.cos(lat_2) * Math.cos(lat_1)));

        int distance = (int) d;
        return Math.round(distance) / 100;
    }
}
