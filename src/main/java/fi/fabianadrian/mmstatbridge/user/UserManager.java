package fi.fabianadrian.mmstatbridge.user;

import fi.fabianadrian.mmstatbridge.MMStatBridge;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private final MMStatBridge plugin;
    private final List<User> userList = new ArrayList<>();

    public UserManager(MMStatBridge plugin) {
        this.plugin = plugin;
    }

    public User getUser(UUID uuid) {
        for (User user : userList) {
            if (user.uuid.equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    public void loadUser(Player player) {
        if (!player.isOnline()) {
            throw new IllegalStateException("Tried to get a user that is not online!");
        }
        userList.add(new User(plugin, player.getUniqueId()));
    }

    public void unloadUser(UUID uuid) {
        userList.removeIf(user -> user.uuid.equals(uuid));
    }
}
