package fi.fabianadrian.mmstatbridge.papi;

import fi.fabianadrian.mmstatbridge.MMStatBridge;
import fi.fabianadrian.mmstatbridge.stat.Statistic;
import fi.fabianadrian.mmstatbridge.stat.StatisticCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class MurderMysteryPlaceholderExpansion extends PlaceholderExpansion {

    private final StatisticCache statisticCache;

    public MurderMysteryPlaceholderExpansion(MMStatBridge plugin) {
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
            return null;
        }

        Optional<Integer> optional = this.statisticCache.statistic(player.getUniqueId(), statistic);
        if (optional.isPresent()) {
            return String.valueOf(optional.get());
        }

        return "loading";
    }
}
