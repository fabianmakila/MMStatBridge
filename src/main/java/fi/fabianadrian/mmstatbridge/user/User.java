package fi.fabianadrian.mmstatbridge.user;

import fi.fabianadrian.mmstatbridge.MMStatBridge;

import java.util.UUID;

public class User extends StatStorage {

    public final UUID uuid;

    public User(MMStatBridge plugin, UUID uuid) {
        super(plugin, uuid);
        this.uuid = uuid;
    }
}
