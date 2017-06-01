package piou.plectre.com.piou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import piou.plectre.com.piou.request.ApiRequestId;
import piou.plectre.com.piou.request.ApiRequestIdS;

public class MainActivity extends AppCompatActivity {


    private TextView tvWindAverage;
    private TextView tvHeading;
    private TextView tvName;
    private ImageView ivArrow;
    private ListView lvNames;
    private TextView tvLat;
    private TextView tvLon;
    private TextView tvHeure;
    private TextView tvJour;
    private LinearLayout coord;
    private TextView bandeauCoord;

    private RequestQueue queue;
    private ApiRequestId request;
    private ApiRequestIdS requestIds;
    private String name;
    private int winHeading;
    private double windAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coord = (LinearLayout) findViewById(R.id.ll_lat_lon);

        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLon = (TextView) findViewById(R.id.tvLon);
        tvHeure = (TextView) findViewById(R.id.tv_heure);
        tvJour = (TextView) findViewById(R.id.tv_jour);
        tvHeading = (TextView) findViewById(R.id.tv_Heading);
        tvName = (TextView) findViewById(R.id.tvName);
        bandeauCoord = (TextView) findViewById(R.id.tv_bandeau_coor);
        tvWindAverage = (TextView) findViewById(R.id.tv_average);

        ivArrow = (ImageView) findViewById(R.id.ivArrow);
        lvNames = (ListView) findViewById(R.id.lvNames);
        queue = MySingleton.getInstance(this).getRequestQueue();

        request = new ApiRequestId(queue, this);
        requestIds = new ApiRequestIdS(queue, this);

        bandeauCoord.setVisibility(View.INVISIBLE);
        coord.setVisibility(View.INVISIBLE);

        /* Requette passée à la class ApiRequestIds
           qui recupére tous les piou avec state à "on" et date du jour
           puis instancie la méthode CheckPiouIdsCallback qui permet d'implementer onSuccess et
           onError
         */
        requestIds.checkIdsPiou("all", new ApiRequestIdS.CheckPiouIdsCallbackNames() {


            // Callback de onSuccessNames
            @Override
            public void onSuccessIds(HashMap<String, String> names) {

                List<HashMap<String, String>> listItem = new ArrayList<>();

                SimpleAdapter sAdapter = new SimpleAdapter(MainActivity.this, listItem, R.layout.list_items,
                        new String[]{"First Line", "Second Line", "Kilometre"},
                        new int[]{R.id.item_1, R.id.item_2, R.id.item_3});

                Iterator it = names.entrySet().iterator();

                while (it.hasNext()) {
                    HashMap<String, String> result = new HashMap<>();
                    Map.Entry pair = (Map.Entry) it.next();
                    result.put("First Line", pair.getKey().toString());
                    result.put("Second Line", pair.getValue().toString());
                    //result.put("Kilometre", pair.getValue().toString());


                    listItem.add(result);

                }
                lvNames.setAdapter(sAdapter);


                lvNames.setOnItemClickListener(lvNames_listener);

            }


            // Gestion du Click sur un item de la list view
            AdapterView.OnItemClickListener lvNames_listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Decouper la chaine et recuperer l'id du pioupiou
                    // decouper avec "_" splitId[1] = id : splitId[3] = distance
                    Object ObjectId = parent.getItemAtPosition(position);
                    String strId = String.valueOf(ObjectId);

                    String[] splitId = strId.split("_");

                    //Log.i("APP", "distance : "+ String.valueOf(splitId[3]));
                    //Log.i("APP", "id : " + String.valueOf(splitId[1]));

                    // Envoyer la requette à l'api pioupiou
                    Request(splitId[1]);
                    String distance = (splitId[3]);
                    Toast.makeText(getBaseContext(), String.valueOf(distance)+" kms à vol de pioupiou ;=)", Toast.LENGTH_SHORT).show();
                    Log.i("APP", strId);
                }
            };

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });


        // recuperer les coordonnées et demarrage de Maps

        coord.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clicLongCoord();
                return true;
            }
        });

        bandeauCoord.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clicLongCoord();
                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void Request(String id) {
        request.checkIdPiou(id, new ApiRequestId.CheckPiouCallback() {
            @Override
            public void onSuccess(int pWinHeading,
                                  double pWindAverage,
                                  String pName,
                                  double latitude,
                                  double longitude,
                                  String currentTime,
                                  String currentDay) {
                Log.i("APP", currentTime);
                Log.i("APP", currentDay);
                name = pName;
                winHeading = pWinHeading;
                windAverage = pWindAverage;
                tvJour.setText(currentDay);
                tvHeure.setText(currentTime);
                tvHeading.setText("Vent du : " + String.valueOf(pWinHeading) + "°");
                tvWindAverage.setText("Vitesse : " + String.valueOf(pWindAverage) + " " +
                        getResources().getString(R.string.vitesseVent));
                tvName.setText(pName.toUpperCase());
                tvLat.setText(String.valueOf(latitude));
                tvLon.setText(String.valueOf(longitude));

                Rotation(pWinHeading);
                coord.setVisibility(View.VISIBLE);
                bandeauCoord.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(String message) {
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Rotation(final int pWindHeading) {

        final Thread chrono = new Thread() {
            float getRotate = ivArrow.getRotation();
            float cp = getRotate;
            float heading = (pWindHeading);


            public void run() {

                while (cp != heading) {
                    if (cp > 360) {
                        cp = 0;
                    }
                    try {
                        sleep(1);
                        cp+=1;
                        ivArrow.setRotation(cp);
                    } catch (InterruptedException e) {

                    } finally {

                    }
                }
            }
        };
        chrono.start();
    }


    // demmarre Google maps
    public void clicLongCoord() {

        String lat = (String) tvLat.getText();
        String lon = (String) tvLon.getText();

        // Parse les String en double
        double dbLat = Double.parseDouble(lat);
        double dbLon = Double.parseDouble(lon);

        if (!lat.equals("0.0") && !lat.equals("")
                && !lon.equals("0.0")
                && !lon.equals("")
                && !lon.equals(null)) {
            Intent intent = new Intent(getBaseContext(), MapsActivity.class);
            intent.putExtra("lat", dbLat);
            intent.putExtra("lon", dbLon);
            intent.putExtra("name", name);
            intent.putExtra("heading", winHeading);
            intent.putExtra("windAverage", windAverage);
            startActivity(intent);
        } else {
            Toast.makeText(getBaseContext(), R.string.desole, Toast.LENGTH_SHORT).show();
        }
    }

}
