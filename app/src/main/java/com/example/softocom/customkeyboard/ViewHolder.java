package com.example.softocom.customkeyboard;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Softocom on 25.04.2016.
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    protected abstract void bind(T item);
}
