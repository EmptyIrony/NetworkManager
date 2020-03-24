package com.emptyirony.networkmanager.quest;

import com.emptyirony.networkmanager.NetworkManager;
import com.emptyirony.networkmanager.quest.data.PlayerQuestData;
import com.emptyirony.networkmanager.quest.data.sub.QuestProgress;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/23 16:25
 * 4
 */
@Getter
public class QuestManager {
    @Getter
    private static QuestManager instance;
    private List<Quest> quests;

    public QuestManager() {
        instance = this;
        this.quests = new ArrayList<>();
    }

    public void addQuest(Quest quest) {
        Bukkit.getPluginManager().registerEvents(quest, NetworkManager.getInstance());
        this.quests.add(quest);
    }

    public void addQuestToPlayer(Player player, Quest quest) {
        PlayerQuestData data = PlayerQuestData.getDataByUUID(player.getUniqueId());
        QuestProgress progress = new QuestProgress();
        progress.setQuest(quest);
        data.getActiveQuest().add(progress);
    }

    public boolean hasQuest(Player player, Quest quest) {
        PlayerQuestData data = PlayerQuestData.getDataByUUID(player.getUniqueId());
        return data.getActiveQuest().stream().anyMatch(questProgress -> questProgress.getQuest().getName().equals(quest.getName()));
    }


}
