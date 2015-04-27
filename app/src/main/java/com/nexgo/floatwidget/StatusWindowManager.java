package com.nexgo.floatwidget;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.PixelFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * @author duxd
 * @since 1.0.0
 */
public class StatusWindowManager {
    private static StatusView statusWindow;
    private static WindowManager.LayoutParams statusWindowParams;
    private static WindowManager mWindowManager;

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
                statusWindowParams.y = screenHeight;
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

    public static void updateTime(final Context context) {
        if (statusWindow != null) {
            TextView datetime = (TextView) statusWindow.findViewById(R.id.datetime);
            datetime.setText(getDateTime(context));

            BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
            ImageView bt = (ImageView) statusWindow.findViewById(R.id.bluetooth);
            if (bta != null && bta.isEnabled()) {
                bt.setImageResource(R.drawable.stat_sys_data_bluetooth_connected);
            } else {
                bt.setImageResource(R.drawable.stat_sys_data_bluetooth);
            }

            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            int level=WifiManager.calculateSignalLevel(info.getRssi(), 5);
            ImageView wifiimg = (ImageView) statusWindow.findViewById(R.id.wifi);
            switch (level) {
                case 0:
                    wifiimg.setImageResource(R.drawable.stat_sys_wifi_signal_null);
                    break;
                case 1:
                    wifiimg.setImageResource(R.drawable.stat_sys_wifi_signal_1_fully);
                    break;
                case 2:
                    wifiimg.setImageResource(R.drawable.stat_sys_wifi_signal_2_fully);
                    break;
                case 3:
                    wifiimg.setImageResource(R.drawable.stat_sys_wifi_signal_3_fully);
                    break;
                case 4:
                    wifiimg.setImageResource(R.drawable.stat_sys_wifi_signal_4_fully);
                    break;
            }
        }
    }

    public static String getDateTime(final Context context) {
        Calendar cal = Calendar.getInstance();
        int tmp;
        StringBuffer sb = new StringBuffer(19);
        sb.append(cal.get(Calendar.YEAR));
        sb.append("-");
        tmp = cal.get(Calendar.MONTH) + 1;
        if (tmp > 9) {
            sb.append(tmp);
        } else {
            sb.append(0);
            sb.append(tmp);
        }

        sb.append("-");
        tmp = cal.get(Calendar.DATE);
        if (tmp > 9) {
            sb.append(tmp);
        } else {
            sb.append(0);
            sb.append(tmp);
        }

        sb.append(" ");
        tmp = cal.get(Calendar.HOUR_OF_DAY);
        if (tmp > 9) {
            sb.append(tmp);
        } else {
            sb.append(0);
            sb.append(tmp);
        }

        sb.append(":");
        tmp = cal.get(Calendar.MINUTE);
        if (tmp > 9) {
            sb.append(tmp);
        } else {
            sb.append(0);
            sb.append(tmp);
        }

        sb.append(":");
        tmp = cal.get(Calendar.SECOND);
        if (tmp > 9) {
            sb.append(tmp);
        } else {
            sb.append(0);
            sb.append(tmp);
        }

        return sb.toString();
    }
}
