package org.omnirom.omnirecord;

import android.content.Intent;
import android.service.quicksettings.TileService;


public class ScreenRecordTileService extends TileService {
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        Intent startIntent = new Intent(this, TakeScreenRecordService.class);
        startIntent.setAction(TakeScreenRecordService.ACTION_START);
        startService(startIntent);
    }
}
