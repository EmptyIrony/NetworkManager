package com.emptyirony.networkmanager.bungee.data;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.WriteConcern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongojack.DBQuery;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 10:08
 * 4
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginData {
    private static Map<UUID, LoginData> cache = new HashMap<>();
    private String uuid;
    private String lastLobby;
    private String lastGame;

    public LoginData(UUID uuid) {
        this.uuid = uuid.toString();
        this.lastLobby = "UNKNOW";
        this.lastGame = "UNKNOW";
    }

    public static LoginData getByUuid(UUID uuid) {
        if (cache.get(uuid) != null) {
            return cache.get(uuid);
        }
        return new LoginData(uuid).load();
    }

    public LoginData load() {
        if (this.uuid == null) {
            throw new NullPointerException("你必须使用带有uuid的构造函数进行构造，空构造函数仅供反序列化使用");
        }
        UUID id = UUID.fromString(uuid);
        if (cache.containsKey(id)) {
            return cache.get(id);
        }

        LoginData data = BungeeNetwork.getInstance().getMongoDB()
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

    public void save(boolean isQuit) {
        BungeeNetwork.getInstance().getProxy().getScheduler().runAsync(BungeeNetwork.getInstance(), () -> {
            UUID uuid = UUID.fromString(LoginData.this.uuid);
            BungeeNetwork.getInstance().getMongoDB()
                    .getPlayerDataJacksonMongoCollection()
                    .replaceOne(DBQuery.is("uuid", LoginData.this.uuid), cache.get(uuid), true, WriteConcern.NORMAL);

            if (isQuit) {
                cache.remove(uuid);
            }
        });
    }

}
