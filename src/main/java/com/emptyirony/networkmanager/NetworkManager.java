package com.emptyirony.networkmanager;

import com.emptyirony.networkmanager.chat.command.IgnoreCommand;
import com.emptyirony.networkmanager.chat.command.MsgCommand;
import com.emptyirony.networkmanager.chat.command.ReplyCommand;
import com.emptyirony.networkmanager.chat.command.StreamCommand;
import com.emptyirony.networkmanager.chat.listener.ChatListener;
import com.emptyirony.networkmanager.data.listener.DataListener;
import com.emptyirony.networkmanager.database.MongoDB;
import com.emptyirony.networkmanager.network.Network;
import com.minexd.pidgin.Pidgin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import strafe.games.core.Stone;

@Getter
public final class NetworkManager extends JavaPlugin {
    @Getter
    private static NetworkManager instance;
    private Network network;
    private MongoDB mongoDB;
    private boolean canJoin;
    private Pidgin pidgin;
    @Getter
    @Setter
    private int serverType;


    @Override
    public void onEnable() {
        instance = this;
        loadRedis();
        this.network = new Network();
        initDatabase();
        initChat();
        canJoin = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                canJoin = true;
            }
        }.runTaskLater(this, 20 * 5);
    }

    private void initDatabase() {
        System.out.println("正在连接数据库...");
        this.mongoDB = new MongoDB();
        System.out.println("数据库连接成功！");
        this.getServer().getPluginManager().registerEvents(new DataListener(), this);
        System.out.println("数据监听器初始化成功");
    }

    private void initChat() {
        System.out.println("初始化聊天系统...");
        Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), NetworkManager.getInstance());
        Stone.get().getHoncho().registerCommand(new IgnoreCommand());
        Stone.get().getHoncho().registerCommand(new MsgCommand());
        Stone.get().getHoncho().registerCommand(new ReplyCommand());
        Stone.get().getHoncho().registerCommand(new StreamCommand());
        System.out.println("聊天系统初始完成");
    }

    private void loadRedis() {
        System.out.println("connecting to Redis");
        this.pidgin = new Pidgin("network", "127.0.01", 6379, "");
        System.out.println("successfully connected Redis!");
    }
}
