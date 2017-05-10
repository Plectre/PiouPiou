package piou.plectre.com.piou.request;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import piou.plectre.com.piou.handler.DateHandler;


/**
 * Created by El Di@blo on 02/05/2017.
 */

public class ApiRequestId {

    private RequestQueue queue;
    private Context context;

    public ApiRequestId(RequestQueue queue, Context context) {

        this.queue = queue;
        this.context = context;
    }

    public void checkIdPiou(final String id, final ApiRequestId.CheckPiouCallback callback) {


        String url = "http://api.pioupiou.fr/v1/live/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Verification si l'id du piou existe
                if (response != null) {
                    Log.d("APP", response.toString());
                    String name = "Default";
                    // Ici on parse le Json et on recupére les datas
                    try {
                        JSONObject data = response.getJSONObject("data");
                        JSONObject meta = data.getJSONObject("meta");
                        JSONObject mesures = data.getJSONObject("measurements");
                        JSONObject location = data.getJSONObject("location");
                        double latitude = location.getDouble("latitude");
                        double longitude = location.getDouble("longitude");
                        double wind_average = Math.floor(mesures.getLong("wind_speed_avg") / 1.852);// Km/h ----> Knot
                        int wind_heading = mesures.getInt("wind_heading");

                        String date = mesures.getString("date");

                        // Appel de la classe qui reformate les dates en francais
                        DateHandler dateHandler = new DateHandler(date);
                        String currentTime = dateHandler.getHeure(date);
                        String currentDay = dateHandler.jour(date);


                        if (wind_heading == 0) {
                            wind_heading = 360;
                        }
                        name = meta.getString("name");
//                      String id = data.getString("id");
                        callback.onSuccess(wind_heading,
                                wind_average,
                                name,
                                latitude,
                                longitude,
                                currentTime,
                                currentDay);


                    } catch (JSONException e) {
                        Log.e("APP", "EXCEPTION : " + e);
                        e.printStackTrace();
                        callback.onError("Erreur de fichier Json!");
                    }
                } else {
                    callback.onError("Pas de reponse !");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    callback.onError("Impossible de se connecter");
                } else if (error instanceof ServerError) {
                    callback.onError("Le Pioupiou ne repond pas !");
                }
            }
        });

        queue.add(request);

    }

    public interface CheckPiouCallback {
        void onSuccess(int winHeading, double windAverage, String name, double latitude, double longitude, String currentTime, String currentDay);

        void onError(String message);

    }
}