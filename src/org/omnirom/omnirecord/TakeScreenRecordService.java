/*
 *  Copyright (C) 2018 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
 package org.omnirom.omnirecord;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;

public class TakeScreenRecordService extends Service {
    private static final String TAG = "TakeScreenRecordService";

    public static final String ACTION_STOP = "stop";
    public static final String ACTION_TOGGLE_POINTER = "toggle_pointer";
    public static final String ACTION_TOGGLE_HINT = "toggle_hint";
    public static final String ACTION_START = "org.omnirom.omnirecord.ACTION_START";
    public static final String ACTION_EXTRA_MODE = "mode";

    private static GlobalScreenRecord mScreenrecord;

    public static class ScreenRecordReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (action.equals(ACTION_START)) {
                    Intent startIntent = new Intent(context, TakeScreenRecordService.class);
                    startIntent.setAction(TakeScreenRecordService.ACTION_START);
                    context.startService(startIntent);
                }
            } catch (Exception e) {
            }
        }
    }

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
                if (intent.hasExtra(ACTION_EXTRA_MODE)) {
                    int mode = intent.getIntExtra(ACTION_EXTRA_MODE, GlobalScreenRecord.SCREEN_RECORD_MID_QUALITY);
                    toggleScreenrecord(finisher, mode);
                } else {
                    toggleScreenrecord(finisher);
                }
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

    private void startScreenrecord(Runnable finisher) {
        if (mScreenrecord == null) {
            mScreenrecord = new GlobalScreenRecord(TakeScreenRecordService.this);
        }
        mScreenrecord.takeScreenrecord(finisher);
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

    private void toggleScreenrecord(Runnable finisher) {
        if (mScreenrecord == null || !mScreenrecord.isRecording()) {
            startScreenrecord(finisher);
        } else {
            stopScreenrecord();
        }
    }
}
