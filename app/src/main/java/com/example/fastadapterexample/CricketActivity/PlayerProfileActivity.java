package com.example.fastadapterexample.CricketActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextClassification;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fastadapterexample.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PlayerProfileActivity extends AppCompatActivity {



    Context context;
    ImageView profile_img;
    TextView title,country,fast_name,middle_name,last_name,short_name,dob,role_playing,batting_style,bowling_style,nationality_txt;
    LinearLayout lin_view;
    ScrollView scroll_view;

    int team_id,cid,pid;
    String logoSTR,titleSTR,countrySTR,shortSTR,firstSTR,lastSTR,middleSTR,dobSTR,roleSTR,battingSTR,bowlingSTR,nationalitySTR;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        register();
        getIntentValue();

        new PlayerProfile().execute();

    }

    private void setTextValue() {
        if(logoSTR.equals("")){
            Glide.with(context).load("https://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.original.jpg").into(profile_img);
        }else{
            Glide.with(context).load(logoSTR).into(profile_img);
        }
        if(titleSTR.equals("")){

        }else {
            title.setText(titleSTR);
        }

        if(countrySTR.equals("")){
            country.setText("Not Avvailable");
        }else {
            country.setText("Country : ("+countrySTR+")");
        }

        if(shortSTR.equals("")){

        }else {
            short_name.setText(shortSTR);
        }

        if(firstSTR.equals("")){
            fast_name.setText("Not Available");
        }else {
            fast_name.setText(firstSTR);
        }

        if(lastSTR.equals("")){
            last_name.setText("Not Available");
        }else {
            last_name.setText(lastSTR);
        }

        if(middleSTR.equals("")){
            middle_name.setText("Not Available");
        }else {
            middle_name.setText(middleSTR);
        }

        if(dobSTR.equals("")){
            dob.setText("Not Available");
        }else {
            dob.setText(dobSTR);
        }

        if(roleSTR==null){
            role_playing.setText("Not Available");
        }else {
            role_playing.setText(roleSTR);
        }

        if(battingSTR.equals("")){
            batting_style.setText("Not Available");
        }else {
            batting_style.setText(battingSTR);
        }

        if(bowlingSTR.equals("")){
            bowling_style.setText("Not Available");
        }else {
            bowling_style.setText(bowlingSTR);
        }

        if(nationalitySTR.equals("")){
            nationality_txt.setText("Not Available");
        }else {
            nationality_txt.setText(nationalitySTR);
        }
        lin_view.setVisibility(View.VISIBLE);
        scroll_view.setVisibility(View.VISIBLE);
    }

    private void getIntentValue() {
        team_id = getIntent().getIntExtra("team_id",0);
        cid = getIntent().getIntExtra("cid_key",0);
        pid = getIntent().getIntExtra("pid",0);
    }

    private void register() {
        context = this;
        progressDialog = new ProgressDialog(context);
        profile_img =findViewById(R.id.profile_img);
        title = findViewById(R.id.title);
        country = findViewById(R.id.country);
        fast_name = findViewById(R.id.fast_name);
        middle_name = findViewById(R.id.middle_name);
        last_name = findViewById(R.id.last_name);
        short_name = findViewById(R.id.short_name);
        dob = findViewById(R.id.dob);
        role_playing = findViewById(R.id.role_playing);
        batting_style = findViewById(R.id.batting_style);
        bowling_style = findViewById(R.id.bowling_style);
        nationality_txt = findViewById(R.id.nationality_txt);
        lin_view = findViewById(R.id.lin_view);
        scroll_view= findViewById(R.id.scroll_view);
    }

    private class PlayerProfile extends AsyncTask {
        String path = "https://rest.entitysport.com/v2/competitions/"+cid+"/squads/?token=ec471071441bb2ac538a0ff901abd249";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }else{
                progressDialog.setMessage("Please Wait.. ,We Are Fetching Player Data On Server");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
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
                for(int i = 0;i < jsonArray.length();i++){
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    int team_id2 = object1.getInt("team_id");
                    if(team_id2 == team_id){
                        JSONArray array = object1.getJSONArray("players");
                        for(int j = 0; j<array.length(); j++){
                            JSONObject object2 = array.getJSONObject(j);
                            int pid2 = object2.getInt("pid");
                            if(pid2==pid){
                                logoSTR = object2.getString("logo_url");
                                titleSTR = object2.getString("title");
                                countrySTR = object2.getString("country");
                                shortSTR = object2.getString("short_name");
                                firstSTR = object2.getString("first_name");
                                lastSTR = object2.getString("last_name");
                                middleSTR = object2.getString("middle_name");
                                dobSTR = object2.getString("birthdate");
                                battingSTR = object2.getString("batting_style");
                                bowlingSTR = object2.getString("bowling_style");
                                nationalitySTR = object2.getString("nationality");
                            }
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
            progressDialog.dismiss();
            Toast.makeText(context,"Data Fetching Complete",Toast.LENGTH_SHORT).show();
            setTextValue();
        }

    }

}
