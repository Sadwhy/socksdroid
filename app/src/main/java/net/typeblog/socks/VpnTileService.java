package net.typeblog.socks;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.VpnService;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;
import net.typeblog.socks.IVpnService;
import net.typeblog.socks.SocksVpnService;

public class VpnTileService extends TileService {
    private IVpnService mBinder;
    private boolean isVpnRunning = false;

    // Called when the user adds the tile
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        updateTile();
    }

    // Called when the tile is clicked
    @Override
    public void onClick() {
        super.onClick();
        if (isVpnRunning) {
            stopVpn();
        } else {
            startVpn();
        }
    }

    // Update the tile to reflect the VPN status
    private void updateTile() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(isVpnRunning ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }

    private void startVpn() {
        Intent prepareIntent = VpnService.prepare(this);
        if (prepareIntent != null) {
            startActivity(prepareIntent);
        } else {
            startVpnService();
        }
    }

    private void startVpnService() {
        Intent serviceIntent = new Intent(this, SocksVpnService.class);
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
        isVpnRunning = true;
        updateTile();
    }

    private void stopVpn() {
        if (mBinder != null) {
            try {
                mBinder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            unbindService(mConnection);
            isVpnRunning = false;
            updateTile();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = IVpnService.Stub.asInterface(service);
            isVpnRunning = true;
            updateTile();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
            isVpnRunning = false;
            updateTile();
        }
    };
}