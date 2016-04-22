package com.example.softocom.customkeyboard;

import android.app.Activity;
import android.app.ActivityManager;
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

import java.util.List;

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
                    //todo get code
                    sendStickerToActivity(android.R.drawable.ic_dialog_alert);
                }

        }
    }

    private void sendStickerToActivity(int resId) {
        Intent mainIntent = new Intent(Intent.ACTION_SEND);
        mainIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        List pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
        String currentActivity = getCurrentActivity();
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
            /*List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
            String processName = list.get(0).processName;*/

            List<ActivityManager.AppTask> tasks = am.getAppTasks();
            ActivityManager.RecentTaskInfo taskInfo = tasks.get(0).getTaskInfo();
            //// FIXME: 22.04.2016 
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String processName = taskInfo.topActivity.getClassName();
                return processName;
            } else {
                String processName = taskInfo.origActivity.getClassName();
                return processName;
            }

        } else {
            ActivityManager.RunningTaskInfo info = am.getRunningTasks(1).get(0);
            return info.topActivity.getClassName();
        }
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
