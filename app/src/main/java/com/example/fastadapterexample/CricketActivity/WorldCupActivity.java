package com.example.fastadapterexample.CricketActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassification;

import com.example.fastadapterexample.CricketModal.MatchModel;
import com.example.fastadapterexample.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class WorldCupActivity extends AppCompatActivity {


    RecyclerView recycle_view;
    Context context;
    //    private static final String path = "https://api.myjson.com/bins/zwtgb";  //Other Multiple
//    private static final String path = "http://lufick.com/androidftp/testJsonFile/jsonFetchData.json";  //Lufick Multiple
    private static final String path = "https://rest.entitysport.com/v2/matches/?status=2&token=ec471071441bb2ac538a0ff901abd249&per_page=10&&paged=1";  //Lufick Multiple
    ArrayList<MatchModel> matchModalAL;
    FastAdapter fastAdapter;
    ItemAdapter<MatchModel> itemAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_cup);

        register();

        //fetch json data using okhttp libaray
        new FetchJsonDataOnServer().execute();

        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recycle_view.setLayoutManager(linearLayoutManager);

        Log.e("test :", "test");

        // Recyceview click
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<MatchModel>() {
            @Override
            public boolean onClick(View v, IAdapter<MatchModel> adapter, MatchModel item, int position) {
                Log.e("match_id", String.valueOf(item.id));
                progressDialog.show();
                Intent intent = new Intent(WorldCupActivity.this, WorldCupDetailsActivity.class);
                intent.putExtra("cid", item.id);
                progressDialog.dismiss();
                startActivity(intent);
                return true;
            }
        });

    }

    private void register() {
        context = this;
        recycle_view = findViewById(R.id.recycle_view);
        matchModalAL = new ArrayList<>();
        progressDialog = new ProgressDialog(context);
    }

    private class FetchJsonDataOnServer extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait While Data Is Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(path).build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                JSONArray jsonArray = jsonObject1.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String short_title = object.getString("short_title");
                    String status_note = object.getString("status_note");

                    JSONObject competition_object = object.getJSONObject("competition");
                    int id = competition_object.getInt("cid");

                    JSONObject teama_object = object.getJSONObject("teama");
                    String teama_name = teama_object.getString("name");
                    String teama_logo_url = teama_object.getString("logo_url");

                    JSONObject teamb_object = object.getJSONObject("teamb");
                    String teamb_name = teamb_object.getString("name");
                    String teamb_logo_url = teamb_object.getString("logo_url");

                    matchModalAL.add(new MatchModel(context, id, short_title, status_note, teama_name, teama_logo_url, teamb_name, teamb_logo_url));
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            itemAdapter.add(matchModalAL);
            recycle_view.setVisibility(View.VISIBLE);
            recycle_view.setAdapter(fastAdapter);
        }
    }
}
