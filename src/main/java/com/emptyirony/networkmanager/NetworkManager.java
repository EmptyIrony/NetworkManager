package com.emptyirony.networkmanager;

import com.emptyirony.networkmanager.chat.command.IgnoreCommand;
import com.emptyirony.networkmanager.chat.command.MsgCommand;
import com.emptyirony.networkmanager.chat.listener.ChatListener;
import com.emptyirony.networkmanager.data.listener.DataListener;
import com.emptyirony.networkmanager.database.MongoDB;
import com.emptyirony.networkmanager.network.Network;
import com.minexd.pidgin.Pidgin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import strafe.games.core.Stone;

public final class NetworkManager extends JavaPlugin {
    @Getter
    private static NetworkManager instance;
    @Getter
    private Network network;
    @Getter
    private MongoDB mongoDB;
    @Getter
    private Pidgin pidgin;
    @Getter
    private JedisPool jedisPool;

    @Override
    public void onEnable() {
        instance = this;
        loadRedis();
        this.network = new Network();
        initDatabase();
        initChat();
    }

    private void initDatabase() {
        System.out.println("正在连接数据库...");
        this.mongoDB = new MongoDB();
        System.out.println("数据库连接成功！");
        System.out.println("初始化数据监听器...");
        this.getServer().getPluginManager().registerEvents(new DataListener(), this);
        System.out.println("数据监听器初始化成功");
    }

    private void initChat() {
        System.out.println("初始化聊天系统...");
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), NetworkManager.getInstance());
        System.out.println("聊天监听系统初始完成");
        Stone.get().getHoncho().registerCommand(new IgnoreCommand());
        Stone.get().getHoncho().registerCommand(new MsgCommand());
        System.out.println("聊天指令系统初始完成");
    }

    private void loadRedis() {
        System.out.println("connecting to Redis");
        jedisPool = new JedisPool("127.0.0.1", 6379);
//        this.pidgin = new Pidgin("network","127.0.0.1",6379,null);
        this.pidgin = Stone.get().getPidgin();
        System.out.println("successfully connected Redis!");
    }
}
