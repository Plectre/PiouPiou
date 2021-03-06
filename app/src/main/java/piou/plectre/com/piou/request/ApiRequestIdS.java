package piou.plectre.com.piou.request;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;

import piou.plectre.com.piou.LauncherActivity;
import piou.plectre.com.piou.handler.ClassArray;
import piou.plectre.com.piou.handler.CompareCoord;
import piou.plectre.com.piou.handler.DateHandler;


/**
 * Created by El Di@blo on 03/05/2017.
 */

public class ApiRequestIdS {
    private RequestQueue queue;
    private double latitude;
    private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ApiRequestIdS(RequestQueue queue, Context context) {

        this.queue = queue;

    }

    public void checkIdsPiou(final String id, final CheckPiouIdsCallbackNames callback) {


        String url = "http://api.pioupiou.fr/v1/live/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Check si il y'a une reponse du serveur
                if (response.length() > 0) {

                    //Log.d("APP", response.toString());
                    try {
                        JSONArray data = response.getJSONArray("data");
                        int dataLenght = data.length();
                        HashMap<String, String> map = new HashMap<>();
                        for (int i = 0; i < dataLenght; i++) {

                            JSONObject jsonObject = data.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            JSONObject status = jsonObject.getJSONObject("status");
                            JSONObject meta = jsonObject.getJSONObject("meta");
                            JSONObject mesures = jsonObject.getJSONObject("measurements");
                            JSONObject location = jsonObject.getJSONObject("location");


                            String state = status.getString("state");
                            String strId = String.valueOf(id);
                            String date = mesures.getString("date");
                            latitude = location.getDouble("latitude");
                            longitude = location.getDouble("longitude");


                            // on verifie l'etat du piou si actif
                            if (state.equals("on")) {
                                // on ecarte les dates qui valent null
                                if (!date.equals("null")) {

                                    // appel de la methode qui compare les dates et renvoi true si
                                    // celles-ci sont identiques
                                    DateHandler dh = new DateHandler(date);
                                    boolean compare = dh.compareYear();

                                    if (compare == true) {

                                        CompareCoord cc = new CompareCoord();
                                        cc.recupCoorPiou(latitude, longitude);
                                        String distance = cc.Compare();
                                        String name = meta.getString("name");

                                        // Ajout des Id et name à la liste
                                        map.put("_" + strId + "_" + "_" + distance + "_", name);
                                        //Log.i("APP", String.valueOf(map)+"\n");
                                        //map.put("_" + String.valueOf(distance) + "_", name);
                                    }

                                }
                            }

                        }

                        // Envoi du map pour classement

                        callback.onSuccessIds(map);

                    } catch (JSONException e) {
                        Log.e("APP", "EXCEPTION : " + e);
                        e.printStackTrace();
                    } catch (ParseException e) {
                        Log.e("APP", "Date.parse execption" + (e));
                        e.printStackTrace();
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
                    callback.onError("Erreur du serveur !");
                }
            }
        });
        queue.add(request);

    }


    public interface CheckPiouIdsCallbackNames {

        void onSuccessIds(HashMap<String, String> idS);

        void onError(String message);

    }
}