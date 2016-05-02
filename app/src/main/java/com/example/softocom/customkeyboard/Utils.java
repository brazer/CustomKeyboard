package com.example.softocom.customkeyboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

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

    public static void sendImageToActivity(int resId, Context context) {
        Bitmap bitmapToShare = BitmapFactory.decodeResource(
                context.getResources(), resId);

        File file = getTmpFile(context, bitmapToShare);

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(shareIntent);
    }

    private static File getTmpFile(Context ctx, Bitmap bmp) {
        File outputDir = null;
        if (ctx != null) {
            outputDir = ctx.getExternalCacheDir();
            if (outputDir == null || !outputDir.canWrite())
                outputDir = ctx.getCacheDir();
        }
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("tmpImg", ".png", outputDir);
            FileOutputStream fos = new FileOutputStream(tmpFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
        return tmpFile;
    }

    private static String getCurrentActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getCurrentActivityFromAboveLollipop(context);
        } else {
            ActivityManager.RunningTaskInfo info = am.getRunningTasks(1).get(0);
            return info.topActivity.getClassName();
        }
    }

    private static String getCurrentActivityFromAboveLollipop(Context context) {
        final int PROCESS_STATE_TOP = 2;
        ActivityManager.RunningAppProcessInfo currentInfo = null;
        Field field = null;
        try {
            field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
        } catch (Exception ignored) {
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo app : appList) {
            if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && app.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
                Integer state = null;
                try {
                    state = field.getInt(app);
                } catch (Exception e) {
                }
                if (state != null && state == PROCESS_STATE_TOP) {
                    currentInfo = app;
                    break;
                }
            }
        }
        return currentInfo.processName;
    }

}
