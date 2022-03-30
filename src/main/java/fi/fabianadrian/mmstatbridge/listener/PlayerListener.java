package fi.fabianadrian.mmstatbridge.listener;

import fi.fabianadrian.mmstatbridge.MMStatBridge;
import fi.fabianadrian.mmstatbridge.stat.StatisticCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {

    private final StatisticCache statisticCache;

    public PlayerListener(MMStatBridge plugin) {
        this.statisticCache = plugin.statisticCache();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        statisticCache.update(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        statisticCache.remove(e.getPlayer().getUniqueId());
    }
}
