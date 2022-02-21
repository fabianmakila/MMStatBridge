package fi.fabianadrian.mmstatbridge.listener;

import fi.fabianadrian.mmstatbridge.MMStatBridge;
import fi.fabianadrian.mmstatbridge.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final UserManager userManager;

    public PlayerListener(MMStatBridge plugin) {
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        userManager.loadUser(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        userManager.unloadUser(e.getPlayer().getUniqueId());
    }
}
