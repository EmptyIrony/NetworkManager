package com.emptyirony.networkmanager.bungee.data;

import com.emptyirony.networkmanager.bungee.BungeeNetwork;
import com.emptyirony.networkmanager.bungee.data.sub.ReportData;
import com.mongodb.WriteConcern;
import lombok.Data;
import org.mongojack.DBQuery;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/19 11:40
 * 4
 */
@Data
public class ReportsData {
    private static ReportsData ins;

    private static String KEY = "PANSHI";
    private Queue<ReportData> activeReports = new LinkedList<>();
    private Queue<ReportData> accepted = new LinkedList<>();

    public ReportsData() {
    }

    public static ReportsData get() {
        return ins;
    }

    public ReportsData load() {
        if (ins != null) {
            return ins;
        }

        ReportsData data = BungeeNetwork.getInstance().getMongoDB()
                .getReportsDataJacksonMongoCollection()
                .find(DBQuery.is("key", KEY))
                .first();

        if (data == null) {
            ins = this;
            this.save();
            return this;
        }

        ins = data;
        return data;
    }

    public void save() {
        BungeeNetwork.getInstance().getProxy().getScheduler().runAsync(BungeeNetwork.getInstance(), () -> {
            BungeeNetwork.getInstance().getMongoDB()
                    .getReportsDataJacksonMongoCollection()
                    .replaceOne(DBQuery.is("key", KEY), ins, true, WriteConcern.NORMAL);
        });
    }
}
