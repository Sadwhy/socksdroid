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

    // Called when the user adds the tile
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setLabel("SOCKS5"); // Set the tile label to "SOCKS5"
            // tile.setIcon(Icon.createWithResource(this, R.drawable.your_icon)); // Set the tile icon (commented out)
            updateTile();
        }
    }

    // Called when the tile is clicked
    @Override
    public void onClick() {
        super.onClick();
        if (isVpnRunning()) { // Corrected to call the method
            Utility.stopVpn(this;
            mRunning = false; // Update mRunning when VPN is stopped
        } else {
            Profile p = new ProfileManager(this).getDefault();
            Utility.startVpn(this, p);
            mRunning = true; // Update mRunning when VPN is started
        }
        updateTile(); // Update tile state after changing VPN status
    }

    // Update the tile to reflect the VPN status
    private void updateTile() {
        Tile tile = getQsTile();
        if (tile != null) {
            tile.setState(isVpnRunning() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE); // Corrected method call
            tile.updateTile();
        }
    }
}