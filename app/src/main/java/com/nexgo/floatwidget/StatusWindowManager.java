package com.nexgo.floatwidget;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author duxd
 * @since 1.0.0
 */
public class StatusWindowManager {
    private static StatusView statusWindow;
    private static WindowManager.LayoutParams statusWindowParams;
    private static WindowManager mWindowManager;
    private static ActivityManager mActivityManager;


    public static boolean isWindowShowing() {
        return statusWindow != null;
    }

    public static void createStatusWindow(final Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (statusWindow == null) {
            statusWindow = new StatusView(context);
            if (statusWindowParams == null) {
                statusWindowParams = new LayoutParams();
                statusWindowParams.type = LayoutParams.TYPE_PHONE;
                statusWindowParams.format = PixelFormat.RGBA_8888;
                statusWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                statusWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                statusWindowParams.width = StatusView.viewWidth;
                statusWindowParams.height = StatusView.viewHeight;
                statusWindowParams.x = screenWidth;
                statusWindowParams.y = screenHeight / 2;
            }
            statusWindow.setParams(statusWindowParams);
            windowManager.addView(statusWindow, statusWindowParams);
        }
    }

    private static WindowManager getWindowManager(final Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    public static void updateTime(final Context context) {

    }

    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

    public static String getUsedPercentValue(final Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return "" + percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }
}
