package com.stylingandroid.nougat;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.stylingandroid.nougat.messenger.ServiceScheduler;

@TargetApi(Build.VERSION_CODES.N)
public class MessengerTileService extends TileService {
    private ServiceScheduler serviceScheduler;

    @Override
    public void onCreate() {
        super.onCreate();
        serviceScheduler = ServiceScheduler.newInstance(this);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        if (serviceScheduler.isEnabled()) {
            updateTileState(Tile.STATE_ACTIVE);
        } else {
            updateTileState(Tile.STATE_INACTIVE);
        }
    }

    private void updateTileState(int state) {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(state);
            Icon icon = tile.getIcon();
            switch (state) {
                case Tile.STATE_ACTIVE:
                    icon.setTint(Color.WHITE);
                    break;
                case Tile.STATE_INACTIVE:
                case Tile.STATE_UNAVAILABLE:
                default:
                    icon.setTint(Color.GRAY);
                    break;
            }
            tile.updateTile();
        }
    }

    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();
        switch (tile.getState()) {
            case Tile.STATE_INACTIVE:
                serviceScheduler.startService();
                updateTileState(Tile.STATE_ACTIVE);
                break;
            case Tile.STATE_ACTIVE:
                serviceScheduler.stopService();
            default:
                updateTileState(Tile.STATE_INACTIVE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        serviceScheduler = null;
        super.onDestroy();
    }
}
