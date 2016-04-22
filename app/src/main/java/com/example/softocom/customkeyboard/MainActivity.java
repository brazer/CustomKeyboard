package com.example.softocom.customkeyboard;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //startKeyboardInputService();
            }
        });


    }

    private void startKeyboardInputService() {
        Intent mainIntent = new Intent(Intent.ACTION_SEND);
        mainIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        for (Object resolveInfo : pkgAppsList) {
            //todo
        }

        String packageName = ((ResolveInfo) pkgAppsList.get(0)).activityInfo.applicationInfo.packageName;
        String className = ((ResolveInfo) pkgAppsList.get(0)).activityInfo.name;

        Intent pictureMessageIntent = new Intent(Intent.ACTION_SEND);
        pictureMessageIntent.setType("image/png");
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + android.R.drawable.ic_dialog_alert);
        pictureMessageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        pictureMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /*ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> taskInfo = am.getRunningAppProcesses();
        String top = taskInfo.get(0).processName;*/

        pictureMessageIntent.setComponent(new ComponentName(packageName, className));

        startActivity(pictureMessageIntent);
        //startService(pictureMessageIntent);
    }

}
