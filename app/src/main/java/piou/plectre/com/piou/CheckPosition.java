package piou.plectre.com.piou;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import piou.plectre.com.piou.handler.CompareCoord;

/**
 * Created by El Di@blo on 17/05/2017.
 */

public class CheckPosition extends Service implements LocationListener {
    private double latitude;
    private double longitude;
    private Context context;
    private boolean inBound = false;


    public CheckPosition(Context context) {
        this.context = context.getApplicationContext();
    }


    @Override
    public void onLocationChanged(Location location) {
        // Localisation une seule fois de la position du client
        if (!inBound) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i("APP", String.valueOf(latitude));
            CompareCoord cc = new CompareCoord();
            cc.recupCoorClient(latitude, longitude);
            inBound = true;

        } else {

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "La localisation n'est pas activée ;=) ", Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void toCompare() {

        if (latitude != 0.0) {
            Log.i("APP", "pas de coordonnées valident");
            CompareCoord cc = new CompareCoord();
        } else {
            Log.i("APP", "pas de coordonnées non valident");
        }
    }


}

