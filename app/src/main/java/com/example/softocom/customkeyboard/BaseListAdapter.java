package com.example.softocom.customkeyboard;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

/**
 * Created by Softocom on 25.04.2016.
 */
public abstract class BaseListAdapter<T, VH extends ViewHolder<T>> extends RecyclerView.Adapter<VH> {

    protected LayoutInflater inflater;
    protected List<T> items;
    protected AdapterView.OnItemClickListener onItemClickListener;

    public BaseListAdapter(LayoutInflater inflater, List<T> items) {
        this.inflater = inflater;
        this.items = items;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @LayoutRes
    protected abstract int getItemLayoutRes();

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createHolder(inflater.inflate(getItemLayoutRes(), parent, false), viewType);
    }

    protected abstract VH createHolder(View view, int viewType);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public void update(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
