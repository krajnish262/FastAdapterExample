package com.example.fastadapterexample.CricketModal;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.fastadapterexample.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class PlayerModal extends AbstractItem<PlayerModal, PlayerModal.ViewHolder> {

    Context context;
    public String title,playing_role,nationality;
    public int pid;

    public PlayerModal(Context context, String title, String playing_role, String nationality, int pid) {
        this.context = context;
        this.title= title;
        this.playing_role = playing_role;
        this.nationality = nationality;
        this.pid = pid;
    }


    @NonNull
    @Override
    public PlayerModal.ViewHolder getViewHolder(View v) {
        return new PlayerModal.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.player_linview;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.inflate_player_list;
    }

    public class ViewHolder extends FastAdapter.ViewHolder<PlayerModal> {

        TextView player_name,playing_role,nationality;

        public ViewHolder(View itemView) {
            super(itemView);
            player_name = itemView.findViewById(R.id.player_name);
            playing_role = itemView.findViewById(R.id.playing_role);
            nationality = itemView.findViewById(R.id.nationality);
        }

        @Override
        public void bindView(PlayerModal item, List<Object> payloads) {
            player_name.setText("Name : "+item.title);
            playing_role.setText("Role : "+item.playing_role);
            nationality.setText("Nationality : "+item.nationality);
        }

        @Override
        public void unbindView(PlayerModal item) {

        }
    }
}

