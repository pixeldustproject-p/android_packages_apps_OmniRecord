package org.omnirom.omnirecord;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserManager;
import android.provider.Settings;

public class TakeScreenRecordService extends Service {
    private static final String TAG = "TakeScreenRecordService";

    public static final String ACTION_STOP = "stop";
    public static final String ACTION_TOGGLE_POINTER = "toggle_pointer";
    public static final String ACTION_TOGGLE_HINT = "toggle_hint";
    public static final String ACTION_START = "start";
    public static final String ACTION_EXTRA_MODE = "mode";

    private static GlobalScreenRecord mScreenrecord;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Intents from screenrecord notification
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction().equals(ACTION_STOP)) {
                stopScreenrecord();
            } else if (intent.getAction().equals(ACTION_TOGGLE_POINTER)) {
                int currentStatus = Settings.System.getInt(getContentResolver(),
                        Settings.System.SHOW_TOUCHES, 0);
                Settings.System.putInt(getContentResolver(), Settings.System.SHOW_TOUCHES,
                        1 - currentStatus);
                mScreenrecord.updateNotification(-1);
            } else if (intent.getAction().equals(ACTION_TOGGLE_HINT)) {
                mScreenrecord.toggleHint();
            } else if (intent.getAction().equals(ACTION_START)) {
                Runnable finisher = new Runnable() {
                    @Override
                    public void run() {
                        stopSelf();
                    }
                };
                int mode = intent.getIntExtra(ACTION_EXTRA_MODE, GlobalScreenRecord.SCREEN_RECORD_MID_QUALITY);
                toggleScreenrecord(finisher, mode);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void startScreenrecord(Runnable finisher, int mode) {
        if (mScreenrecord == null) {
            mScreenrecord = new GlobalScreenRecord(TakeScreenRecordService.this);
        }
        mScreenrecord.takeScreenrecord(finisher, mode);
    }

    private void stopScreenrecord() {
        if (mScreenrecord == null) {
            return;
        }
        mScreenrecord.stopScreenrecord();

        // Turn off pointer in all cases
        Settings.System.putInt(getContentResolver(), Settings.System.SHOW_TOUCHES,
                0);
    }

    private void toggleScreenrecord(Runnable finisher, int mode) {
        if (mScreenrecord == null || !mScreenrecord.isRecording()) {
            startScreenrecord(finisher, mode);
        } else {
            stopScreenrecord();
        }
    }
}
