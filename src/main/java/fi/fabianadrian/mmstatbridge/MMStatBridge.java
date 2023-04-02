package fi.fabianadrian.mmstatbridge;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import co.aikar.idb.Database;
import fi.fabianadrian.mmstatbridge.listener.PlayerListener;
import fi.fabianadrian.mmstatbridge.stat.StatisticCache;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MMStatBridge extends JavaPlugin {
    private StatisticCache statisticCache;

    public StatisticCache statisticCache() {
        return this.statisticCache;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        FileConfiguration config = getConfig();
        String dbUser = config.getString("mysql.user");
        String dbPass = config.getString("mysql.password");
        String dbName = config.getString("mysql.database");
        String hostAndPort = config.getString("mysql.host");

        if (dbUser == null || dbPass == null || dbName == null || hostAndPort == null) {
            //Send message: Missing storage credentials, check config
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Database db = BukkitDB.createHikariDatabase(this, dbUser, dbPass, dbName, hostAndPort);
        DB.setGlobalDatabase(db);

        this.statisticCache = new StatisticCache(this);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }
    }
}
