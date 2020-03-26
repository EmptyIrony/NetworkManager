package com.emptyirony.networkmanager;

import com.emptyirony.networkmanager.network.Network;
import com.minexd.pidgin.Pidgin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.nicknamer.command.GeneralCommands;
import org.inventivetalent.nicknamer.command.NickCommands;
import org.inventivetalent.nicknamer.command.SkinCommands;
import strafe.games.core.Stone;

@Getter
public class NetworkManager extends JavaPlugin {
    @Getter
    private static NetworkManager instance;
    @Getter
    @Deprecated
    private static NetworkManager ins;
    private Network network;
    private boolean canJoin;
    private Pidgin pidgin;
    @Getter
    @Setter
    private int serverType;

    public GeneralCommands generalCommands;
    public NickCommands nickCommands;
    public SkinCommands skinCommands;


    @Override
    public void onEnable() {
        instance = this;
        ins = this;
        loadRedis();
        this.network = new Network();
        canJoin = false;

        new BukkitRunnable() {
            @Override
            public void run() {
                canJoin = true;
            }
        }.runTaskLater(this, 20 * 5);

        Stone.get().getHoncho().registerCommand(new TestCommand());
    }

    private void loadRedis() {
        System.out.println("connecting to Redis");
        this.pidgin = new Pidgin("network", "127.0.01", 6379, "");
        System.out.println("successfully connected Redis!");
    }

}
