package com.emptyirony.networkmanager.quest.data.sub;

import com.emptyirony.networkmanager.quest.Quest;
import com.emptyirony.networkmanager.quest.object.TimeType;
import lombok.Data;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/23 16:41
 * 4
 */
@Data
public class QuestProgress {
    private int now;
    private Quest quest;

    public QuestProgress() {
        this.now = 0;
    }

    public int getTotal() {
        return this.quest.getTotal();
    }

    public TimeType getTimeType() {
        return this.quest.getTimeType();
    }

    public String getQuestName() {
        return this.quest.getName();
    }
}
