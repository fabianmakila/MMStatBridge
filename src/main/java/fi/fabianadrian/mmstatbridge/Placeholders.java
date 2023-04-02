package fi.fabianadrian.mmstatbridge;

import fi.fabianadrian.mmstatbridge.stat.Statistic;
import fi.fabianadrian.mmstatbridge.stat.StatisticCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public final class Placeholders extends PlaceholderExpansion {

    private final StatisticCache statisticCache;

    public Placeholders(MMStatBridge plugin) {
        this.statisticCache = plugin.statisticCache();
    }

    @Override
    public @NotNull String getAuthor() {
        return "FabianAdrian";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "murdermystery";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.7.9";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        Statistic statistic;
        try {
            statistic = Statistic.valueOf(params.toUpperCase());
        } catch (IllegalArgumentException e) {
            return "0";
        }

        return String.valueOf(statisticCache.statistic(player.getUniqueId(), statistic));
    }
}
