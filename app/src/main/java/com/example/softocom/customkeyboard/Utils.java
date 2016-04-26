package com.example.softocom.customkeyboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Softocom on 25.04.2016.
 */
public class Utils {

    public static void sendStickerToActivity(int resId, Context context) {
        Intent mainIntent = new Intent(Intent.ACTION_SEND);
        mainIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        List pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        String currentActivity = getCurrentActivity(context);
        String packageName = null, className = null;
        for (Object resolveInfo : pkgAppsList) {
            if (((ResolveInfo) resolveInfo).activityInfo.name.equals(currentActivity)) {
                packageName = ((ResolveInfo) resolveInfo).activityInfo.applicationInfo.packageName;
                className = ((ResolveInfo) resolveInfo).activityInfo.name;
                break;
            }
        }

        Intent pictureMessageIntent = new Intent(Intent.ACTION_SEND);
        pictureMessageIntent.setType("image/*");
        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
        pictureMessageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        pictureMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (packageName != null && className != null) {
            pictureMessageIntent.setComponent(new ComponentName(packageName, className));
        }

        context.startActivity(pictureMessageIntent);
    }

    private static String getCurrentActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //// FIXME: 25.04.2016 empty string return
            String currentApp = "";
            @SuppressWarnings("WrongConstant")
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                    time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),
                            usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(
                            mySortedMap.lastKey()).getPackageName();
                }
            }
            return currentApp;
        } else {
            ActivityManager.RunningTaskInfo info = am.getRunningTasks(1).get(0);
            return info.topActivity.getClassName();
        }
    }

}
