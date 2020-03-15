package com.emptyirony.networkmanager.data;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.data.sub.GuildData;
import com.emptyirony.networkmanager.data.sub.PlayerOption;
import com.emptyirony.networkmanager.data.sub.StaffOption;
import com.emptyirony.networkmanager.data.sub.object.MsgType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.WriteConcern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mongojack.DBQuery;
import strafe.games.core.profile.Profile;
import strafe.games.core.util.callback.TypeCallback;

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
    private List<String> ignored = new ArrayList<>();
    private Set<String> friends = new HashSet<>();
    private String lastMsg;
    private StaffOption staffOption = new StaffOption(true, 0);
    private PlayerOption playerOption = new PlayerOption(false, MsgType.Friend);
    private boolean isNick;
    private String nickedName;
    private GuildData guildData;


    public PlayerData(UUID uuid) {
        this.uuid = uuid.toString();
    }

    public static PlayerData getByUuid(UUID uuid) {
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        return new PlayerData(uuid).load();
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

    public void isFriend(String myself, String targetName, TypeCallback<Boolean> callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Profile profile = Profile.getByUsername(myself);
                PlayerData data = PlayerData.getByUuid(profile.getUuid());

                Profile targetProfile = Profile.getByUsername(targetName);
                callback.callback(data.getFriends().contains(targetProfile.getUuid().toString()));
            }
        }.runTaskAsynchronously(NetworkManager.getInstance());
    }

    public PlayerData load() {

        if (this.uuid == null) {
            throw new NullPointerException("你必须使用带有uuid的构造函数进行构造，空构造函数仅供反序列化使用");
        }
        UUID id = UUID.fromString(uuid);
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        if (Bukkit.isPrimaryThread()) {
            throw new RuntimeException("那名玩家不在该服务器上，无法找到那名玩家的缓存，你必须异步获取玩家档案");
        }

        PlayerData data = NetworkManager.getInstance().getMongoDB()
                .getPlayerDataJacksonMongoCollection()
                .find(DBQuery.is("uuid", this.uuid))
                .first();

        if (data == null) {
            cache.put(id, this);
            this.save(false);
            return this;
        }
        cache.put(id, data);
        return data;
    }
}
