package fi.fabianadrian.mmstatbridge.user;

import co.aikar.idb.DB;
import co.aikar.idb.DbRow;
import fi.fabianadrian.mmstatbridge.MMStatBridge;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class StatStorage {

    private final Map<Statistic, Integer> statisticMap = new EnumMap<>(Statistic.class);

    public StatStorage(MMStatBridge plugin, UUID uuid) {

        final String TABLE_NAME = plugin.getConfig().getString("mysql.table");

        MMStatBridge.newSharedChain("data").async(() -> {
            try {
                DbRow dbRow = DB.getFirstRow("SELECT * from " + TABLE_NAME + " WHERE UUID='" + uuid.toString() + "';");
                if (dbRow == null || dbRow.isEmpty()) return;

                for (Statistic stat : Statistic.values()) {
                    int value = dbRow.getInt(stat.getName());
                    if (value == 0) continue;

                    statisticMap.put(stat, value);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }).execute();
    }

    public int getStatistic(Statistic statistic) {
        return statisticMap.getOrDefault(statistic, 0);
    }

    public enum Statistic {
        KILLS("kills"), DEATHS("deaths"), GAMES_PLAYED("gamesplayed"), WINS("wins"), LOSES("loses"), HIGHEST_SCORE("highestscore");

        private final String name;

        Statistic(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
