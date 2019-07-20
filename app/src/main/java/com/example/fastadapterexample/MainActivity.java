package com.example.fastadapterexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItemAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements ItemTouchCallback {

    RecyclerView recycle_view;
    SearchView search_view;
    ArrayList<AbstractItem> userAL;
    FastAdapter fastAdapter;
    ItemAdapter<AbstractItem> itemAdapter;
    SharedPreferences sharedPreferences;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register();

//        String userHM = new Gson().toJson(new HashMap<Integer, Integer>());
//        String jsonSTR = sharedPreferences.getString("myHM", userHM);
//        TypeToken<HashMap<Integer, Integer>> token = new TypeToken<HashMap<Integer, Integer>>() {
//        };
//        final HashMap<Integer, Integer> retrievedHashMap = new Gson().fromJson(jsonSTR, token.getType());

        String userHMStr = new Gson().toJson(new HashMap<Integer, Integer>());
        String jsonHMStr = sharedPreferences.getString("myHM", userHMStr);
        // convert json string into list object.
        TypeToken<HashMap<Integer, Integer>> token = new TypeToken<HashMap<Integer, Integer>>() {
        };
        final HashMap<Integer, Integer> retriveHM = new Gson().fromJson(jsonHMStr, token.getType());


        //Set Value into Fast Adapter.
        for (int i = 1; i <= 4; i++) {
            if (i == 1) {
                userAL.add(new HeadModel(i));
            }
            String path = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSn4h6I3mMYRMYflFTvRQjSS8_vCTgC677yJaLnD8L0yaEiDH1e";
            userAL.add(new UserModel(i + 1, "Name " + i, "user description " + i, path, this));
        }


        Collections.sort(userAL, new Comparator<AbstractItem>() {
            @Override
            public int compare(AbstractItem o1, AbstractItem o2) {

                int o1Id = -1;
                int o2Id = -1;
                if (o1 instanceof UserModel) {
                    o1Id = ((UserModel) o1).id;
                } else if (o1 instanceof HeadModel) {
                    o1Id = ((HeadModel) o1).id;
                }

                if(o2 instanceof UserModel){
                    o2Id = ((UserModel) o2).id;
                }else if(o2 instanceof HeadModel){
                    o2Id = ((HeadModel) o2).id;
                }

                if (retriveHM.get(o1Id) != null && retriveHM.get(o2Id) != null) {
                    int pos1 = retriveHM.get(o1Id);
                    int pos2 = retriveHM.get(o2Id);
                    return pos1 - pos2;
                }

                return 0;
            }
        });


        Log.e("user Arraylist : ", String.valueOf(userAL.size()));
        itemAdapter = new ItemAdapter<>();

        fastAdapter = FastAdapter.with(itemAdapter);
//        fastAdapter = FastAdapter.with(Arrays.asList(headerAdapter, itemAdapter));


        recycle_view.setLayoutManager(new LinearLayoutManager(this));
        recycle_view.setAdapter(fastAdapter);
        itemAdapter.add(userAL);


        //Set Click Lisner On Fast Adapter.
        fastAdapter.withSelectable(true);

//        fastAdapter.withOnClickListener(new OnClickListener<UserModal>() {
//            @Override
//            public boolean onClick(View v, IAdapter<UserModal> adapter, UserModal item, int position) {
//                String data = "The Position is : "+ position + " , The Id is = "+ item.id;
//                Log.e("data = ", data);
//                Toast.makeText(MainActivity.this, item.id + ", position = " + position, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        fastAdapter.withOnClickListener(new OnClickListener<AbstractItem>() {
            @Override
            public boolean onClick(View v, IAdapter<AbstractItem> adapter, AbstractItem item, int position) {
                if (item instanceof UserModel) {
                    UserModel modal = (UserModel) item;
                    Toast.makeText(MainActivity.this, modal.id + ", position = " + position, Toast.LENGTH_SHORT).show();
                } else if (item instanceof HeadModel) {
                    HeadModel modal = (HeadModel) item;
                    Toast.makeText(MainActivity.this, modal.id + ", position = " + position, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

        });


        //set Drag and Drop on Fast Adapter
        SimpleDragCallback dragCallback = new SimpleDragCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(dragCallback);
        itemTouchHelper.attachToRecyclerView(recycle_view);


        //Fillter Text
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                itemAdapter.filter(s);
                return true;
            }
        });

        itemAdapter.getItemFilter().withFilterPredicate(new IItemAdapter.Predicate<AbstractItem>() {
            @Override
            public boolean filter(AbstractItem item, CharSequence charSequence) {
                if (item instanceof UserModel) {
                    UserModel modal = (UserModel) item;
                    return modal.name.startsWith(String.valueOf(charSequence));
                }
                return false;
            }
        });
    }


    private void register() {
        recycle_view = findViewById(R.id.recycle_view);
        userAL = new ArrayList<>();
        search_view = findViewById(R.id.search_view);
        sharedPreferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
    }

    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        Collections.swap(itemAdapter.getAdapterItems(), oldPosition, newPosition);
        fastAdapter.notifyAdapterItemMoved(oldPosition, newPosition);
//        HashMap<Integer, Integer> hashMap = new HashMap<>();
//        for (int i = 0; i < itemAdapter.getAdapterItems().size(); i++) {
//            hashMap.put(itemAdapter.getAdapterItems().get(i).id, i);
//        }
//
//        String gSonObject = new Gson().toJson(hashMap);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("myHM", gSonObject);
//        editor.apply();


        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < itemAdapter.getAdapterItems().size(); i++) {
            if (itemAdapter.getAdapterItem(i) instanceof UserModel) {
                UserModel modal = (UserModel) itemAdapter.getAdapterItem(i);
                hashMap.put(modal.id, i);
            } else if (itemAdapter.getAdapterItem(i) instanceof HeadModel) {
                HeadModel modal = (HeadModel) itemAdapter.getAdapterItem(i);
                hashMap.put(modal.id, i);
            }
        }
        String saveGson = new Gson().toJson(hashMap);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("myHM", saveGson);
        editor.apply();

        return true;
    }

    @Override
    public void itemTouchDropped(int oldPosition, int newPosition) {
    }


}

