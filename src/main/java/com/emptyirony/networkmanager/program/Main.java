package com.emptyirony.networkmanager.program;

import com.emptyirony.networkmanager.database.MongoDB;
import com.emptyirony.networkmanager.quest.data.PlayerQuestData;
import com.emptyirony.networkmanager.quest.data.sub.QuestProgress;
import com.emptyirony.networkmanager.quest.object.TimeType;
import lombok.SneakyThrows;
import org.mongojack.DBQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/23 17:16
 * 4
 */
public class Main {
    private static MongoDB mongo;
    private static Timer dayTimer;
    private static Timer weekTimer;

    @SneakyThrows
    public static void main(String[] args) {
        mongo = new MongoDB();

        long dayS = 24 * 60 * 60 * 1000;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd '00:00:00'");
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
        if (System.currentTimeMillis() > startTime.getTime()) {
            startTime = new Date(startTime.getTime() + dayS);
        }

        dayTimer = new Timer();
        dayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (PlayerQuestData playerQuestData : mongo.getPlayerQuestDataJacksonMongoCollection().find()) {
                    if (playerQuestData == null) {
                        continue;
                    }
                    boolean done = false;
                    int failed = 0;
                    while (!done || failed <= 3) {
                        try {
                            List<QuestProgress> collect = playerQuestData.getActiveQuest().stream().filter(questProgress -> questProgress.getQuest().getTimeType() == TimeType.Day).collect(Collectors.toList());
                            playerQuestData.getActiveQuest().removeAll(collect);

                            mongo.getPlayerQuestDataJacksonMongoCollection().replaceOne(DBQuery.is("uuid", playerQuestData.getUuid()), playerQuestData);

                            done = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            failed++;
                        }
                    }
                }
            }
        }, startTime, dayS);

        long weekS = 24 * 60 * 60 * 1000 * 7;
        Date startTimew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
        if (System.currentTimeMillis() > startTimew.getTime()) {
            startTimew = new Date(startTimew.getTime() + weekS);
        }


        weekTimer = new Timer();
        weekTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (PlayerQuestData playerQuestData : mongo.getPlayerQuestDataJacksonMongoCollection().find()) {
                    if (playerQuestData == null) {
                        continue;
                    }
                    boolean done = false;
                    int failed = 0;
                    while (!done || failed <= 3) {
                        try {
                            List<QuestProgress> collect = playerQuestData.getActiveQuest().stream().filter(questProgress -> questProgress.getQuest().getTimeType() == TimeType.Weak).collect(Collectors.toList());
                            playerQuestData.getActiveQuest().removeAll(collect);

                            mongo.getPlayerQuestDataJacksonMongoCollection().replaceOne(DBQuery.is("uuid", playerQuestData.getUuid()), playerQuestData);

                            done = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            failed++;
                        }
                    }
                }
            }
        }, startTimew, weekS);
    }
}
