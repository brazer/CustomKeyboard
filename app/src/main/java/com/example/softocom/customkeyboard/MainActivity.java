package com.example.softocom.customkeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private StickersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<Integer> listStickers;
        listStickers = new ArrayList<>();
        listStickers.add(R.drawable.s1);
        listStickers.add(R.drawable.s2);
        listStickers.add(R.drawable.s3);
        listStickers.add(R.drawable.s4);
        listStickers.add(R.drawable.s5);
        listStickers.add(R.drawable.s6);
        listStickers.add(R.drawable.s7);
        listStickers.add(R.drawable.s8);
        listStickers.add(R.drawable.s9);
        listStickers.add(R.drawable.s10);
        listStickers.add(R.drawable.s11);
        listStickers.add(R.drawable.s12);
        listStickers.add(R.drawable.s13);
        listStickers.add(R.drawable.s14);
        listStickers.add(R.drawable.s15);
        listStickers.add(R.drawable.s16);
        listStickers.add(R.drawable.s17);
        listStickers.add(R.drawable.s18);

        GridLayoutManager lLayout = new GridLayoutManager(MainActivity.this, 3);
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(lLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        mAdapter = new StickersAdapter(inflater, listStickers, this);
        list.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Utils.sendImageToActivity(mAdapter.getItem(position), this);
    }

}
