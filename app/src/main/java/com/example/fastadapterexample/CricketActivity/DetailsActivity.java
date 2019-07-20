package com.example.fastadapterexample.CricketActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastadapterexample.CricketModal.PlayerModal;
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

public class DetailsActivity extends AppCompatActivity {

    Context context;
    int cid,team_id;
    String logo_url;
    RecyclerView recyclerView;
    TextView title_txt;
    ImageView country_flag_img;
    Toolbar toolbar;
    FastAdapter<PlayerModal> fastAdapter;
    ItemAdapter<PlayerModal> itemAdapter;
    ArrayList<PlayerModal> plAL = new ArrayList<>();
    ProgressDialog progressDialog;
    String title_match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        register();
        getIntentValue();

        itemAdapter = new ItemAdapter<>();
        fastAdapter = FastAdapter.with(itemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(fastAdapter);

        new fetchPlayerDataById().execute();

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<PlayerModal>() {
            @Override
            public boolean onClick(View v, IAdapter<PlayerModal> adapter, PlayerModal item, int position) {
                Intent intent = new Intent(context, PlayerProfileActivity.class);
                intent.putExtra("cid_key",cid);
                intent.putExtra("team_id",team_id);
                intent.putExtra("pid",item.pid);
                startActivity(intent);
                return true;
            }
        });
    }

    private void register() {
        context = this;
        recyclerView=findViewById(R.id.recyclev_view);
        title_txt = findViewById(R.id.title_txt);
        progressDialog = new ProgressDialog(context);
        country_flag_img = findViewById(R.id.country_flag_img);
        toolbar = findViewById(R.id.toolbar);
    }

    private void getIntentValue() {
        cid = getIntent().getIntExtra("cid_key",0);
        team_id = getIntent().getIntExtra("team_id",0);
        logo_url = getIntent().getStringExtra("logo_url");
    }

    private class fetchPlayerDataById extends AsyncTask {
        String path = "https://rest.entitysport.com/v2/competitions/"+cid+"/squads/?token=ec471071441bb2ac538a0ff901abd249";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait.. While We Fetch All Players Value");
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(path).build();


            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                JSONArray jsonArray = jsonObject1.getJSONArray("squads");

                for(int i = 0 ; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    int team_id2 = object.getInt("team_id");
                    title_match= object.getString("title");
                    if(team_id2 == team_id){
                        JSONArray jsonArray1= object.getJSONArray("players");
                        for(int j = 0; j < jsonArray1.length(); j++){
                            JSONObject object1 = jsonArray1.getJSONObject(j);
                            int pid = object1.getInt("pid");
                            String title = object1.getString("title");
                            String playing_role = object1.getString("playing_role");
                            String nationality = object1.getString("nationality");

                            plAL.add(new PlayerModal(context,title,playing_role,nationality,pid));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            itemAdapter.add(plAL);
            title_txt.setText(title_match);
            Glide.with(context).load(logo_url).into(country_flag_img);
            progressDialog.dismiss();
            country_flag_img.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }

    }

}

