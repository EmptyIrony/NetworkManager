package com.emptyirony.networkmanager.quest.event;

import com.emptyirony.networkmanager.quest.Quest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/23 16:30
 * 4
 */
@Getter
@AllArgsConstructor
public class QuestCompleteEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private Player player;
    private UUID uuid;
    private Quest quest;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


}
