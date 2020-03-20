package com.emptyirony.networkmanager.bungee.database;

import com.emptyirony.networkmanager.bungee.data.LoginData;
import com.emptyirony.networkmanager.bungee.data.ReportsData;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.mongojack.JacksonMongoCollection;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/2/26 10:17
 * 4
 */
@Getter
public class MongoDB {
    private MongoDatabase database;
    private MongoClient client;

    private JacksonMongoCollection<LoginData> playerDataJacksonMongoCollection;

    private JacksonMongoCollection<ReportsData> reportsDataJacksonMongoCollection;

    public MongoDB() {
        this.client = new MongoClient(new ServerAddress("127.0.0.1", 27017));

        this.database = client.getDatabase("network");

        this.playerDataJacksonMongoCollection = JacksonMongoCollection.<LoginData>builder().build(this.database.getCollection("loginData"), LoginData.class);

        this.reportsDataJacksonMongoCollection = JacksonMongoCollection.<ReportsData>builder().build(this.database.getCollection("reportsData"), ReportsData.class);
    }
}
