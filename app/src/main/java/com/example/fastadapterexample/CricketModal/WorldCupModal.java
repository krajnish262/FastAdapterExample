package com.example.fastadapterexample.CricketModal;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.fastadapterexample.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class WorldCupModal extends AbstractItem<WorldCupModal, WorldCupModal.Viewholder> {

    public int team_id;
     String title,date,team_title,logo_url,thumb_url,abr;
    Context context;

    public WorldCupModal(Context context, int team_id, String title, String date, String team_title, String logo_url, String thumb_url, String abr) {
        this.context=context;
        this.team_id=team_id;
        this.title=title;
        this.date=date;
        this.team_title=team_title;
        this.logo_url=logo_url;
        this.thumb_url=thumb_url;
        this.abr=abr;
    }

    @NonNull
    @Override
    public Viewholder getViewHolder(View v) {
        return new Viewholder(v);
    }

    @Override
    public int getType() {
        return R.id.squad_line_view;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.inflate_team_squad;
    }

    public class Viewholder extends FastAdapter.ViewHolder<WorldCupModal> {

        ImageView thumb_img;
        TextView title_txt,date_txt;

        public Viewholder(View itemView) {
            super(itemView);
            thumb_img = itemView.findViewById(R.id.thumb_img);
            title_txt = itemView.findViewById(R.id.title_txt);
            date_txt = itemView.findViewById(R.id.date_txt);
        }

        @Override
        public void bindView(WorldCupModal item, List<Object> payloads) {
            Glide.with(context).load(item.logo_url).into(thumb_img);
            title_txt.setText(item.title);
            date_txt.setText(item.abr);
        }

        @Override
        public void unbindView(WorldCupModal item) {

        }
    }
}
