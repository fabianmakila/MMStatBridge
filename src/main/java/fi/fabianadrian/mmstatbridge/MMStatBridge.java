package fi.fabianadrian.mmstatbridge;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import co.aikar.idb.Database;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import fi.fabianadrian.mmstatbridge.listener.PlayerListener;
import fi.fabianadrian.mmstatbridge.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MMStatBridge extends JavaPlugin {

    private static TaskChainFactory taskChainFactory;
    private UserManager userManager;

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public void onEnable() {
        taskChainFactory = BukkitTaskChainFactory.create(this);

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

        userManager = new UserManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }
    }
}
