package com.emptyirony.networkmanager.quest.data;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.quest.data.sub.QuestProgress;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.WriteConcern;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.mongojack.DBQuery;

import java.util.*;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/23 16:41
 * 4
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PlayerQuestData {
    private static Map<UUID, PlayerQuestData> cache = new HashMap<>();
    private String uuid;
    private String name;
    private List<QuestProgress> activeQuest;

    public PlayerQuestData(UUID uuid, String name) {
        this.uuid = uuid.toString();
        this.name = name;
        this.activeQuest = new ArrayList<>();

        this.load();
    }

    public static PlayerQuestData getDataByUUID(UUID uuid) {
        return cache.get(uuid);
    }

    public PlayerQuestData load() {
        UUID uuidFromString = UUID.fromString(this.uuid);
        if (cache.containsKey(uuidFromString)) {
            return cache.get(uuidFromString);
        }
        PlayerQuestData data = NetworkManager.getInstance().getMongoDB()
                .getPlayerQuestDataJacksonMongoCollection()
                .find(DBQuery.is("uuid", this.uuid))
                .first();

        if (data == null) {
            cache.put(uuidFromString, this);
            return this;
        }

        cache.put(uuidFromString, data);
        return data;
    }

    public void save() {
        this.save(false);
    }

    public void save(boolean isQuit) {
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID fromString = UUID.fromString(PlayerQuestData.this.uuid);
                NetworkManager.getInstance().getMongoDB()
                        .getPlayerQuestDataJacksonMongoCollection()
                        .replaceOne(DBQuery.is("uuid", PlayerQuestData.this.uuid), cache.get(fromString), true, WriteConcern.NORMAL);
                if (isQuit) {
                    cache.remove(fromString);
                }
            }
        }.runTaskAsynchronously(NetworkManager.getInstance());
    }

}
