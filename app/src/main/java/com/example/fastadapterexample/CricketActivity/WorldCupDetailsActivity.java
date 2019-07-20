package com.example.fastadapterexample.CricketActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextClassification;

import com.example.fastadapterexample.CricketModal.WorldCupModal;
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

public class WorldCupDetailsActivity extends AppCompatActivity {



    Context context;
    ProgressDialog progressDialog;
    RecyclerView recycle_view;

    int cid;
    String logo_url;
    ArrayList<WorldCupModal> wcAL = new ArrayList<>();
    FastAdapter<WorldCupModal> fastAdapter;
    ItemAdapter<WorldCupModal> itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_cup_details);


        register();
        getIntentValue();

        itemAdapter= new ItemAdapter<>();
        fastAdapter= FastAdapter.with(itemAdapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(context);
        recycle_view.setLayoutManager(linearLayoutManager);
        recycle_view.setAdapter(fastAdapter);

        new FetchValue().execute();

        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<WorldCupModal>() {
            @Override
            public boolean onClick(View v, IAdapter<WorldCupModal> adapter, WorldCupModal item, int position) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("cid_key",cid);
                intent.putExtra("team_id",item.team_id);
                intent.putExtra("logo_url",logo_url);
                startActivity(intent);
                return true;
            }
        });
    }


    private void getIntentValue() {
        cid = getIntent().getIntExtra("cid",0);
    }

    private void register() {
        context = this;
        progressDialog = new ProgressDialog(context);
        recycle_view =findViewById(R.id.recycle_view);
    }


    private class FetchValue extends AsyncTask {
        String url = "https://rest.entitysport.com/v2/competitions/"+cid+"/squads/?token=ec471071441bb2ac538a0ff901abd249";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }else{
                progressDialog.setMessage("Fetch Data By Using ID");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                JSONArray jsonArray = jsonObject1.getJSONArray("squads");
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    int team_id = object.getInt("team_id");
                    String title = object.getString("title");
                    String date = object.getString("gmdate");

                    JSONObject teamObject = object.getJSONObject("team");
                    String team_title = teamObject.getString("title");
                    logo_url = teamObject.getString("logo_url");
                    String thumb_url = teamObject.getString("thumb_url");
                    String abr = teamObject.getString("abbr");

                    wcAL.add(new WorldCupModal(context,team_id,title,date,team_title,logo_url,thumb_url,abr));
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
            itemAdapter.add(wcAL);
            progressDialog.dismiss();
        }
    }
}
