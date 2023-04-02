package fi.fabianadrian.mmstatbridge.stat;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import fi.fabianadrian.mmstatbridge.MMStatBridge;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public final class StatisticCache {

    @Language("SQL")
    private static final String QUERY_TEMPLATE = "SELECT * from $1%s WHERE UUID = ?;";
    @Language("SQL")
    private final String QUERY;

    private final AsyncLoadingCache<UUID, Map<Statistic, Integer>> statistics = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .refreshAfterWrite(1, TimeUnit.MINUTES)
            .buildAsync(this::createStatistic);

    public StatisticCache(MMStatBridge plugin) {
        String tableName = plugin.getConfig().getString("mysql.table");
        this.QUERY = String.format(QUERY_TEMPLATE, tableName);
    }

    public int statistic(UUID uuid, Statistic statistic) {
        Map<Statistic, Integer> statMap = this.statistics.synchronous().getIfPresent(uuid);
        if (statMap == null) return 0;

        return statMap.getOrDefault(statistic, 0);
    }

    private Map<Statistic, Integer> createStatistic(UUID uuid) {
        try {
            DbRow dbRow = DB.getFirstRow(this.QUERY, uuid.toString());
            if (dbRow == null || dbRow.isEmpty()) return null;

            Map<Statistic, Integer> statisticMap = new EnumMap<>(Statistic.class);
            for (Statistic stat : Statistic.values()) {
                int value = dbRow.getInt(stat.getName());
                if (value == 0) continue;

                statisticMap.put(stat, value);
            }

            return statisticMap;
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
