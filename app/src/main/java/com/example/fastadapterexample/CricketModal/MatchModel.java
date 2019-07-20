package com.example.fastadapterexample.CricketModal;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fastadapterexample.R;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class MatchModel extends AbstractItem<MatchModel,MatchModel.ViewHolder> {

    public int id;
    public String short_title,teama_name,teama_logo_url,teamb_name,teamb_logo_url,status_note;
    Context context;

    public MatchModel(Context context, int id, String short_title, String status_note, String teama_name, String teama_logo_url, String teamb_name, String teamb_logo_url)
    {
        this.context = context;
        this.id = id;
        this.short_title = short_title;
        this.teama_name = teama_name;
        this.teama_logo_url = teama_logo_url;
        this.teamb_name = teamb_name;
        this.teamb_logo_url = teamb_logo_url;
        this.status_note = status_note;
    }


    @Override
    public MatchModel.ViewHolder getViewHolder(View v) {
        return new MatchModel.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.line_id;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.inflate_cricket_match_list;
    }

    public class ViewHolder extends FastAdapter.ViewHolder<MatchModel> {

        ImageView teama_img,teamb_img;
        TextView teama_name,teamb_name,short_title,status_note;

        public ViewHolder(View itemView) {
            super(itemView);
            teama_img = itemView.findViewById(R.id.teama_img);
            teamb_img = itemView.findViewById(R.id.teamb_img);
            teama_name = itemView.findViewById(R.id.teama_name);
            teamb_name = itemView.findViewById(R.id.teamb_name);
            short_title = itemView.findViewById(R.id.short_title);
            status_note = itemView.findViewById(R.id.status_note);
        }

        @Override
        public void bindView(MatchModel item, List<Object> payloads) {
            Glide.with(context).load(teama_logo_url).into(teama_img);
            Glide.with(context).load(teamb_logo_url).into(teamb_img);
            teama_name.setText(item.teama_name);
            teamb_name.setText(item.teamb_name);
            short_title.setText(item.short_title);
            status_note.setText(item.status_note);
        }

        @Override
        public void unbindView(MatchModel item) {

        }


    }
}
