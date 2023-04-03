package fi.fabianadrian.mmstatbridge.stat;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import fi.fabianadrian.mmstatbridge.MMStatBridge;
import org.intellij.lang.annotations.Language;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class StatisticCache {

    @Language("SQL")
    private static final String QUERY_TEMPLATE = "SELECT * FROM %s WHERE UUID = ?;";
    @Language("SQL")
    private final String QUERY;

    private final AsyncLoadingCache<UUID, Map<Statistic, Integer>> statistics = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .refreshAfterWrite(30, TimeUnit.SECONDS)
            .buildAsync(this::createStatistic);

    public StatisticCache(MMStatBridge plugin) {
        String tableName = plugin.getConfig().getString("mysql.table");
        this.QUERY = String.format(QUERY_TEMPLATE, tableName);
    }

    public Optional<Integer> statistic(UUID uuid, Statistic statistic) {
        Map<Statistic, Integer> statMap = this.statistics.synchronous().getIfPresent(uuid);
        if (statMap == null) {
            if (!this.statistics.asMap().containsKey(uuid)) {
                // Value is not in cache yet, fetch it asynchronously
                this.statistics.get(uuid);
            }
            return Optional.empty();
        } else {
            // Return the value if it's present, or an empty Optional if it's not
            return Optional.ofNullable(statMap.get(statistic));
        }
    }

    public void invalidate(UUID uuid) {
        this.statistics.synchronous().invalidate(uuid);
    }

    private Map<Statistic, Integer> createStatistic(UUID uuid) {
        try {
            DbRow dbRow = DB.getFirstRow(this.QUERY, uuid.toString());
            if (dbRow.isEmpty()) {
                return Collections.emptyMap();
            }

            Map<Statistic, Integer> statisticMap = new EnumMap<>(Statistic.class);
            for (Statistic stat : Statistic.values()) {
                int value = dbRow.getInt(stat.getName());
                if (value == 0) continue;

                statisticMap.put(stat, value);
            }

            return statisticMap;
        } catch (SQLException ex) {
            throw new IllegalStateException("Encountered a SQLException: ", ex);
        }
    }
}
