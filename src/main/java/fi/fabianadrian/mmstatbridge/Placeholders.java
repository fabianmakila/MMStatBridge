package fi.fabianadrian.mmstatbridge;

import fi.fabianadrian.mmstatbridge.user.StatStorage;
import fi.fabianadrian.mmstatbridge.user.User;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    private final MMStatBridge plugin;

    public Placeholders(MMStatBridge plugin) {
        this.plugin = plugin;
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
        return "0.1.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {

        User user = plugin.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "0";

        StatStorage.Statistic statistic;
        try {
            statistic = StatStorage.Statistic.valueOf(params.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }

        return String.valueOf(user.getStatistic(statistic));
    }
}
