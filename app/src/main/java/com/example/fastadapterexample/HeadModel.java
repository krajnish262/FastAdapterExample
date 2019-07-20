package com.example.fastadapterexample;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.items.AbstractItem;

public class HeadModel extends AbstractItem<HeadModel, HeadModel.ViewHolder> {

    int id;

    public HeadModel(int id) {
        this.id = id;
    }

    @Override
    public HeadModel.ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.header_lin;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.inflate_header;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
