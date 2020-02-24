package com.emptyirony.networkmanager.database;

import com.emptyirony.networkmanager.data.PlayerData;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.mongojack.JacksonMongoCollection;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/24 8:53
 * 4
 */
@Getter
public class MongoDB {
    private MongoDatabase database;
    private MongoClient client;

    private JacksonMongoCollection<PlayerData> playerDataJacksonMongoCollection;

    public MongoDB() {
        this.client = new MongoClient(new ServerAddress("127.0.0.1", 27017));

        this.database = client.getDatabase("network");

        this.playerDataJacksonMongoCollection = JacksonMongoCollection.<PlayerData>builder().build(this.database.getCollection("data"), PlayerData.class);
    }
}
