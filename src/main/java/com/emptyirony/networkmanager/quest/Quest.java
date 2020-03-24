package com.emptyirony.networkmanager.quest;

import com.emptyirony.networkmanager.quest.data.PlayerQuestData;
import com.emptyirony.networkmanager.quest.data.sub.QuestProgress;
import com.emptyirony.networkmanager.quest.event.QuestCompleteEvent;
import com.emptyirony.networkmanager.quest.event.QuestProgressAddEvent;
import com.emptyirony.networkmanager.quest.object.TimeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/23 16:26
 * 4
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Quest implements Listener {
    private String name;
    private TimeType timeType;
    private int total;


    public void addProgress(Player player) {
        QuestProgressAddEvent event = new QuestProgressAddEvent(player, player.getUniqueId(), this);
        Bukkit.getPluginManager().callEvent(event);

        PlayerQuestData data = PlayerQuestData.getDataByUUID(player.getUniqueId());
        data.getActiveQuest().stream().filter(quest -> quest.getQuest().getName().equals(this.name)).forEach(questProgress -> {
            questProgress.setNow(questProgress.getNow() + 1);
            if (questProgress.getNow() >= total) {
                complete(player);
            }
        });

        data.save();
    }

    public void complete(Player player) {
        QuestCompleteEvent event = new QuestCompleteEvent(player, player.getUniqueId(), this);
        Bukkit.getPluginManager().callEvent(event);

        PlayerQuestData data = PlayerQuestData.getDataByUUID(player.getUniqueId());

        List<QuestProgress> collect = data.getActiveQuest().stream().filter(questProgress -> questProgress.getQuest().getName().equals(this.name)).collect(Collectors.toList());
        data.getActiveQuest().removeAll(collect);
        data.save();
    }


}
