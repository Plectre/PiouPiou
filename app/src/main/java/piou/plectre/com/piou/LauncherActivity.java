package piou.plectre.com.piou;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.os.SystemClock.sleep;

public class LauncherActivity extends AppCompatActivity {

    public String URL = "https://pioupiou.fr/fr";
    public TextView tv_url;
    boolean localised = false;
    private double lati;
    private LocationListener ll;
    private LocationManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        ll = new CheckPosition(getApplicationContext());

        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

        // url des pioupiou
        tv_url = (TextView) findViewById(R.id.url_pioupiou);

        tv_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requette = URL;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requette));
                startActivity(intent);
            }
        });

        Thread chrono = new Thread() {
            public void run() {

                try {
                    sleep(4000);

                    Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    cookToast(getString(R.string.timeOut));
                } finally {

                }

            }
        };
        chrono.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onPause() {
        super.onPause();
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void cookToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

}
