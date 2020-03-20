package com.emptyirony.networkmanager.bungee.data.sub;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/3/19 11:40
 * 4
 */
@Data
@NoArgsConstructor
public class ReportData {
    private String id;
    private String reporter;
    private String target;
    private String reason;
    private String receiver;

    public ReportData(String id, String reporter, String target, String reason) {
        this.id = id;
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
    }
}
