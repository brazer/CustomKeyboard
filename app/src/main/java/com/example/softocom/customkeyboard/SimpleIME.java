package com.example.softocom.customkeyboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Softocom on 21.04.2016.
 */
public class SimpleIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.invalidateAllKeys();
        return kv;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                if (primaryCode < Byte.MAX_VALUE) {
                    char code = (char) primaryCode;
                    if (Character.isLetter(code) && caps) {
                        code = Character.toUpperCase(code);
                    }
                    ic.commitText(String.valueOf(code), 1);
                } else {
                    switch (primaryCode) {
                        case 500:
                            Utils.sendStickerToActivity(R.drawable.s1, this);
                            //sendStickerToActivity(R.drawable.s1);
                            break;
                        case 501:
                            sendStickerToActivity(R.drawable.s2);
                            break;
                        case 502:
                            sendStickerToActivity(R.drawable.s3);
                            break;
                        case 503:
                            sendStickerToActivity(R.drawable.s4);
                            break;
                        case 504:
                            sendStickerToActivity(R.drawable.s5);
                            break;
                        case 505:
                            sendStickerToActivity(R.drawable.s6);
                            break;
                        case 506:
                            sendStickerToActivity(R.drawable.s7);
                            break;
                        case 507:
                            sendStickerToActivity(R.drawable.s8);
                            break;
                        case 508:
                            sendStickerToActivity(R.drawable.s9);
                            break;
                        case 509:
                            sendStickerToActivity(R.drawable.s10);
                            break;
                        case 510:
                            sendStickerToActivity(R.drawable.s11);
                            break;
                        case 511:
                            sendStickerToActivity(R.drawable.s12);
                            break;
                        case 512:
                            sendStickerToActivity(R.drawable.s13);
                            break;
                        case 513:
                            sendStickerToActivity(R.drawable.s14);
                            break;
                        case 514:
                            sendStickerToActivity(R.drawable.s15);
                            break;
                        case 515:
                            sendStickerToActivity(R.drawable.s16);
                            break;
                        case 516:
                            sendStickerToActivity(R.drawable.s17);
                            break;
                        case 517:
                            sendStickerToActivity(R.drawable.s18);
                            break;
                    }
                }

        }
    }

    private void sendStickerToActivity(int resId) {
        Intent mainIntent = new Intent(Intent.ACTION_SEND);
        mainIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        //String currentActivity = getActivity();
        String currentActivity = getCurrentActivity();
        //String currentActivity = getActivityDep(pkgAppsList.size());
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
        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + resId);
        pictureMessageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        pictureMessageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (packageName != null && className != null) {
            pictureMessageIntent.setComponent(new ComponentName(packageName, className));
        }

        startActivity(pictureMessageIntent);
    }

    private String getCurrentActivity() {
        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String currentApp = "";
            @SuppressWarnings("WrongConstant")
            UsageStatsManager usm = (UsageStatsManager) getSystemService("usagestats");
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
            /*List<ActivityManager.AppTask> tasks = am.getAppTasks();
            ActivityManager.RecentTaskInfo taskInfo = tasks.get(0).getTaskInfo();
            //// FIXME: 22.04.2016 
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String processName = taskInfo.topActivity.getClassName();
                return processName;
            } else {
                String processName = taskInfo.origActivity.getClassName();
                return processName;
            }*/

        } else {
            ActivityManager.RunningTaskInfo info = am.getRunningTasks(1).get(0);
            return info.topActivity.getClassName();
        }
    }

    private String getActivityDep(int count) {
        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(count);

        return list.get(1).topActivity.getClassName();
    }

    private String getActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            HashMap activities = (HashMap) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity.getLocalClassName();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {
        if (primaryCode == 49) {

        }
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

}
