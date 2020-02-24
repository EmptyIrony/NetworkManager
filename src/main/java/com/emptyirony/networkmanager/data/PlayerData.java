package com.emptyirony.networkmanager.data;

import com.emptyirony.networkmanager.NetworkManager;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.WriteConcern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;
import org.mongojack.DBQuery;

import java.util.*;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 8:49
 * 4
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerData {
    private static Map<UUID, PlayerData> cache;

    static {
        cache = new HashMap<>();
    }

    private String uuid;
    private String name;
    private List<String> ignored;
    private List<UUID> friends;
    private String lastMsg;
    private boolean notify;


    public PlayerData(UUID uuid) {
        this.uuid = uuid.toString();
        this.ignored = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.notify = true;
        load();
    }

    private void load() {
        if (this.uuid == null) {
            throw new NullPointerException("你必须使用带有uuid的构造函数进行构造，空构造函数仅供反序列化使用");
        }
        UUID id = UUID.fromString(uuid);
        if (cache.containsKey(id)) {
            cache.get(id);
            return;
        }

        PlayerData data = NetworkManager.getInstance().getMongoDB()
                .getPlayerDataJacksonMongoCollection()
                .find(DBQuery.is("uuid", this.uuid))
                .first();

        if (data == null) {
            cache.put(id, this);
            this.save(false);
            return;
        }
        cache.put(id, data);
    }

    public void save(boolean isQuit) {
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID uuid = UUID.fromString(PlayerData.this.uuid);
                NetworkManager.getInstance().getMongoDB()
                        .getPlayerDataJacksonMongoCollection()
                        .replaceOne(DBQuery.is("uuid", PlayerData.this.uuid), cache.get(uuid), true, WriteConcern.NORMAL);

                if (isQuit) {
                    cache.remove(uuid);
                }
            }
        }.runTaskLaterAsynchronously(NetworkManager.getInstance(), 2L);
    }
}
