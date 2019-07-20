package com.example.fastadapterexample;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class UserModel extends AbstractItem<UserModel,UserModel.ViewHolder> {

    String name,description,path;
    Context context;
    int id;

    public UserModel(int id, String name, String description, String path, Context context) {
        this.name = name;
        this.description = description;
        this.path = path;
        this.id = id;
        this.context = context;
    }

    @Override
    public UserModel.ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.linear_layout;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.inflate_user_layout;
    }


    public class ViewHolder extends FastAdapter.ViewHolder<UserModel> {

        TextView name_txt,description_txt,id_txt;
        ImageView circle_view;

        public ViewHolder(View itemView) {
            super(itemView);
            name_txt = itemView.findViewById(R.id.name_txt);
            description_txt = itemView.findViewById(R.id.description_txt);
            id_txt = itemView.findViewById(R.id.id_txt);
            circle_view = itemView.findViewById(R.id.circle_img);
        }

        @Override
        public void bindView(UserModel item, List<Object> payloads) {
            name_txt.setText(item.name);
            description_txt.setText(item.description);
            id_txt.setText(String.valueOf(item.id - 1));
            Glide.with(context)
                    .load(path)
                    .into(circle_view);
        }

        @Override
        public void unbindView(UserModel item) {
            name_txt.setText("");
            description_txt.setText("");
            id_txt.setText("");
        }
    }

}
