package com.emptyirony.networkmanager;

import com.emptyirony.networkmanager.network.Network;
import com.emptyirony.networkmanager.pidgin.Pidgin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class NetworkManager extends JavaPlugin {
    @Getter
    private static NetworkManager instance;

    private Network network;
    private boolean canJoin;
    private Pidgin pidgin;
    @Getter
    @Setter
    private int serverType;


    @Override
    public void onEnable() {
        instance = this;
        this.pidgin = new Pidgin("network", "127.0.01", 6379, "");
        this.network = new Network();
        canJoin = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                canJoin = true;
            }
        }.runTaskLater(this, 20 * 5);
    }
}
