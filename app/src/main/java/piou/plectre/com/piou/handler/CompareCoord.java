package piou.plectre.com.piou.handler;


/**
 * Created by El Di@blo on 20/05/2017.
 * calcul en km de la distance separant les deux coordonnées
 */

public class CompareCoord {

    private double clientLatitude;
    private double clientLongitude;
    private double piouLatitude;
    private double piouLongitude;


    public void recupCoorPiou(double latitude, double longitude) {

        this.piouLatitude = latitude;
        this.piouLongitude = longitude;

    }

    public void recupCoorClient(double latitude, double longitude) {

        this.clientLatitude = latitude;
        this.clientLongitude = longitude;

    }

    /**
     * Fonction qui calcule la disatance entre le piou
     * et le client
     */
    public String Compare() {

        // convertion en radiants
        double lat_1 = Math.PI * (clientLatitude / 180);
        double lon_1 = Math.PI * (clientLongitude / 180);
        double lat_2 = Math.PI * (piouLatitude / 180);
        double lon_2 = Math.PI * (piouLongitude / 180);

        double theta = clientLongitude - piouLongitude;
        double rtheta = Math.PI * theta / 180;


        double d = Math.sin(lat_1) * Math.sin(lat_2) + Math.cos(lon_1) * Math.cos(lon_2) * Math.cos(rtheta);
        d = Math.acos(d);
        d = Math.ceil(d * 180 / Math.PI);
        d = d * 60 * 1.1515;
        int di = (int) d;
        String distance = String.valueOf(di);
        return distance;

    }
}

