package com.example.protien_pricer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchHandler extends Service {

    private final String apikey = "UkaojsuvAJCo3lryhbiiUg0eodTWSnmQyCIYr76d";
    private final OkHttpClient client = new OkHttpClient();

    private JSONArray search_results;
    public JSONArray getResults() {
        // because GET is async, we have to wait to get results at first
        long stop = System.currentTimeMillis() + 10000 ;
        long count = 0;

        while( count < stop ){
            if (search_results != null) return search_results;
            count = System.currentTimeMillis();
        }
        return search_results;
    }
    private void setResults(JSONArray results){
        this.search_results = results;
    }
    public void resetResults(){this.search_results = null; }

    private void setItems(){
        JSONArray array = getResults();
        ArrayList<FoodItem> items = new ArrayList<FoodItem>();

        for(int i = 0; i < array.length(); i++){
            try {
                JSONObject obj = array.getJSONObject(i);
                FoodItem item = new FoodItem(obj);
                item.setIndex(i);
                items.add(item);
                //System.out.println(obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SearchReturns.getInstance().setItems(items);
    };


    public LocalBinder local_binder = new LocalBinder();

    public SearchHandler() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return local_binder;
    }

    // =========== Search ============
    public void search(String term, int size) throws Exception {

        String url_base = "https://api.nal.usda.gov/fdc/v1/foods/search?query=";
        String url_mod = term + "&pageSize=" + String.valueOf(size) + "&api_key=" + apikey;
        url_mod += "&dataType=Branded";

        Request request = new Request.Builder()
                .url(url_base + url_mod)
                .build();

        client.newCall(request).enqueue(new Callback() {
            private JSONArray result;

            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String json_string = responseBody.string();

                    try {
                        JSONObject json = new JSONObject(json_string);
                        //System.out.println("json: " + json.toString());
                        JSONArray results =  json.getJSONArray("foods");

                        setResults(results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        setItems();
    }

    public class LocalBinder extends Binder {
        SearchHandler getBoundService() {
            return SearchHandler.this;
        }
    }


}