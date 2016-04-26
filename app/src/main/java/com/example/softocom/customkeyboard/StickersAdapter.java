package com.example.softocom.customkeyboard;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Softocom on 25.04.2016.
 */
public class StickersAdapter extends BaseListAdapter<Integer, StickersAdapter.Holder> {

    private AdapterView.OnItemClickListener mListener;

    public StickersAdapter(LayoutInflater inflater, List<Integer> items, AdapterView.OnItemClickListener listener) {
        super(inflater, items);
        mListener = listener;
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.list_item;
    }

    @Override
    protected Holder createHolder(View view, int viewType) {
        return new Holder(view, mListener);
    }

    static class Holder extends ViewHolder<Integer> {

        private AdapterView.OnItemClickListener mListener;

        @Bind(R.id.sticker)
        ImageView mStickerIv;

        public Holder(View itemView, AdapterView.OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
        }

        @Override
        protected void bind(Integer item) {
            Picasso.with(itemView.getContext()).load(item).into(mStickerIv);
        }

        @OnClick(R.id.card_view) void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(null, v, getAdapterPosition(), getAdapterPosition());
            }
        }
    }

}
