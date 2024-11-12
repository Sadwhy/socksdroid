package net.typeblog.socks;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import net.typeblog.socks.util.Profile;
import net.typeblog.socks.util.ProfileManager;
import net.typeblog.socks.util.Utility;

public class VpnTileService extends TileService {
    private boolean mRunning = false; // Define and initialize mRunning

    public boolean isVpnRunning() {
        return mRunning;
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
            updateTile();
            isVpnRunning();
        }
    }

    @Override
    public void onClick() {
        super.onClick();
        if (mRunning) {
            Utility.stopVpn(this);
            mRunning = false;
        } else {
            Profile p = new ProfileManager(this).getDefault();
            Utility.startVpn(this, p);
            mRunning = true;
        }
        updateTile();
    }

    private void updateTile() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(isVpnRunning() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE); // Corrected method call
            tile.updateTile();
        }
    }
}