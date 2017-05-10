package piou.plectre.com.piou.request;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by El Di@blo on 03/05/2017.
 */

public class ApiRequestIdS {
    private RequestQueue queue;
    private Context context;



    public ApiRequestIdS(RequestQueue queue, Context context) {

            this.queue = queue;
            this.context = context;
        }

    public void checkIdsPiou(final String id, final CheckPiouIdsCallbackNames callback) {


        String url = "http://api.pioupiou.fr/v1/live/" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Check si il y'a une reponse du serveur
                if (response.length() > 0) {

                    Log.d("APP", response.toString());
                    try {
                        JSONArray data = response.getJSONArray("data");
                        int dataLenght = data.length();
                        //ArrayList<String> names = new ArrayList<>();
                        HashMap<String, String> map = new HashMap<>();
                        for (int i = 0; i < dataLenght; i++) {

                            JSONObject jsonObject = data.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            JSONObject status = jsonObject.getJSONObject("status");
                            JSONObject meta = jsonObject.getJSONObject("meta");
                            String name = meta.getString("name");
                            String state = status.getString("state");
                            String strId = String.valueOf(id);

                            if (state.equals("on")) {
                                //Log.i("APP", state + " " +strId + " "+ name);
                                //names.add(String.valueOf(id));
                                map.put("_"+strId+"_", name);

                            }

                        }
                        callback.onSuccessIds(map);
                        //callback.onSuccessIds(names);

                    } catch (JSONException e) {
                        Log.e("APP", "EXCEPTION : " + e);
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