package fi.fabianadrian.mmstatbridge.stat;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import fi.fabianadrian.mmstatbridge.MMStatBridge;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class StatisticCache {

    private final MMStatBridge plugin;

    public StatisticCache(MMStatBridge plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Map<Statistic, Integer>> cache = new ConcurrentHashMap<>();

    public int statistic(UUID uuid, Statistic statistic) {
        Map<Statistic, Integer> statMap = this.cache.get(uuid);
        if (statMap == null) return 0;

        return statMap.getOrDefault(statistic, 0);
    }

    public void update(UUID uuid) {
        final String TABLE_NAME = plugin.getConfig().getString("mysql.table");

        MMStatBridge.newSharedChain("data").async(() -> {
            try {
                DbRow dbRow = DB.getFirstRow("SELECT * from " + TABLE_NAME + " WHERE UUID='" + uuid.toString() + "';");
                if (dbRow == null || dbRow.isEmpty()) return;

                Map<Statistic, Integer> statisticMap = new EnumMap<>(Statistic.class);
                for (Statistic stat : Statistic.values()) {
                    int value = dbRow.getInt(stat.getName());
                    if (value == 0) continue;

                    statisticMap.put(stat, value);
                }

                this.cache.put(uuid, statisticMap);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }).execute();
    }

    public void remove(UUID uuid) {
        MMStatBridge.newSharedChain("data").async(() -> this.cache.remove(uuid)).execute();
    }
}
